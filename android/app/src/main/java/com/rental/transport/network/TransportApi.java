package com.rental.transport.network;

import com.rental.transport.model.Transport;

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
    public Call<Transport> doGetTransport(@Query("id") Long id);

    @GET("/transport/list")
    public Call<List<Transport>> doGetTransportList(
            @Query("page") Integer page,
            @Query("size") Integer size);

    @GET("/transport/list/type")
    public Call<List<Transport>> doGetTransportListByType(
            @Query("page") Integer page,
            @Query("size") Integer size,
            @Query("type") String type);

    @Headers("Content-Type: application/json")
    @POST("/transport")
    public Call<Long> doPostTransport(@Query("type")String type);

    @Headers("Content-Type: application/json")
    @DELETE("/transport")
    public Call doDeleteTransport(@Query("id")Long id);

    @Headers("Content-Type: application/json")
    @PUT("/transport")
    public Call doPutTransport(@Body Transport transport);

    @Headers("Content-Type: application/json")
    @GET("/transport/count")
    public Call<Long> doGetCount();

    @Headers("Content-Type: application/json")
    @GET("/transport/count/type")
    public Call<Long> doGetCountByType(@Query("type")String type);
}
