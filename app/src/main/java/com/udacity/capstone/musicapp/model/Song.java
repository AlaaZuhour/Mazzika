package com.udacity.capstone.musicapp.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class Song implements Parcelable{
    private int id;

    private int priority ;

    @SerializedName("trackName")
    @Expose
    private String title;

    @SerializedName("artistName")
    @Expose
    private String artist;

    @SerializedName("previewUrl")
    @Expose
    private String streamUrl;

    @SerializedName("artworkUrl100")
    @Expose
    private String imageUrl;

    private boolean isFavorit;
    public Song(){

    }

    protected Song(Parcel in) {
        id = in.readInt();
        priority = in.readInt();
        title = in.readString();
        artist = in.readString();
        streamUrl = in.readString();
        imageUrl = in.readString();
        isFavorit = in.readByte() != 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isFavorit() {
        return isFavorit;
    }

    public void setFavorit(boolean favorit) {
        isFavorit = favorit;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(priority);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(streamUrl);
        dest.writeString(imageUrl);
        dest.writeByte((byte) (isFavorit ? 1 : 0));
    }
}
