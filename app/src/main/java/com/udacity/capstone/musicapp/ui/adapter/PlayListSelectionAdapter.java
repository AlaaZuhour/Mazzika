package com.udacity.capstone.musicapp.ui.adapter;


import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.capstone.musicapp.R;
import com.udacity.capstone.musicapp.model.Playlist;
import com.udacity.capstone.musicapp.ui.SongSelectedListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayListSelectionAdapter extends RecyclerView.Adapter<PlayListSelectionAdapter.RecyclerViewHolder>{

    private ArrayList<Playlist> playlists;
    private int lastCheckedPosition=-1;

    public void setPlaylists(ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }

    @Override
    public PlayListSelectionAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.song_selection_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent,  false);
        return new PlayListSelectionAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayListSelectionAdapter.RecyclerViewHolder holder, int position) {
        if(position == lastCheckedPosition){
            holder.imageView.setVisibility(View.VISIBLE);
        }
        else {
            holder.imageView.setVisibility(View.INVISIBLE);
        }
        Playlist current = playlists.get(position);
        holder.title.setText(current.getName());
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textView)
        TextView title;

        @BindView(R.id.imageView)
        ImageView imageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(v -> {
                lastCheckedPosition = getAdapterPosition();
                notifyDataSetChanged();
            });
        }
    }



    public Playlist getSelectedList(){
        return playlists.get(lastCheckedPosition);
    }
}
