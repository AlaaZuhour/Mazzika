package com.udacity.capstone.musicapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thumbnail implements Parcelable{

    @SerializedName("default")
    @Expose
    private Default _default;


    protected Thumbnail(Parcel in) {
        _default = in.readParcelable(Default.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(_default, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Thumbnail> CREATOR = new Creator<Thumbnail>() {
        @Override
        public Thumbnail createFromParcel(Parcel in) {
            return new Thumbnail(in);
        }

        @Override
        public Thumbnail[] newArray(int size) {
            return new Thumbnail[size];
        }
    };

    public Default getDefault() {
        return _default;
    }
}