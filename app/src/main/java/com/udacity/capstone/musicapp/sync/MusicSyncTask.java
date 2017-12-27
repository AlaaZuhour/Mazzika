package com.udacity.capstone.musicapp.sync;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.udacity.capstone.musicapp.R;
import com.udacity.capstone.musicapp.data.DataManeger;
import com.udacity.capstone.musicapp.data.MusicContract;
import com.udacity.capstone.musicapp.model.Song;
import com.udacity.capstone.musicapp.model.SongResponse;
import com.udacity.capstone.musicapp.retrofit.ITunes;
import com.udacity.capstone.musicapp.retrofit.RetrofitBuilder;
import com.udacity.capstone.musicapp.ui.MainActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class MusicSyncTask {
    private static boolean sInitialized;
    private static SongResponse songResponse;
    private static TaskCompletedListener mListener;
    private static Context mContext;
    public static void initialize(@NonNull Context context) {
        if (sInitialized) return;

        mContext = context;
        sInitialized = true;
        ArrayList<Song> songs = DataManeger.querySongs(context);

        if(songs == null || songs.size() == 0){
            syncMusic(context);
        }

    }

    public static void setListener(Fragment fragment){
        mListener = (TaskCompletedListener) fragment;
    }



    public static void syncMusic(final Context context){
        mContext = context;
        if(!isDeviceOnline()){
            View view =  ((MainActivity)context).findViewById(android.R.id.content).getRootView();
            final Snackbar snackbar = Snackbar.make(view, context.getString(R.string.no_internet), Snackbar.LENGTH_LONG);

            snackbar.show();
            return;
        }
        ITunes songService = RetrofitBuilder.Retrieve();
        Call<SongResponse> songList = songService.getSongs();
        songResponse = new SongResponse();
        songList.enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(@NonNull Call<SongResponse> call, @NonNull Response<SongResponse> response) {
                songResponse = response.body();
                ContentValues[] contentValues = new ContentValues[songResponse.songArrayList.size()+1];

                for(int i=1;i<contentValues.length-1;){
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_ARTIST,songResponse.songArrayList.get(i).getArtist());
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_TITLE,songResponse.songArrayList.get(i).getTitle());
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_URL,songResponse.songArrayList.get(i).getStreamUrl());
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_IMAGE_URL,songResponse.songArrayList.get(i).getImageUrl());
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_ID,songResponse.songArrayList.get(i).getId());
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_PLAYLIST_ID,-1);
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_FAVORIT,0);
                    contentValues[i-1] = contentValues1;
                    i++;

                }
                DataManeger.addSongs(context,contentValues);
                mListener.onDownloadFinished(context.getString(R.string.mix));
            }

            @Override
            public void onFailure(@NonNull Call<SongResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void getMaherZainSongs(Context context) {
        mContext = context;
        if(!isDeviceOnline()){
            View view =  ((MainActivity)context).findViewById(android.R.id.content).getRootView();
            final Snackbar snackbar = Snackbar.make(view, context.getString(R.string.no_internet), Snackbar.LENGTH_LONG);

            snackbar.show();
            return;
        }
        ITunes songService = RetrofitBuilder.Retrieve();
        Call<SongResponse> songList = songService.getArtistSongs(335438840);
        songList.enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(@NonNull Call<SongResponse> call, @NonNull Response<SongResponse> response) {
                songResponse = response.body();
                ContentValues[] contentValues = new ContentValues[songResponse.songArrayList.size()+1];

                for(int i=1;i<contentValues.length-1;){
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_ARTIST,songResponse.songArrayList.get(i).getArtist());
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_TITLE,songResponse.songArrayList.get(i).getTitle());
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_URL,songResponse.songArrayList.get(i).getStreamUrl());
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_IMAGE_URL,songResponse.songArrayList.get(i).getImageUrl());
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_ID,songResponse.songArrayList.get(i).getId());
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_PLAYLIST_ID,-1);
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_FAVORIT,0);
                    contentValues[i-1] = contentValues1;
                    i++;

                }
                DataManeger.addSongs(context,contentValues);
                mListener.onDownloadFinished(context.getString(R.string.maher));
            }

            @Override
            public void onFailure(@NonNull Call<SongResponse> call, @NonNull Throwable t) {
            }
        });


    }

    public static void getMesutKurtisSongs(Context context) {
        mContext = context;
        if(!isDeviceOnline()){
            View view =  ((MainActivity)context).findViewById(android.R.id.content).getRootView();
            final Snackbar snackbar = Snackbar.make(view,context.getString(R.string.no_internet), Snackbar.LENGTH_LONG);

            snackbar.show();
            return;
        }
        ITunes songService = RetrofitBuilder.Retrieve();
        Call<SongResponse> songList = songService.getArtistSongs(74457352);
        songList.enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(@NonNull Call<SongResponse> call, @NonNull Response<SongResponse> response) {
                songResponse = response.body();
                ContentValues[] contentValues = new ContentValues[songResponse.songArrayList.size()+1];

                for(int i=1;i<contentValues.length-1;){
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_ARTIST,songResponse.songArrayList.get(i).getArtist());
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_TITLE,songResponse.songArrayList.get(i).getTitle());
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_URL,songResponse.songArrayList.get(i).getStreamUrl());
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_IMAGE_URL,songResponse.songArrayList.get(i).getImageUrl());
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_ID,songResponse.songArrayList.get(i).getId());
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_PLAYLIST_ID,-1);
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_FAVORIT,0);
                    contentValues[i-1] = contentValues1;
                    i++;

                }
                DataManeger.addSongs(context,contentValues);
                mListener.onDownloadFinished(context.getString(R.string.mesut));
            }

            @Override
            public void onFailure(@NonNull Call<SongResponse> call, @NonNull Throwable t) {

            }
        });



    }

    public interface TaskCompletedListener{
        void onDownloadFinished(String artiste);
    }

    private static boolean isDeviceOnline(){
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null || ni.isConnected();
    }
}
