package com.udacity.capstone.musicapp.data;

import android.net.Uri;
import android.provider.BaseColumns;



public class MusicContract {
    public static final String CONTENT_AUTHORITY="com.udacity.capstone.musicapp";
    public static final Uri CONTENT_BASE_URL = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MUSIC = "music";
    public static final String PATH_PLAYLIST = "playlist";
    public static final class MusicEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                CONTENT_BASE_URL.buildUpon().appendPath(PATH_MUSIC).build();

        public static final String TABLE_NAME = "songs";
        public static final String COLUMN_SONG_ID = "id";
        public static final String COLUMN_SONG_TITLE = "title";
        public static final String COLUMN_SONG_ARTIST = "artist";
        public static final String COLUMN_SONG_URL = "url";
        public static final String COLUMN_IMAGE_URL = "image";
        public static final String COLUMN_FAVORIT = "favorit";
        public static final String COLUMN_PLAYLIST_ID = "playlist";
    }

    public static final class PlaylistEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                CONTENT_BASE_URL.buildUpon().appendPath(PATH_PLAYLIST).build();

        public static final String TABLE_NAME = "playlist";
        public static final String COLUMN_PLAYLIST_ID = "id";
        public static final String COLUMN_PLAYLIST_NAME = "name";
    }
}
