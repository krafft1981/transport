package com.rental.transport.network;

import com.rental.transport.model.Type;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TypeApi {

    @GET("/type/list")
    public Call<List<Type>> doGetTypeList(
            @Query("page") Integer page,
            @Query("size") Integer size);

    @POST("/type")
    public Call<Long> doPostType(
            @Query("name") Integer name);

    @GET("/type/count")
    public Call<Long> doGetTypeCount();
}
