package com.udacity.capstone.musicapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MusicDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_NAME = "music.db";

    public MusicDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MUSIC_TABLE = "CREATE TABLE " + MusicContract.MusicEntry.TABLE_NAME + " (" +
                MusicContract.MusicEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MusicContract.MusicEntry.COLUMN_SONG_ARTIST + " TEXT NOT NULL, " +
                MusicContract.MusicEntry.COLUMN_SONG_TITLE + " TEXT NOT NULL, " +
                MusicContract.MusicEntry.COLUMN_SONG_URL + " TEXT NOT NULL, " +
                MusicContract.MusicEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL, " +
                MusicContract.MusicEntry.COLUMN_SONG_ID+ " INTEGER NOT NULL, " +
                MusicContract.MusicEntry.COLUMN_PLAYLIST_ID+ " INTEGER NOT NULL, " +
                MusicContract.MusicEntry.COLUMN_FAVORIT+" INTEGER NOT NULL);";

        final String SQL_CREATE_PLAYLIST_TABLE = "CREATE TABLE " + MusicContract.PlaylistEntry.TABLE_NAME + " (" +
                MusicContract.PlaylistEntry.COLUMN_PLAYLIST_NAME + " TEXT NOT NULL, " +
                MusicContract.PlaylistEntry.COLUMN_PLAYLIST_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT);" ;

        sqLiteDatabase.execSQL(SQL_CREATE_MUSIC_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PLAYLIST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MusicContract.MusicEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MusicContract.PlaylistEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
