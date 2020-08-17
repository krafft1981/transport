package com.rental.transport.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface TransportApi {

    @Headers("Content-Type: application/json")
    @GET("/transport")
    public Call<com.rental.transport.model.Transport> getTransport(@Query("id") Long id);

    @GET("/transport/list")
    public Call<List<com.rental.transport.model.Transport>> getTransportList(
            @Query("page") Integer page,
            @Query("size") Integer size);

    @Headers("Content-Type: application/json")
    @POST("/transport")
    public Call<Long> doPostTransport(@Query("type")String type);

    @Headers("Content-Type: application/json")
    @DELETE("/transport")
    public Call doDeleteTransport(@Query("id")Long id);

    @Headers("Content-Type: application/json")
    @PUT("/transportApi")
    public Call doPutTransport(@Body TransportApi transportApi);
}
