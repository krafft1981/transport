package com.rental.transport.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.rental.transport.model.Image;

import java.io.File;
import java.io.FileOutputStream;

import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageService {

    private static ImageService mInstance;
    private Context context;

    public ImageService(Context context) {
        this.context = context;
    }

    public static ImageService getInstance(@NonNull Context context) {
        if (mInstance == null) {
            mInstance = new ImageService(context);
        }

        return mInstance;
    }


    private File getFile(Long id) {
        return new File(context.getCacheDir(), id.toString());
    }

    private Boolean getImageFromCache(Long id, LinearLayout layout) {

        File file = getFile(id);
        if (file.exists()) {
            ImageView iv = new ImageView (context);
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                iv.setImageBitmap(bitmap);

                layout.addView(iv);
                layout.invalidate();
            }

            catch (Exception e) {
                file.delete();
                return false;
            }
        }

        return true;
    }

    public void setImage(Long id, int defaultImage, ImageView imageView) {

    }

    public void setImage(Long id, int defaultImage, LinearLayout layout) {

        if (getImageFromCache(id, layout) == false) {
            NetworkService
                    .getInstance()
                    .getImageApi()
                    .doGetImage(id)
                    .enqueue(new Callback<Image>() {
                        @Override
                        public void onResponse(@NonNull Call<Image> call, @NonNull Response<Image> response) {
                            Image body = response.body();
                            String base64String = body.getData();
                            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);

                            try {
                                File file = getFile(id);
                                FileOutputStream out = new FileOutputStream(file);
                                out.write(decodedString, 0, decodedString.length);
                            }

                            catch (Exception e) {

                            }

                            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            ImageView iv = new ImageView (context);
                            iv.setImageBitmap(bitmap);

                            layout.addView(iv);
                            layout.invalidate();
                        }

                        @Override
                        public void onFailure(@NonNull Call<Image> call, @NonNull Throwable t) {
                            ImageView iv = new ImageView (context);
                            iv.setImageResource(defaultImage);

                            layout.addView(iv);
                            layout.invalidate();
                        }
                    });
        }
    }
}

/*
            File file = new File(context.getCacheDir(), imageId.toString());

            if (file.exists()) {
                try {
                    FileInputStream in = new FileInputStream(file);
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    ImageView image = item.findViewById(R.id.gridview_image);
                    image.setImageBitmap(bitmap);
                }
                catch (Exception e) {
                    Toast
                            .makeText(context, "Битая картинка: " + imageId, Toast.LENGTH_LONG)
                            .show();
                    file.delete();
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

                                Image body = response.body();

                                String base64String = body.getData();
                                byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);

                                try {
                                    FileOutputStream out = new FileOutputStream(file);
                                    out.write(decodedString, 0, decodedString.length);
                                }

                                catch (Exception e) {}

                                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                ImageView image = item.findViewById(R.id.gridview_image);
                                image.setImageBitmap(bitmap);
                                image.invalidate();
                            }

                            @Override
                            public void onFailure(@NonNull Call<Image> call, @NonNull Throwable t) {

                                Toast
                                        .makeText(context, t.toString(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
            }
 */
