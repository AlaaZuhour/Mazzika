package com.udacity.capstone.musicapp.ui.fragment;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.udacity.capstone.musicapp.R;
import com.udacity.capstone.musicapp.data.DataManeger;
import com.udacity.capstone.musicapp.model.Playlist;
import com.udacity.capstone.musicapp.model.Song;
import com.udacity.capstone.musicapp.ui.MainActivity;
import com.udacity.capstone.musicapp.ui.MusicService;
import com.udacity.capstone.musicapp.ui.adapter.PlayListSelectionAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;


public class PlayFragment extends Fragment implements View.OnClickListener{


    private MusicService musicSrv;
    private boolean musicBound=false;
    private Intent playIntent;

    @BindView(R.id.next_but)
    View nextButton;

    @BindView(R.id.previous_but)
    View prevButton;

    @BindView(R.id.play_but)
    View playButton;

    private ArrayList<Song> songArrayList;
    private ArrayList<Playlist> playlists;
    private ArrayList<String> playlistsNames;
    private int songPos;
    private long playerPostion;

    @BindView(R.id.song_image)
    ImageView songImage;

    @BindView(R.id.imageView2)
    ImageView backgroundImage;

    @BindView(R.id.songTitle)
    TextView songTitle;

    @BindView(R.id.favoriteButton)
    ImageButton favorite;

    @BindView(R.id.addToPlayList)
    ImageButton addToPlayList;


    private PlayListSelectionAdapter selectionAdaptor;


    public PlayFragment() {
        // Required empty public constructor
    }

    public long getPlayerPostion() {
        return playerPostion;
    }

    public void setPlayerPostion(long postion) {
        playerPostion = postion;
        if(musicSrv != null){
            musicSrv.getExoPlayer().seekTo(playerPostion);
        }
    }



    public int getSongPos() {
        return songPos;
    }

    public void setSongPos(int songPos) {
        this.songPos = songPos;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(playIntent==null){
            playIntent = new Intent(getActivity(), MusicService.class);
            getActivity().startService(playIntent);
            getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_play, container, false);
        ButterKnife.bind(this,rootView);
        ((MainActivity)getActivity()).playFragment = this;
        if(getArguments() != null) {
            songArrayList = getArguments().getParcelableArrayList("songs");
            songPos = getArguments().getInt("position");
        }
        if(savedInstanceState != null){
            songArrayList = savedInstanceState.getParcelableArrayList("songs");
        }
        if(musicSrv != null){
            musicSrv.getExoPlayer().seekTo(playerPostion);
        }
        songTitle.setText(songArrayList.get(songPos).getTitle());
        nextButton.setEnabled(true);
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        favorite.setOnClickListener(this);
        addToPlayList.setOnClickListener(this);

        setImages();



        return rootView;
    }


    private void setImages(){
        Glide.with(getActivity()).load(songArrayList.get(songPos).getImageUrl())
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(30)))
                .into(backgroundImage);

        Glide.with(getActivity()).load(songArrayList.get(songPos).getImageUrl())
                .into(songImage);

        if(songArrayList.get(songPos).isFavorit()){
            favorite.setImageResource(android.R.drawable.star_big_on);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState != null) {
            outState.putParcelableArrayList("songs", songArrayList);
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            songArrayList = savedInstanceState.getParcelableArrayList("songs");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
            musicSrv.getExoPlayer().seekTo(playerPostion);
            Log.d("player",musicSrv.getExoPlayer().getCurrentPosition()+"");
            musicSrv.playSong();
            if(playButton != null) {
                ((ImageView) playButton).setImageResource(R.drawable.pause_but);
            }
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
    public void onResume() {

        if(musicSrv != null){
            musicSrv.getExoPlayer().seekTo(playerPostion);
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if(musicSrv.getExoPlayer() != null){
            playerPostion = musicSrv.getExoPlayer().getCurrentPosition();
        }
        musicSrv.pause();
        super.onPause();
    }


    @Override
    public void onClick(View v) {

        if(v == nextButton){
            musicSrv.playNext();
            songPos = musicSrv.getSongPosn();
            ((ImageView)playButton).setImageResource(R.drawable.pause_but);
            setImages();

        }else if(v==prevButton){
            musicSrv.playPrev();
            ((ImageView)playButton).setImageResource(R.drawable.pause_but);
            songPos = musicSrv.getSongPosn();
            setImages();

        }else if(v == favorite){
            updateSong();
        }else if(v == addToPlayList){
            openAddToPlayListDialog();
        }else if(v == playButton){
            if(musicSrv.isPlaying()){
                ((ImageView)playButton).setImageResource(R.drawable.play_but);
                musicSrv.pause();
            }else {
                ((ImageView)playButton).setImageResource(R.drawable.pause_but);
                musicSrv.play();
            }
        }
    }

    private void updatePlayLists(Playlist selectedPlayList) {
        DataManeger.updateSongsPlayList(getActivity(),songArrayList.get(songPos),selectedPlayList.getId());
    }

    private void openAddToPlayListDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_songs_dialog, null);
        dialogBuilder.setView(dialogView);

        playlists = DataManeger.queryPlaylist(getActivity());
        if(playlists != null || playlists.size() == 0) {
            final RecyclerView songsRecyclerView = dialogView.findViewById(R.id.song_list_dialog);
            prepareRecycler(songsRecyclerView);

        }else {
            Toast.makeText(getActivity(),getString(R.string.no_playlist),Toast.LENGTH_SHORT).show();
            return;
        }

        dialogBuilder.setTitle(getString(R.string.choose_playlist));

        dialogBuilder.setPositiveButton(getString(R.string.done), (dialog, whichButton) -> {
            Playlist selectedPlayList = selectionAdaptor.getSelectedList();
            updatePlayLists(selectedPlayList);
        });

        dialogBuilder.setNegativeButton(getString(R.string.cancel), (dialog, whichButton) ->
                dialog.dismiss());
        AlertDialog b = dialogBuilder.create();
        b.show();

    }

    private void prepareRecycler(RecyclerView songsRecyclerView) {
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity());
        selectionAdaptor = new PlayListSelectionAdapter();
        selectionAdaptor.setPlaylists(playlists);
        songsRecyclerView.setLayoutManager(linearLayoutManager);
        songsRecyclerView.setAdapter(selectionAdaptor);
    }

    private void updateSong() {
        if(songArrayList.get(songPos).isFavorit()){
            favorite.setImageResource(android.R.drawable.star_big_off);
            DataManeger.updateSongsFavorite(getActivity(),songArrayList.get(songPos),0);
            songArrayList.get(songPos).setFavorit(false);
        }else{
            favorite.setImageResource(android.R.drawable.star_big_on);
            DataManeger.updateSongsFavorite(getActivity(),songArrayList.get(songPos),1);
            songArrayList.get(songPos).setFavorit(true);
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
