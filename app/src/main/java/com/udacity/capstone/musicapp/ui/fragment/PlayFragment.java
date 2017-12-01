package com.udacity.capstone.musicapp.ui.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.udacity.capstone.musicapp.R;
import com.udacity.capstone.musicapp.ui.MusicService;


public class PlayFragment extends Fragment implements YouTubePlayer.OnInitializedListener {


    private OnFragmentInteractionListener mListener;
    private MusicService musicSrv;
    private boolean musicBound=false;
    private Intent playIntent;
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
        //exoPlayerView = rootView.findViewById(R.id.playerView);


        //Glide.with(getActivity()).
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

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            //musicSrv.setList(tracks);
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
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
