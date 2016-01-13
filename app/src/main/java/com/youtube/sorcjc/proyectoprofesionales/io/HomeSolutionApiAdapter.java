package com.youtube.sorcjc.proyectoprofesionales.io;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class HomeSolutionApiAdapter {

    private static HomeSolutionApiService API_SERVICE;

    public static HomeSolutionApiService getApiService() {

        // Creating the interceptor, and setting the log level
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Adding the interceptor to a client
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.interceptors().add(logging);

        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://homesolution.com.ar/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient) // <-- using the log level
                    .build();
            API_SERVICE = retrofit.create(HomeSolutionApiService.class);
        }

        return API_SERVICE;
    }

}
