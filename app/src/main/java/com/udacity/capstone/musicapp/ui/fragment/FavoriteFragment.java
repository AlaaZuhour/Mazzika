package com.udacity.capstone.musicapp.ui.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.capstone.musicapp.R;
import com.udacity.capstone.musicapp.data.DataManeger;
import com.udacity.capstone.musicapp.model.Song;
import com.udacity.capstone.musicapp.ui.MainActivity;
import com.udacity.capstone.musicapp.ui.SongSelectedListener;
import com.udacity.capstone.musicapp.ui.adapter.MusicAdapter;
import com.udacity.capstone.musicapp.utilities.FragmentState;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteFragment extends Fragment implements SongSelectedListener{

    @BindView(R.id.songs_list)
    RecyclerView songView;

    private int recyclerPostion;
    private ArrayList<Song> tracks;
    public MusicAdapter musicAdapter;
    private GridLayoutManager layoutManager;

    public FavoriteFragment() {
        // Required empty public constructor
    }
    public int getRecyclerPostion() {
        return recyclerPostion;
    }

    public void setRecyclerPostion(int recyclerPostion) {
        this.recyclerPostion = recyclerPostion;
        songView.getLayoutManager().scrollToPosition(recyclerPostion);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

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


        tracks = DataManeger.queryFavoriteSongs(getActivity());
        prepareAdapter();


        return rootView;
    }

    private void prepareAdapter() {
        musicAdapter = new MusicAdapter(FavoriteFragment.this);
        musicAdapter.setMusicList(tracks);
        songView.setAdapter(musicAdapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("songs", tracks);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (layoutManager != null)
            recyclerPostion = ((GridLayoutManager) songView.getLayoutManager())
                    .findLastVisibleItemPosition();


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
