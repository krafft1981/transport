package com.rental.transport.network;

import com.rental.transport.model.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface OrderApi {

    @Headers("Content-Type: application/json")
    @GET("/order")
    public Call<Order> getOrder(@Query("id") Long id);

    @GET("/order/list")
    public Call<List<Order>> getOrderList(
            @Query("page") Integer page,
            @Query("size") Integer size);

    @Headers("Content-Type: application/json")
    @POST("/order")
    public Call<Long> doPostOrder();

    @Headers("Content-Type: application/json")
    @DELETE("/order")
    public Call doDeleteOrder(@Query("id")Long id);

    @Headers("Content-Type: application/json")
    @PUT("/orderApi")
    public Call doPutOrder(@Body OrderApi orderApi);
}
