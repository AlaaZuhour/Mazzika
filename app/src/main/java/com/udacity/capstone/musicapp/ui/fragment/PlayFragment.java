package com.udacity.capstone.musicapp.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.udacity.capstone.musicapp.R;
import com.udacity.capstone.musicapp.model.Song;
import com.udacity.capstone.musicapp.ui.MusicService;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;


public class PlayFragment extends Fragment implements View.OnClickListener{


    private MusicService musicSrv;
    private boolean musicBound=false;
    private Intent playIntent;
    private View nextButton,prevButton;
    private ArrayList<Song> songArrayList;
    private int songPos;
    private ImageView songImage, backgroundImage;
    private SimpleExoPlayerView exoPlayerView;

    public PlayFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(playIntent==null){
            playIntent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(playIntent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_play, container, false);
        backgroundImage = rootView.findViewById(R.id.imageView2);
        songImage = rootView.findViewById(R.id.song_image);
        exoPlayerView = rootView.findViewById(R.id.playerView);
        exoPlayerView.requestFocus();
        nextButton = rootView.findViewById(com.google.android.exoplayer2.ui.R.id.exo_next);
        prevButton = rootView.findViewById(com.google.android.exoplayer2.ui.R.id.exo_prev);
        nextButton.setEnabled(true);
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        exoPlayerView.setControllerShowTimeoutMs(0);
        songArrayList = getArguments().getParcelableArrayList("songs");
        songPos = getArguments().getInt("position");

        Glide.with(getActivity()).load(songArrayList.get(songPos).getImageUrl())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(30)))
                .into(backgroundImage);

        Glide.with(getActivity()).load(songArrayList.get(songPos).getImageUrl())
                .into(songImage);
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
        getActivity().stopService(playIntent);
        musicBound=false;
        musicSrv=null;
        super.onDetach();
    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songArrayList);
            musicSrv.setSongPosn(songPos);
            exoPlayerView.setPlayer(musicSrv.getExoPlayer());
            musicSrv.playSong();
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };


    public void onDestroy() {
        getActivity().stopService(playIntent);
        musicBound=false;
        musicSrv=null;
        super.onDestroy();
    }

    @Override
    public void onPause() {
        musicSrv.pause();
        super.onPause();
    }

    @Override
    public void onStop() {
        musicSrv.pause();
        super.onStop();
    }

    @Override
    public void onClick(View v) {

        if(v == nextButton){
            musicSrv.playNext();
        }else if(v==prevButton){
            musicSrv.playPrev();
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
