package com.rental.transport.network;

import com.rental.transport.model.Transport;
import com.rental.transport.model.Type;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface TypeApi {

    @Headers("Content-Type: application/json")
    @GET("/type")
    public Call<Transport> doGetType(@Query("id") Long id);

    @GET("/type/list")
    public Call<List<Transport>> doGetTypeList(
            @Query("page") Integer page,
            @Query("size") Integer size);

    @Headers("Content-Type: application/json")
    @POST("/type")
    public Call<Long> doPostType(@Query("type") String type);

    @Headers("Content-Type: application/json")
    @DELETE("/type")
    public Call doDeleteType(@Query("id") Long id);

    @Headers("Content-Type: application/json")
    @PUT("/type")
    public Call doPutType(@Body Type type);
}
