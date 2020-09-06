package com.rental.transport.adapter;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.rental.transport.R;
import com.rental.transport.model.Image;
import com.rental.transport.model.Transport;
import com.rental.transport.service.NetworkService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Random;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransportGridAdapter extends BaseAdapter {

    private Context context;
    private List<Transport> data;

    public TransportGridAdapter(Context context, List<Transport> data) {

        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int id) {
        return data.get(id);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int id, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.transport_element, null);

        Transport info = data.get(id);
        TextView description = item.findViewById(R.id.transport_description);

        if ((info.getType() != null) && (info.getName() != null)) {
            StringBuilder builder = new StringBuilder();
            builder.append(info.getType());
            builder.append(" ");
            builder.append(info.getName());
            description.setText(builder.toString());
        }

        if (info.getImages().size() > 0) {

            Random rand = new Random();
            Long imageId = info.getImages().get(rand.nextInt(info.getImages().size()));

            File file = new File(context.getCacheDir(), imageId.toString());

            if (file.exists()) {
                try {
                    FileInputStream in = new FileInputStream(file);
//                    in.read(buffer, 0, bufferlength);
                }
                catch (Exception e) {

                }
            }

            else {
                NetworkService
                        .getInstance()
                        .getImageApi()
                        .doGetImage(imageId)
                        .enqueue(new Callback<Image>() {
                            @Override
                            public void onResponse(@NonNull Call<Image> call, @NonNull Response<Image> response) {

                                Toast
                                        .makeText(context, "Картинка подргужена", Toast.LENGTH_LONG)
                                        .show();

                                Image image = response.body();
                                String base64String = image.getData();
                                byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                                try {

                                    FileOutputStream out = new FileOutputStream(file);
                                    out.write(decodedString, 0, decodedString.length);
                                }

                                catch (Exception e) {

                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Image> call, @NonNull Throwable t) {

                                Toast
                                        .makeText(context, t.toString(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
            }
/*
            int[] images = {
                    R.drawable.i0,
                    R.drawable.i1,
                    R.drawable.i2,
                    R.drawable.i3,
                    R.drawable.i4,
                    R.drawable.i5,
                    R.drawable.i6,
                    R.drawable.i7,
                    R.drawable.i8,
                    R.drawable.i9,
                    R.drawable.i10,
                    R.drawable.i11,
                    R.drawable.i12,
                    R.drawable.i13,
                    R.drawable.i14,
                    R.drawable.i15,
                    R.drawable.i16
            };
*/

//            ImageView image = item.findViewById(R.id.gridview_image);
//            image.setImageResource(images[rand.nextInt(images.length)]);
        }
//        Random random = new Random();
//        if (dataElement != null) {
//            String base64String = dataElement.get(random.nextInt(dataElement.size())).getData();
//            byte[] decodedString = Base64.decode(base64String.split(",")[1], Base64.DEFAULT);
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            image.setImageBitmap(decodedByte);
//        }

        return item;
    }
}
