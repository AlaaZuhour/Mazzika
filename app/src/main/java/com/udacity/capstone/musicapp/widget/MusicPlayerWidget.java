package com.udacity.capstone.musicapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.udacity.capstone.musicapp.R;
import com.udacity.capstone.musicapp.data.DataManeger;
import com.udacity.capstone.musicapp.ui.MainActivity;
import com.udacity.capstone.musicapp.ui.MusicService;


public class MusicPlayerWidget extends AppWidgetProvider {

    protected static final int REQUEST_NEXT = 1;
    protected static final int REQUEST_PREV = 2;
    protected static final int REQUEST_PLAYPAUSE = 3;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        onUpdate(context, appWidgetManager, appWidgetIds, null);
    }

    public static void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds,Bundle extras){
        ComponentName serviceName = new ComponentName(context, MusicService.class);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_standard);
        remoteViews.setOnClickPendingIntent(R.id.image_next, PendingIntent.getService(
                context,
                REQUEST_NEXT,
                new Intent(context, MusicService.class)
                        .setAction(MusicService.NEXT_ACTION)
                        .setComponent(serviceName),
                0
        ));
        remoteViews.setOnClickPendingIntent(R.id.image_prev, PendingIntent.getService(
                context,
                REQUEST_PREV,
                new Intent(context, MusicService.class)
                        .setAction(MusicService.PREVIOUS_ACTION)
                        .setComponent(serviceName),
                0
        ));
        remoteViews.setOnClickPendingIntent(R.id.image_playpause, PendingIntent.getService(
                context,
                REQUEST_PLAYPAUSE,
                new Intent(context, MusicService.class)
                        .setAction(MusicService.TOGGLEPAUSE_ACTION)
                        .setComponent(serviceName),
                0
        ));
        if (extras != null) {
            String t = extras.getString("name");
            if (t != null) {
                remoteViews.setTextViewText(R.id.textView_title, t);
            }
            t = extras.getString("artist");
            if(t != null){
                remoteViews.setTextViewText(R.id.textView_subtitle, t);
            }
            remoteViews.setImageViewResource(R.id.image_playpause,
                    extras.getBoolean("playing") ? R.drawable.ic_pause_white_36dp : R.drawable.ic_play_white_36dp);
        }
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }




}