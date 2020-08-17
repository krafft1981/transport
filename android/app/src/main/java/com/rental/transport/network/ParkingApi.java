package com.rental.transport.network;

import com.rental.transport.model.Parking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ParkingApi {

    @Headers("Content-Type: application/json")
    @GET("/parking")
    public Call<Parking> getParking(@Query("id") Long id);

    @GET("/parking/list")
    public Call<List<Parking>> getParkingList(
            @Query("page") Integer page,
            @Query("size") Integer size);

    @Headers("Content-Type: application/json")
    @POST("/parking")
    public Call<Long> doPostParking();

    @Headers("Content-Type: application/json")
    @DELETE("/parking")
    public Call doDeleteParking(@Query("id")Long id);

    @Headers("Content-Type: application/json")
    @PUT("/parkingApi")
    public Call doPutParking(@Body ParkingApi parkingApi);
}
