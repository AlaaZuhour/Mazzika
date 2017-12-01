package com.udacity.capstone.musicapp.retrofit;


import com.udacity.capstone.musicapp.model.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ITube {

    @GET("v3/search?key=AIzaSyBJr1wF9qw70RxyqA0MCBcuxtu3-GxF_LE&type=video&part=snippet")
    Call<SearchResponse> getVideosList(@Query("q") String word);
}
