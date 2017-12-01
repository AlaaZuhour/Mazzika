package com.udacity.capstone.musicapp.ui.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.capstone.musicapp.R;
import com.udacity.capstone.musicapp.data.DataManeger;
import com.udacity.capstone.musicapp.data.MusicContract;
import com.udacity.capstone.musicapp.model.Song;
import com.udacity.capstone.musicapp.sync.MusicSyncTask;
import com.udacity.capstone.musicapp.ui.MainActivity;
import com.udacity.capstone.musicapp.ui.adapter.MusicAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements MusicSyncTask.TaskCompletedListener{

    private OnFragmentInteractionListener mListener;

    private RecyclerView songView;

    private ArrayList<Song> tracks;
    public MusicAdapter musicAdapter;
    private GridLayoutManager layoutManager;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        songView = rootView.findViewById(R.id.songs_list);
        int ot = getResources().getConfiguration().orientation;
        layoutManager =
                new GridLayoutManager(getActivity(),ot == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2);
        songView.setLayoutManager(layoutManager);
        songView.setHasFixedSize(true);


        MusicSyncTask.initialize(getActivity());
        new MusicAysncTask().execute();

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class MusicAysncTask extends AsyncTask<Void,Void,Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {

            return DataManeger.querySongs(getActivity());
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null || cursor.getCount() != 0) {
                ArrayList<Song> songs = new ArrayList<>();
                if (cursor.moveToFirst()) {
                    do {
                        Song song = new Song();
                        song.setId(cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_PRIORITY)));
                        song.setArtist(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_ARTIST)));
                        song.setTitle(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_TITLE)));
                        song.setStreamUrl(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_SONG_URL)));
                        song.setImageUrl(cursor.getString(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_IMAGE_URL)));
                        song.setFavorit(cursor.getInt(cursor.getColumnIndex(MusicContract.MusicEntry.COLUMN_FAVORIT)) > 0);
                        songs.add(song);

                    } while (cursor.moveToNext());
                }
                tracks = songs;
                musicAdapter = new MusicAdapter(getActivity(), tracks);
                songView.setAdapter(musicAdapter);


            }
        }
    }

    @Override
    public void onDownloadFinished() {
        new MusicAysncTask().execute();
    }

}
