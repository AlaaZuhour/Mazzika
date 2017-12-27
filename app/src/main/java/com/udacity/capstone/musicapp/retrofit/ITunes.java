package com.udacity.capstone.musicapp.retrofit;


import com.udacity.capstone.musicapp.model.SongResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ITunes {

    @GET("lookup?entity=song")
    Call<SongResponse> getArtistSongs(@Query("id") long artistId);

    @GET("search?term=all&entity=song")
    Call<SongResponse> getSongs();
}
