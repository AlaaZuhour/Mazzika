package com.udacity.capstone.musicapp.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.udacity.capstone.musicapp.model.Playlist;
import com.udacity.capstone.musicapp.model.Song;

import java.util.ArrayList;


public class DataManeger {

    public static void addSongs(Context context,ContentValues[] values){

        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.delete(MusicContract.MusicEntry.CONTENT_URI,null,null);
        contentResolver.bulkInsert(MusicContract.MusicEntry.CONTENT_URI,values);
    }

    public static ArrayList<Song> querySongsWithPlayList(Context context,int playlistID ){
        ArrayList<Song> songs = new ArrayList<>();

        String[] selectionArg = {playlistID+""};
        Cursor cursor = context.getContentResolver().query(MusicContract.MusicEntry.CONTENT_URI,
                null, MusicContract.MusicEntry.COLUMN_PLAYLIST_ID + " = ? ", selectionArg, null);

        if (cursor != null || cursor.getCount() != 0) {

            if (cursor.moveToFirst()) {
                do {
                    Song song = new Song();
                    song.setId(cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_ID)));
                    song.setArtist(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_ARTIST)));
                    song.setTitle(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_TITLE)));
                    song.setStreamUrl(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_URL)));
                    song.setImageUrl(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_IMAGE_URL)));
                    song.setFavorit(cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_FAVORIT)) > 0);
                    song.setPlayListId(cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_PLAYLIST_ID)));
                    songs.add(song);

                } while (cursor.moveToNext());
            }
        }
        return songs;
    }
    public static ArrayList<Song> querySongs(Context context,String artistName){
        ArrayList<Song> songs = new ArrayList<>();

        String[] selectionArg = {artistName};
        Cursor cursor = context.getContentResolver().query(MusicContract.MusicEntry.CONTENT_URI,
                null, MusicContract.MusicEntry.COLUMN_SONG_ARTIST + " = ? ", selectionArg, null);

        if (cursor != null || cursor.getCount() != 0) {

            if (cursor.moveToFirst()) {
                do {
                    Song song = new Song();
                    song.setId(cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_ID)));
                    song.setArtist(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_ARTIST)));
                    song.setTitle(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_TITLE)));
                    song.setStreamUrl(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_URL)));
                    song.setImageUrl(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_IMAGE_URL)));
                    song.setFavorit(cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_FAVORIT)) > 0);
                    song.setPlayListId(cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_PLAYLIST_ID)));
                    songs.add(song);

                } while (cursor.moveToNext());
            }
        }

        return songs;
    }

    public static int queryPlaylistWithName(Context context, String name){
        String[] selectionArg = {name};
        int id = 0;
        Cursor cursor = context.getContentResolver().query(MusicContract.PlaylistEntry.CONTENT_URI,
                null, MusicContract.PlaylistEntry.COLUMN_PLAYLIST_NAME + " = ? ", selectionArg, null);

        if (cursor != null ||  cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    id = cursor.getInt(cursor.getColumnIndex(MusicContract.PlaylistEntry.COLUMN_PLAYLIST_ID));
                } while (cursor.moveToNext());
            }
            return id;
        }

        return -1;
    }

    public static  ArrayList<Song> querySongs(Context context){
        ArrayList<Song> songs = new ArrayList<>();
        String[] selectionArg = {"Mesut Kurtis","Maher Zain"};
        Cursor cursor = context.getContentResolver().query(MusicContract.MusicEntry.CONTENT_URI,
                null,null,null,null);

        if (cursor != null || cursor.getCount() != 0) {

            if (cursor.moveToFirst()) {
                do {
                    Song song = new Song();
                    song.setId(cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_ID)));
                    song.setArtist(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_ARTIST)));
                    song.setTitle(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_TITLE)));
                    song.setStreamUrl(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_URL)));
                    song.setImageUrl(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_IMAGE_URL)));
                    song.setFavorit(cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_FAVORIT)) > 0);
                    song.setPlayListId(cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_PLAYLIST_ID)));
                    songs.add(song);

                } while (cursor.moveToNext());
            }
        }

        return  songs;
    }

    public static void addPlaylists(Context context,ContentValues[] values){
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.delete(MusicContract.PlaylistEntry.CONTENT_URI,null,null);
        contentResolver.bulkInsert(MusicContract.PlaylistEntry.CONTENT_URI,values);
    }

    public static ArrayList<Playlist> queryPlaylist(Context context){
        Cursor cursor = context.getContentResolver().query(MusicContract.PlaylistEntry.CONTENT_URI,
                null,null,null,null);

        ArrayList<Playlist> playlists =new ArrayList<>();

        if (cursor != null || cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    Playlist playlist =new Playlist();
                    playlist.setId(cursor.getInt(cursor.getColumnIndex(MusicContract.PlaylistEntry.COLUMN_PLAYLIST_ID)));
                    playlist.setName(cursor.getString(cursor.getColumnIndex(MusicContract.PlaylistEntry.COLUMN_PLAYLIST_NAME)));
                    playlists.add(playlist);
                } while (cursor.moveToNext());
            }
        }

        return  playlists;
    }

    public static void updateSongsFavorite(Context context,Song song,int favorite){

        ContentValues contentValues1 = new ContentValues();
        String[] selections = {"" + song.getId()};
        contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_ARTIST,song.getArtist());
        contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_TITLE,song.getTitle());
        contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_URL,song.getStreamUrl());
        contentValues1.put(MusicContract.MusicEntry.COLUMN_IMAGE_URL,song.getImageUrl());
        contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_ID,song.getId());
        contentValues1.put(MusicContract.MusicEntry.COLUMN_PLAYLIST_ID,song.getPlayListId());
        contentValues1.put(MusicContract.MusicEntry.COLUMN_FAVORIT,favorite);
        int row = context.getContentResolver().update(MusicContract.MusicEntry.CONTENT_URI, contentValues1,
                MusicContract.MusicEntry.COLUMN_SONG_ID + " = ? ", selections);
    }
    public static void updateSongsPlayList(Context context,Song song,int playlistID ){

        ContentValues contentValues1 = new ContentValues();
        String[] selections = {"" + song.getId()};
        contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_ARTIST,song.getArtist());
        contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_TITLE,song.getTitle());
        contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_URL,song.getStreamUrl());
        contentValues1.put(MusicContract.MusicEntry.COLUMN_IMAGE_URL,song.getImageUrl());
        contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_ID,song.getId());
        contentValues1.put(MusicContract.MusicEntry.COLUMN_PLAYLIST_ID,playlistID);
        contentValues1.put(MusicContract.MusicEntry.COLUMN_FAVORIT, song.isFavorit()?1:0);
        int row = context.getContentResolver().update(MusicContract.MusicEntry.CONTENT_URI, contentValues1,
                MusicContract.MusicEntry.COLUMN_SONG_ID + " = ? ", selections);
    }


    public static ArrayList<Song> queryFavoriteSongs(Context context){
        ArrayList<Song> songs = new ArrayList<>();

        String[] selectionArg = {"1"};
        Cursor cursor = context.getContentResolver().query(MusicContract.MusicEntry.CONTENT_URI,
                null, MusicContract.MusicEntry.COLUMN_FAVORIT + " = ? ", selectionArg, null);

        if (cursor != null || cursor.getCount() != 0) {

            if (cursor.moveToFirst()) {
                do {
                    Song song = new Song();
                    song.setId(cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_ID)));
                    song.setArtist(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_ARTIST)));
                    song.setTitle(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_TITLE)));
                    song.setStreamUrl(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_URL)));
                    song.setImageUrl(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_IMAGE_URL)));
                    song.setFavorit(cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_FAVORIT)) > 0);
                    song.setPlayListId(cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_PLAYLIST_ID)));
                    songs.add(song);

                } while (cursor.moveToNext());
            }
        }
        return songs;
    }
}
