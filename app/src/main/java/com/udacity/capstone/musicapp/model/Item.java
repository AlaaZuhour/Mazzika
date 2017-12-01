package com.udacity.capstone.musicapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item implements Parcelable{

    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("etag")
    @Expose
    private String etag;
    @SerializedName("id")
    @Expose
    private Videos videos;
    @SerializedName("snippet")
    @Expose
    private Snippet snippet;

    protected Item(Parcel in) {
        kind = in.readString();
        etag = in.readString();
        videos = in.readParcelable(Videos.class.getClassLoader());
        snippet = in.readParcelable(Snippet.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(kind);
        dest.writeString(etag);
        dest.writeParcelable(videos, flags);
        dest.writeParcelable(snippet, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public Videos getId() {
        return videos;
    }

    public void setId(Videos videos) {
        this.videos = videos;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

}