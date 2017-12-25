package com.udacity.capstone.musicapp.ui.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.support.annotation.Nullable;
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
import com.udacity.capstone.musicapp.ui.SongSelectedListener;
import com.udacity.capstone.musicapp.ui.adapter.MusicAdapter;
import com.udacity.capstone.musicapp.utilities.FragmentState;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

public class HomeFragment extends Fragment implements MusicSyncTask.TaskCompletedListener,
        SongSelectedListener {


    @BindView(R.id.songs_list)
    RecyclerView songView;

    private int recyclerPostion;
    private ArrayList<Song> tracks;
    public MusicAdapter musicAdapter;
    private GridLayoutManager layoutManager;

    public HomeFragment() {
    }

    public ArrayList<Song> getTracks() {
        return tracks;
    }

    public void setTracks(ArrayList<Song> tracks) {
        this.tracks = tracks;
    }

    public int getRecyclerPostion() {
        return recyclerPostion;
    }

    public void setRecyclerPostion(int recyclerPostion) {
        this.recyclerPostion = recyclerPostion;
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

        ButterKnife.bind(this, rootView);

        int ot = getResources().getConfiguration().orientation;
        layoutManager =
                new GridLayoutManager(getActivity(), ot == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2);
        songView.setLayoutManager(layoutManager);
        songView.setHasFixedSize(true);
        layoutManager.scrollToPosition(recyclerPostion);

        if (savedInstanceState != null) {
            tracks = savedInstanceState.getParcelableArrayList("songs");
            prepareAdapter();
        }

        if (getArguments() != null) {
            tracks = getArguments().getParcelableArrayList("songs");
            prepareAdapter();
            getArguments().clear();
        }
        MusicSyncTask.setListener(this);
        MusicSyncTask.initialize(getActivity());
        if (tracks == null || tracks.size() == 0) {
            new MusicAysncTask().execute();

        }else {
            prepareAdapter();
        }

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("songs", tracks);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSongSelectedListener(int postion) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("songs", tracks);
        args.putInt("position", postion);
        PlayFragment playFragment = new PlayFragment();
        playFragment.setArguments(args);
        ((MainActivity) getActivity()).mFragmentState = FragmentState.Play;
        ((MainActivity) getActivity()).playFragment = playFragment;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, playFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (layoutManager != null)
            recyclerPostion = ((GridLayoutManager) songView.getLayoutManager())
                    .findLastVisibleItemPosition();


    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private class MusicAysncTask extends AsyncTask<Void, Void, ArrayList<Song>> {

        @Override
        protected ArrayList<Song> doInBackground(Void... voids) {

            return DataManeger.querySongs(getActivity());
        }

        @Override
        protected void onPostExecute(ArrayList<Song> songs) {

            tracks = songs;
            prepareAdapter();

        }
    }

    @Override
    public void onDownloadFinished(String art) {
        new MusicAysncTask().execute();
    }

    public void prepareAdapter() {
        musicAdapter = new MusicAdapter(HomeFragment.this);
        musicAdapter.setMusicList(tracks);
        songView.setAdapter(musicAdapter);
    }
}
