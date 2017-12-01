package com.udacity.capstone.musicapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class SongResponse {
    @SerializedName("results")
    @Expose
    public ArrayList<Song> songArrayList;

    public SongResponse(){
        songArrayList = new ArrayList<>();
    }
}
