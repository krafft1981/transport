package com.rental.transport.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RegistrationApi {

    @Headers("Content-Type: application/json")
    @POST("/registration")
    public Call<Long> doPostRegistration(@Query("account") String account);

    @GET("/registration/exist")
    public Call<Boolean> doGetCustomerExist(
            @Query("account") String account
    );
}
