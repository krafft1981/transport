package com.rental.transport.network;

import com.rental.transport.model.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface CalendarApi {

    @Headers("Content-Type: application/json")
    @GET("/calendar")
    public Call<List<Calendar>> doGetCalendar(
            @Query("start") Integer start,
            @Query("stop") Integer stop);
}
