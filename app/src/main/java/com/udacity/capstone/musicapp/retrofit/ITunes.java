package com.udacity.capstone.musicapp.retrofit;


import com.udacity.capstone.musicapp.model.SongResponse;

import retrofit2.Call;
import retrofit2.http.GET;


public interface ITunes {

    @GET("lookup?id=335438840&entity=song")
    Call<SongResponse> getSongs();
}
