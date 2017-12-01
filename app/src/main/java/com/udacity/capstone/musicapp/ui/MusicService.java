package com.udacity.capstone.musicapp.ui;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.mp4.Mp4Extractor;
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource;
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
import com.udacity.capstone.musicapp.model.Song;

import java.util.ArrayList;

import com.udacity.capstone.musicapp.model.PriorityQueue;

public class MusicService extends Service implements ExoPlayer.EventListener {
    //media player
    private SimpleExoPlayer mExoPlayer;
    private final IBinder musicBind = new MusicBinder();
    private ArrayList<Song> songs;
    private PriorityQueue songsQueue;
    //current position
    private int songPosn=0;

    public MusicService() {
    }
    public void onCreate(){
        super.onCreate();
        songPosn=0;
        initMusicPlayer();
    }

    public SimpleExoPlayer getExoPlayer() {
        return mExoPlayer;
    }

    public PriorityQueue getSongsQueue() {
        return songsQueue;
    }

    public void setSongsQueue(PriorityQueue songsQueue1){
        songsQueue = songsQueue1;
    }
    public void setList(ArrayList<Song> theSongs){
        songs=theSongs;
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
       if(playbackState == ExoPlayer.STATE_ENDED){
           playSong();
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
//        songsQueue=MainActivity.songsQueue;
         if (songsQueue != null && songsQueue.size() != 0) {
             Song playSong = songsQueue.remove();
             songsQueue.insert(playSong);
             MediaSource mediaSource1 = new DynamicConcatenatingMediaSource();
             ((DynamicConcatenatingMediaSource)mediaSource1).addMediaSource(
                     buildMediaSource(playSong.getStreamUrl()));
             mExoPlayer.prepare(mediaSource1);
             mExoPlayer.setPlayWhenReady(true);
         }
    }
    public void setSong(int songIndex){
        songPosn=songIndex;
    }
    @Override
    public boolean onUnbind(Intent intent){
        mExoPlayer.stop();
        mExoPlayer.release();
        return false;
    }

    public void pause(){
        mExoPlayer.setPlayWhenReady(false);
    }
}
