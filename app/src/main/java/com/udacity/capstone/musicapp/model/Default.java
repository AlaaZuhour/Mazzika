package com.udacity.capstone.musicapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Default implements Parcelable{

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("height")
    @Expose
    private Integer height;

    protected Default(Parcel in) {
        url = in.readString();
        if (in.readByte() == 0) {
            width = null;
        } else {
            width = in.readInt();
        }
        if (in.readByte() == 0) {
            height = null;
        } else {
            height = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        if (width == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(width);
        }
        if (height == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(height);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Default> CREATOR = new Creator<Default>() {
        @Override
        public Default createFromParcel(Parcel in) {
            return new Default(in);
        }

        @Override
        public Default[] newArray(int size) {
            return new Default[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

}