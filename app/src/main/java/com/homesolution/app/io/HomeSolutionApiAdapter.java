package com.homesolution.app.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.homesolution.app.io.deserializer.ChatDeserializer;
import com.homesolution.app.io.deserializer.LoginDeserializer;
import com.homesolution.app.io.response.ChatResponse;
import com.homesolution.app.io.response.LoginResponse;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class HomeSolutionApiAdapter {

    private static HomeSolutionApiService API_SERVICE;

    public static HomeSolutionApiService getApiService(String country) {

        // Creating the interceptor, and setting the log level
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Adding the interceptor to a client
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.interceptors().add(logging);

        // Define the base url by country
        String baseUrl = getUrlByCountry(country) + "api/";

        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(buildGsonConverter())
                    .client(httpClient) // <-- using the log level
                    .build();
            API_SERVICE = retrofit.create(HomeSolutionApiService.class);
        }

        return API_SERVICE;
    }

    private static String getUrlByCountry(String country) {
        switch (country) {
            case "cl":
                return "http://cl.homesolution.net/";
            case "uy":
                return "http://uy.homesolution.net/";
            case "ec":
                return "http://ec.homesolution.net/";
            case "co":
                return "http://co.homesolution.net/";
            case "pe":
                return "http://pe.homesolution.net/";
            default: // ar
                return "http://homesolution.com.ar/";
        }
    }

    private static GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Adding custom deserializers
        gsonBuilder.registerTypeAdapter(LoginResponse.class, new LoginDeserializer());
        gsonBuilder.registerTypeAdapter(ChatResponse.class, new ChatDeserializer());
        Gson myGson = gsonBuilder.create();

        return GsonConverterFactory.create(myGson);
    }

}
