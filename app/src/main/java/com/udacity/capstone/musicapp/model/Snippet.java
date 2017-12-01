package com.udacity.capstone.musicapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Snippet implements Parcelable{

    @SerializedName("publishedAt")
    @Expose
    private String publishedAt;
    @SerializedName("channelId")
    @Expose
    private String channelId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("thumbnails")
    @Expose
    private Thumbnail thumbnails;
    @SerializedName("channelTitle")
    @Expose
    private String channelTitle;
    @SerializedName("liveBroadcastContent")
    @Expose
    private String liveBroadcastContent;

    protected Snippet(Parcel in) {
        publishedAt = in.readString();
        channelId = in.readString();
        title = in.readString();
        description = in.readString();
        thumbnails = in.readParcelable(Thumbnail.class.getClassLoader());
        channelTitle = in.readString();
        liveBroadcastContent = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(publishedAt);
        dest.writeString(channelId);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeParcelable(thumbnails, flags);
        dest.writeString(channelTitle);
        dest.writeString(liveBroadcastContent);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Snippet> CREATOR = new Creator<Snippet>() {
        @Override
        public Snippet createFromParcel(Parcel in) {
            return new Snippet(in);
        }

        @Override
        public Snippet[] newArray(int size) {
            return new Snippet[size];
        }
    };

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Thumbnail getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Thumbnail thumbnail) {
        this.thumbnails = thumbnail;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getLiveBroadcastContent() {
        return liveBroadcastContent;
    }

    public void setLiveBroadcastContent(String liveBroadcastContent) {
        this.liveBroadcastContent = liveBroadcastContent;
    }

}