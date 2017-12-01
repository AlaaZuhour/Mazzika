package com.udacity.capstone.musicapp.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

    @SerializedName("items")
    @Expose
    private List<Item> items = null;

    public List<Item> getItems() {
        return items;
    }
}
