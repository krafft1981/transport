package com.rental.transport.network;

import com.rental.transport.model.Calendar;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CalendarApi {

    @Headers("Content-Type: application/json")
    @GET("/calendar/count")
    public Call<Long> doGetCountRequest();

    @Headers("Content-Type: application/json")
    @PUT("/calendar")
    public Call<Void> doPutOutRequest(
            @Query("start") Integer start,
            @Query("stop") Integer stop);

    @Headers("Content-Type: application/json")
    @DELETE("/calendar")
    public Call<Void> doDeleteOutRequest(
            @Query("start") Integer start,
            @Query("stop") Integer stop);

    @Headers("Content-Type: application/json")
    @GET("/calendar")
    public Call<Void> doGetOutRequest(
            @Query("start") Integer start,
            @Query("stop") Integer stop);

    @Headers("Content-Type: application/json")
    @GET("/calendar/byTime")
    public Call<List<Calendar>> doGetByTimeRequest(
            @Query("start") Integer start,
            @Query("stop") Integer stop);
}
