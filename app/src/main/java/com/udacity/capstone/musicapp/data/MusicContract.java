package com.udacity.capstone.musicapp.data;

import android.provider.BaseColumns;



public class MusicContract {

    public static final class MusicEntry implements BaseColumns{
        public static final String TABLE_NAME = "songs";
        public static final String COLUMN_SONG_PRIORITY = "priority";
        public static final String COLUMN_SONG_TITLE = "title";
        public static final String COLUMN_SONG_ARTIST = "artist";
        public static final String COLUMN_SONG_URL = "url";
        public static final String COLUMN_IMAGE_URL = "image";
        public static final String COLUMN_FAVORIT = "favorit";
    }

    public static final class CustomerEntry implements BaseColumns{

    }
}
