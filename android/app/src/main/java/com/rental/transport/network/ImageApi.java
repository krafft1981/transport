package com.rental.transport.network;

import com.rental.transport.model.Image;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ImageApi {

    @Headers("Content-Type: application/json")
    @POST("/image")
    public Call<Long> doPostImage(@Body String image);

    @Headers("Content-Type: application/json")
    @GET("/image")
    public Call<Image> doGetImage(@Query("id") Long id);

    @Headers("Content-Type: application/json")
    @DELETE("/image")
    public Call doDeleteImage(@Query("id") Long[] id);
}
