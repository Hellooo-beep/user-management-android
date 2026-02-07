package com.example.usermanagementapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;

    private static final String BASE_URL = "http://10.0.2.2:8080/";

    // This method creates Retrofit instance (or returns existing one)
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // This method gets the API service
    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }
}