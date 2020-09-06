package com.rental.transport.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.rental.transport.R;
import com.rental.transport.model.Image;
import com.rental.transport.service.NetworkService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerSetting extends Fragment {

    ProgressBar progress;
    ImageView image;
    Button buttonSave;
    Button buttonGet;
    TextView md5viewIn;
    TextView md5viewOut;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.customer_setting, container, false);

        buttonGet = (Button) root.findViewById(R.id.buttonGet);
        buttonSave = (Button) root.findViewById(R.id.buttonSave);
        progress = (ProgressBar) getActivity().findViewById(R.id.progress);
        image = (ImageView) root.findViewById(R.id.image);
        md5viewIn = (TextView) root.findViewById(R.id.md5summIn);
        md5viewOut = (TextView) root.findViewById(R.id.md5summOut);

        View.OnClickListener oclBtnGet = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = (EditText) root.findViewById(R.id.textGet);
                Long id = Long.parseLong(text.getText().toString());

                progress.setVisibility(View.VISIBLE);

                NetworkService
                        .getInstance()
                        .getImageApi()
                        .doGetImage(id)
                        .enqueue(new Callback<Image>() {
                            @Override
                            public void onResponse(@NonNull Call<Image> call, @NonNull Response<Image> response) {

                                progress.setVisibility(View.GONE);

                                Image data = response.body();
                                if (data != null) {

                                    try {
                                        byte[] bytesOfMessage = data.getData().getBytes("UTF-8");

                                        MessageDigest md = MessageDigest.getInstance("MD5");
                                        byte[] md5sum = md.digest(bytesOfMessage);

                                        md5viewIn.setText(bytesToHex(md5sum));
                                    }
                                    catch (Exception e) {

                                    }

                                    String base64String = data.getData();
                                    byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    image.setImageBitmap(decodedByte);
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Image> call, @NonNull Throwable t) {

                                progress.setVisibility(View.GONE);
                                Toast
                                        .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
            }
        };

        View.OnClickListener oclBtnSave = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 2);

                progress.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            }
        };


        buttonGet.setOnClickListener(oclBtnGet);
        buttonSave.setOnClickListener(oclBtnSave);

        return root;
    }

    // Sending side
    //byte[] data = text.getBytes("UTF-8");
    //String base64 = Base64.encodeToString(data, Base64.DEFAULT);

    // Receiving side
    //byte[] data = Base64.decode(base64, Base64.DEFAULT);
    //String text = new String(data, "UTF-8");

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            try {
                Uri uri = data.getData();
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri), null, options);
                image.setImageBitmap(bitmap);

                Integer size = getActivity().getContentResolver().openInputStream(uri).read();
                md5viewOut.setText(size.toString() + " OK");

/*
                byte[] total = convertStreamToString(inputStream).getBytes("UTF-8");
                String base64String = Base64.encodeToString(total, Base64.DEFAULT);

                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] md5sum = md.digest(base64String.getBytes());

                md5viewOut.setText(base64String.length() + " " + bytesToHex(md5sum));
                progress.setVisibility(View.VISIBLE);

                NetworkService
                        .getInstance()
                        .getImageApi()
                        .doPostImage(base64String)
                        .enqueue(new Callback<Long>() {
                            @Override
                            public void onResponse(@NonNull Call<Long> call, @NonNull Response<Long> response) {

                                progress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(@NonNull Call<Long> call, @NonNull Throwable t) {

                                progress.setVisibility(View.GONE);
                                Toast
                                        .makeText(getActivity(), t.toString(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
*/
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
