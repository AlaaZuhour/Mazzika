package com.udacity.capstone.musicapp.ui;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.mp4.Mp4Extractor;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.udacity.capstone.musicapp.data.DataManeger;
import com.udacity.capstone.musicapp.model.Song;

import java.util.ArrayList;

import com.udacity.capstone.musicapp.model.PriorityQueue;

public class MusicService extends Service implements ExoPlayer.EventListener {

    public static final String NEXT_ACTION = "com.udacity.mazzika.next";
    public static final String PREVIOUS_ACTION = "com.udacity.mazzika.previous";
    public static final String TOGGLEPAUSE_ACTION = "com.udacity.mazzika.togglepause";
    private SimpleExoPlayer mExoPlayer;
    private final IBinder musicBind = new MusicBinder();
    private ArrayList<Song> songs;
    private Song playSong;
    private WidgetReceiver mWidgetReceiver;
    //current position
    private int songPosn=0;

    public MusicService() {
    }
    public void onCreate(){
        super.onCreate();
        songPosn=0;
        mWidgetReceiver = new WidgetReceiver();
        initMusicPlayer();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(TOGGLEPAUSE_ACTION);
        filter.addAction(NEXT_ACTION);
        filter.addAction(PREVIOUS_ACTION);
        registerReceiver(mWidgetReceiver, filter);

    }

    public SimpleExoPlayer getExoPlayer() {
        return mExoPlayer;
    }


    public void setList(ArrayList<Song> theSongs){
        songs=theSongs;
    }

    public void setSongPosn(int position){
        this.songPosn = position;
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        Log.d("next","timeLine");
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        Log.d("next","tracks");
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
       if(playbackState == ExoPlayer.STATE_ENDED){
           prepareIntent();
           playNext();
       }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        Log.d("next","playback");
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
    public void initMusicPlayer(){
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            mExoPlayer.addListener(this);
            // Prepare the MediaSource.

        }
    }

    private MediaSource buildMediaSource(String url) {
        String userAgent = Util.getUserAgent(this, " ");
        Handler mHandler = new Handler();
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory(
                userAgent, null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true);
       return new ExtractorMediaSource(Uri.parse(url), dataSourceFactory, Mp4Extractor.FACTORY,
               mHandler, null);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return musicBind;

    }
    public void playSong(){
        if(songs == null) {
            songs = DataManeger.querySongs(this);
        }
        playSong = songs.get(songPosn);
        MediaSource mediaSource1 = buildMediaSource(playSong.getStreamUrl());
        mExoPlayer.prepare(mediaSource1);
        mExoPlayer.setPlayWhenReady(true);
        prepareIntent();
    }

    private void prepareIntent() {
        Intent musicIntent = new Intent();
        musicIntent.putExtra("name",playSong.getTitle());
        musicIntent.putExtra("artist",playSong.getArtist());
        musicIntent.putExtra("playing",mExoPlayer.getPlayWhenReady());
        sendStickyBroadcast(musicIntent);
    }


    @Override
    public boolean onUnbind(Intent intent){
        mExoPlayer.stop();
        mExoPlayer.release();
        unregisterReceiver(mWidgetReceiver);
        return false;
    }

    public void pause(){
        mExoPlayer.setPlayWhenReady(false);
    }

    public boolean isPlaying(){
        return mExoPlayer.getPlayWhenReady();
    }
    public void play(){
        mExoPlayer.setPlayWhenReady(true);
    }
    public void playNext(){
        int newPos = ++songPosn;
        if(newPos >= songs.size()){
            songPosn=0;
        }
        playSong();
    }

    public int getSongPosn() {
        return songPosn;
    }

    public void playPrev(){
        int newPos = --songPosn;
        if(newPos < 0){
            songPosn=0;
        }
        playSong();
    }

    public class WidgetReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(NEXT_ACTION)) {
                playNext();
            }else if(intent.getAction().equals(PREVIOUS_ACTION)){
                playPrev();
            }else if(intent.getAction().equals(TOGGLEPAUSE_ACTION)){
                if(mExoPlayer.getPlaybackState() == ExoPlayer.STATE_READY){

                }
            }
        }
    }
}
