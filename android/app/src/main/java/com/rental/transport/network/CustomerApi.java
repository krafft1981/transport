package com.rental.transport.network;

import com.rental.transport.model.Customer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CustomerApi {

    @Headers("Content-Type: application/json")
    @GET("/customer")
    public Call<Customer> getCustomer(@Query("id") Long id);

    @GET("/customer/list")
    public Call<List<Customer>> getCustomerList(
            @Query("page") Integer page,
            @Query("size") Integer size);


    @Headers("Content-Type: application/json")
    @PUT("/customerApi")
    public Call doPutOrder(@Body CustomerApi customerApi);
}
