package com.rental.transport.service;

import com.burgstaller.okhttp.AuthenticationCacheInterceptor;
import com.burgstaller.okhttp.CachingAuthenticatorDecorator;
import com.burgstaller.okhttp.digest.CachingAuthenticator;
import com.burgstaller.okhttp.digest.Credentials;
import com.burgstaller.okhttp.digest.DigestAuthenticator;
import com.rental.transport.network.CalendarApi;
import com.rental.transport.network.CustomerApi;
import com.rental.transport.network.ImageApi;
import com.rental.transport.network.OrderApi;
import com.rental.transport.network.ParkingApi;
import com.rental.transport.network.RegistrationApi;
import com.rental.transport.network.TransportApi;
import com.rental.transport.network.TypeApi;
import com.rental.transport.network.WorkTimeApi;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.NonNull;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private String baseUrl="http://88.200.201.2:8080";
    private String password="password";

    private static NetworkService mInstance;

    private Retrofit mRetrofit;
    private Retrofit mRetrofitDidest;

    public static NetworkService getInstance(@NonNull String account) {
        if (mInstance == null) {
            mInstance = new NetworkService(account);
        }

        return mInstance;
    }

    public static NetworkService getInstance() {

        return mInstance;
    }

    private NetworkService(String account) {

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final DigestAuthenticator authenticator = new DigestAuthenticator(new Credentials(account, password));
        final Map<String, CachingAuthenticator> authCache = new ConcurrentHashMap<>();

        mRetrofitDidest = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                    .authenticator(new CachingAuthenticatorDecorator(authenticator, authCache))
                    .addInterceptor(new AuthenticationCacheInterceptor(authCache))
                    .build()
                )
                .build();
    }

    public RegistrationApi getRegistrationApi() {
        return mRetrofit.create(RegistrationApi.class);
    }

    public CustomerApi getCustomerApi() {
        return mRetrofitDidest.create(CustomerApi.class);
    }

    public TransportApi getTransportApi() {
        return mRetrofitDidest.create(TransportApi.class);
    }

    public OrderApi getOrderApi() {
        return mRetrofitDidest.create(OrderApi.class);
    }

    public ParkingApi getParkingApi() {
        return mRetrofitDidest.create(ParkingApi.class);
    }

    public TypeApi getTypeApi() {
        return mRetrofitDidest.create(TypeApi.class);
    }

    public ImageApi getImageApi() {
        return mRetrofitDidest.create(ImageApi.class);
    }

    public WorkTimeApi getWorkTimeApi() {
        return mRetrofitDidest.create(WorkTimeApi.class);
    }

    public CalendarApi getCalendarApi() {
        return mRetrofitDidest.create(CalendarApi.class);
    }
}
