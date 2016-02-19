package com.youtube.sorcjc.proyectoprofesionales.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.youtube.sorcjc.proyectoprofesionales.io.deserializers.LoginDeserializer;
import com.youtube.sorcjc.proyectoprofesionales.io.responses.LoginResponse;


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
                    .baseUrl("http://dev.homesolution.com.ar/api/")
                    .addConverterFactory(buildGsonConverter())
                    .client(httpClient) // <-- using the log level
                    .build();
            API_SERVICE = retrofit.create(HomeSolutionApiService.class);
        }

        return API_SERVICE;
    }

    private static GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Adding custom deserializers
        gsonBuilder.registerTypeAdapter(LoginResponse.class, new LoginDeserializer());
        Gson myGson = gsonBuilder.create();

        return GsonConverterFactory.create(myGson);
    }

}
