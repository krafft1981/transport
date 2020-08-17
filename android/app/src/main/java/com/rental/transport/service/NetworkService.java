package com.rental.transport.service;

import com.rental.transport.network.CustomerApi;
import com.rental.transport.network.OrderApi;
import com.rental.transport.network.ParkingApi;
import com.rental.transport.network.RegistrationApi;
import com.rental.transport.network.TransportApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private static NetworkService mInstance;
    private static final String BASE_URL = "http://88.200.201.2:8080";
    private Retrofit mRetrofit;

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    private NetworkService() {

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public RegistrationApi getRegistrationApi() {
        return mRetrofit.create(RegistrationApi.class);
    }

    public CustomerApi getCustomerApi() {
        return mRetrofit.create(CustomerApi.class);
    }

    public TransportApi getTransportApi() {
        return mRetrofit.create(TransportApi.class);
    }

    public OrderApi getOrderApi() {
        return mRetrofit.create(OrderApi.class);
    }

    public ParkingApi getParkingApi() {
        return mRetrofit.create(ParkingApi.class);
    }
}
