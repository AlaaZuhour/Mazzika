package com.udacity.capstone.musicapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class DataManeger {
    private static MusicDbHelper mMusicDbHelper;
    private static SQLiteDatabase mSQLiteDatabase;
    public static void addSongs(Context context,ContentValues[] values){
        mMusicDbHelper = new MusicDbHelper(context);
        mSQLiteDatabase = mMusicDbHelper.getWritableDatabase();
        mSQLiteDatabase.beginTransaction();
        try {
            for (int i=0; i<values.length-2;i++) {
                mSQLiteDatabase.insert(MusicContract.MusicEntry.TABLE_NAME, null, values[i]);
            }
            mSQLiteDatabase.setTransactionSuccessful();
        }finally {
            mSQLiteDatabase.endTransaction();
        }
    }

    public static Cursor querySongs(Context context,String id){
        mMusicDbHelper = new MusicDbHelper(context);
        mSQLiteDatabase = mMusicDbHelper.getReadableDatabase();
        String[] selectionArg = {id};
        return  mSQLiteDatabase.query(MusicContract.MusicEntry.TABLE_NAME,
                null, MusicContract.MusicEntry._ID+" = ? ",selectionArg,
                null,null,null);
    }

    public static Cursor querySongs(Context context){
        mMusicDbHelper = new MusicDbHelper(context);
        mSQLiteDatabase = mMusicDbHelper.getReadableDatabase();
        return  mSQLiteDatabase.query(MusicContract.MusicEntry.TABLE_NAME,
                null, null,null,null,null,null);
    }
}
