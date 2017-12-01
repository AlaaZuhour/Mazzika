package com.udacity.capstone.musicapp.sync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.udacity.capstone.musicapp.data.DataManeger;
import com.udacity.capstone.musicapp.data.MusicContract;
import com.udacity.capstone.musicapp.model.SongResponse;
import com.udacity.capstone.musicapp.retrofit.ITunes;
import com.udacity.capstone.musicapp.retrofit.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicSyncTask {
    private static boolean sInitialized;
    private static SongResponse songResponse;
    private static TaskCompletedListener mListener;
    public static void initialize(@NonNull Context context) {
        if (sInitialized) return;

        sInitialized = true;
        Cursor cursor = DataManeger.querySongs(context);

        if(cursor == null || cursor.getCount() == 0){
            syncMusic(context);
        }

    }



    private static void syncMusic(final Context context){
        ITunes songService = RetrofitBuilder.Retrieve();
        Call<SongResponse> songList = songService.getSongs();
        songResponse = new SongResponse();
        mListener = (TaskCompletedListener) context;
        songList.enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(@NonNull Call<SongResponse> call, @NonNull Response<SongResponse> response) {
                songResponse = response.body();
                Log.d("response","sucsses");
                ContentValues[] contentValues = new ContentValues[songResponse.songArrayList.size()+1];

                for(int i=1;i<contentValues.length-1;){
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_ARTIST,songResponse.songArrayList.get(i).getArtist());
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_TITLE,songResponse.songArrayList.get(i).getTitle());
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_URL,songResponse.songArrayList.get(i).getStreamUrl());
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_IMAGE_URL,songResponse.songArrayList.get(i).getImageUrl());
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_SONG_PRIORITY,0);
                    contentValues1.put(MusicContract.MusicEntry.COLUMN_FAVORIT,0);
                    contentValues[i-1] = contentValues1;
                    i++;

                }
                DataManeger.addSongs(context,contentValues);
                mListener.onDownloadFinished();
            }

            @Override
            public void onFailure(@NonNull Call<SongResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public interface TaskCompletedListener{
        void onDownloadFinished();
    }
}
