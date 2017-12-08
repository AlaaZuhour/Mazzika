package com.udacity.capstone.musicapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class MusicProvider extends ContentProvider {

    static final int MUSIC = 100;
    static final int MUSIC_WITH_ID = 101;
    private MusicDbHelper musicDbHelper;
    public static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uri = new UriMatcher(UriMatcher.NO_MATCH);
        uri.addURI(MusicContract.CONTENT_AUTHORITY,MusicContract.PATH_MUSIC,MUSIC);
        uri.addURI(MusicContract.CONTENT_AUTHORITY,MusicContract.PATH_MUSIC+"/#",MUSIC_WITH_ID);

        return uri;
    }

    @Override
    public boolean onCreate() {
        musicDbHelper = new MusicDbHelper(getContext());
        return true;
    }
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase sqLiteDatabase = musicDbHelper.getWritableDatabase();
        int rowInserted = 0;
        switch (sUriMatcher.match(uri)) {
            case MUSIC:
                sqLiteDatabase.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long id = sqLiteDatabase.insert(MusicContract.MusicEntry.TABLE_NAME, null, value);
                        if (id != -1)
                            rowInserted++;
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } finally {
                    sqLiteDatabase.endTransaction();
                }
                if (rowInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowInserted;
            default:
                return super.bulkInsert(uri, values);
        }

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase sqLiteDatabase = musicDbHelper.getReadableDatabase();
        Cursor retCursor = null;
        switch (sUriMatcher.match(uri)) {
            case MUSIC:
                retCursor = sqLiteDatabase.query(MusicContract.MusicEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MUSIC_WITH_ID:
                String id = uri.getLastPathSegment();
                String[] selectionArg = {id};
                retCursor = sqLiteDatabase.query(MusicContract.MusicEntry.TABLE_NAME,
                        projection, MusicContract.MusicEntry._ID + " = ? ", selectionArg,
                        null, null, sortOrder);
                break;
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = musicDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection) selection = "1";

        switch (match) {
            case MUSIC: {
                rowsDeleted = db.delete(
                        MusicContract.MusicEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = musicDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MUSIC:
                rowsUpdated = db.update(MusicContract.MusicEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
