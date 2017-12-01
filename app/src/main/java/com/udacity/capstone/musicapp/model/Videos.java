package com.udacity.capstone.musicapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Videos implements Parcelable{

    @SerializedName("kind")
    @Expose
    private String kind;

    @SerializedName("videoId")
    @Expose
    private String videoId;

    protected Videos(Parcel in) {
        kind = in.readString();
        videoId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(kind);
        dest.writeString(videoId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Videos> CREATOR = new Creator<Videos>() {
        @Override
        public Videos createFromParcel(Parcel in) {
            return new Videos(in);
        }

        @Override
        public Videos[] newArray(int size) {
            return new Videos[size];
        }
    };

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

}
