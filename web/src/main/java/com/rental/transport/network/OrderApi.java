package com.rental.transport.network;

import com.rental.transport.model.Order;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface OrderApi {

    @Headers("Content-Type: application/json")
    @GET("/order")
    public Call<Order> doGetOrder(@Query("id") Long id);

    @Headers("Content-Type: application/json")
    @GET("/order/list")
    public Call<List<Order>> doGetOrderList(
            @Query("page") Integer page,
            @Query("size") Integer size);
}
