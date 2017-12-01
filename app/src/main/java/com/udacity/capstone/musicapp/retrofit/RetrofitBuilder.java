package com.udacity.capstone.musicapp.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitBuilder {
    private static ITunes iTunes;
    private static ITube iTube;
    public static ITunes Retrieve() {

        Gson gson = new GsonBuilder().create();

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();


        iTunes = new Retrofit.Builder()
                .baseUrl("https://itunes.apple.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClientBuilder.build())
                .build().create(ITunes.class);


        return iTunes;
    }

    public static ITube RetrieveITube(){
        Gson gson = new GsonBuilder().create();

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();


        iTube = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClientBuilder.build())
                .build().create(ITube.class);


        return iTube;
    }
}
