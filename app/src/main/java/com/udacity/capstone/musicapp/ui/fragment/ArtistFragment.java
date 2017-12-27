package com.udacity.capstone.musicapp.ui.fragment;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.capstone.musicapp.R;
import com.udacity.capstone.musicapp.data.DataManeger;
import com.udacity.capstone.musicapp.model.Song;
import com.udacity.capstone.musicapp.model.SongResponse;
import com.udacity.capstone.musicapp.sync.MusicSyncTask;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class ArtistFragment extends Fragment implements MusicSyncTask.TaskCompletedListener{

    @BindViews({R.id.maher_view, R.id.mesut_view, R.id.mazzika_view})
    List<CardView> cardViewList;

    ArrayList<Song> songs;

    public ArtistFragment() {
        // Required empty public constructor
        MusicSyncTask.setListener(this);
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_artist, container, false);
        ButterKnife.bind(this, rootView);
        if (savedInstanceState != null) {
            songs = savedInstanceState.getParcelableArrayList("songs");
        }
        return rootView;
    }

    @OnClick({R.id.maher_view, R.id.mesut_view, R.id.mazzika_view})
    public void onCardViewClicked(CardView cardView) {

        switch (cardView.getId()) {
            case R.id.maher_view:
                songs = DataManeger.querySongs(getActivity(), "Maher Zain");
                if (songs == null || songs.size() == 0) {
                    MusicSyncTask.getMaherZainSongs(getActivity());
                }else{
                    openHomeFragment();
                }
                break;

            case R.id.mesut_view:
                songs = DataManeger.querySongs(getActivity(), "Mesut Kurtis");
                if (songs == null || songs.size() == 0) {
                    MusicSyncTask.getMesutKurtisSongs(getActivity());
                }else{
                   openHomeFragment();
                }
                break;

            case R.id.mazzika_view:
                if (songs == null || songs.size() == 0) {
                    MusicSyncTask.syncMusic(getActivity());
                }else{
                    openHomeFragment();
                }
                break;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("songs", songs);

    }

    public void openHomeFragment() {
        Bundle args = new Bundle();
        args.putParcelableArrayList("songs", songs);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onDownloadFinished(String artiste) {
        if(artiste.equals(getString(R.string.mesut))){
            songs = DataManeger.querySongs(getActivity(),"Mesut Kurtis");
        }else if(artiste.equals(getString(R.string.maher))){
            songs =DataManeger.querySongs(getActivity(),"Maher Zain");
        }else {
            songs = DataManeger.querySongs(getActivity());
        }
        openHomeFragment();
    }
}
