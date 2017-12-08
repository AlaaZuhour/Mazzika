package com.udacity.capstone.musicapp.ui.adapter;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.udacity.capstone.musicapp.R;
import com.udacity.capstone.musicapp.model.Item;
import com.udacity.capstone.musicapp.model.PriorityQueue;
import com.udacity.capstone.musicapp.model.Song;
import com.udacity.capstone.musicapp.ui.SongSelectedListener;

import java.util.ArrayList;



public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.RecyclerViewHolder>{
    private ArrayList<Song> musicList;
    private ArrayList<Item> itemList;
    private boolean isSearch;
    private Context mContext;
    private SongSelectedListener mListener;

    public MusicAdapter(@NonNull Context context, ArrayList<Song> music) {
        this.musicList= music;
        this.mContext = context;
        this.isSearch = false;
        this.mListener = (SongSelectedListener) context;
    }

    public MusicAdapter(Fragment fragment){
        this.mListener = (SongSelectedListener) fragment;
        this.mContext = fragment.getActivity();
    }


    public void setList(ArrayList<Item> items){
        this.itemList = items;
        isSearch = true;
    }
    public void setMusicList(ArrayList<Song> songs){
        this.musicList = songs;
        isSearch =false;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.song_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent,  false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if(isSearch){
            Item currentItem = itemList.get(position);
            holder.title.setText(currentItem.getSnippet().getTitle());
            Glide.with(mContext).load(currentItem.getSnippet().getThumbnails().getDefault().getUrl()).into(holder.imageView);
        }else {
            Song current = musicList.get(position);
            holder.title.setText(current.getTitle());
            holder.art.setText(current.getArtist());
            Glide.with(mContext).load(current.getImageUrl()).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        if(isSearch){
            return itemList.size();
        }else {
            return musicList.size();
        }
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title,art;
        ImageView imageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.song_title);
            art =  itemView.findViewById(R.id.artiste_name);
            imageView = itemView.findViewById(R.id.thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onSongSelectedListener(getAdapterPosition());
        }
    }
}
