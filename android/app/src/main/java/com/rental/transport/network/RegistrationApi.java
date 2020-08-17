package com.rental.transport.network;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RegistrationApi {

    @Headers("Content-Type: application/json")
    @POST("/registration")
    public Call<Long> postRegistration(@Query("account") String account);
}
