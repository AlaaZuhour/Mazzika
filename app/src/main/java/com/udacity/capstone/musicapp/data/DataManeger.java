package com.udacity.capstone.musicapp.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DataManeger {

    public static void addSongs(Context context,ContentValues[] values){

        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.delete(MusicContract.MusicEntry.CONTENT_URI,null,null);
        contentResolver.bulkInsert(MusicContract.MusicEntry.CONTENT_URI,values);
    }

    public static Cursor querySongs(Context context,String id){

        String[] selectionArg = {id};
        return context.getContentResolver().query(MusicContract.MusicEntry.CONTENT_URI,
                null,MusicContract.MusicEntry._ID+" = ? ",selectionArg,null);
    }

    public static Cursor querySongs(Context context){

        return  context.getContentResolver().query(MusicContract.MusicEntry.CONTENT_URI,
                null,null,null,null);
    }
}
