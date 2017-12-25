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
import com.udacity.capstone.musicapp.model.Song;
import com.udacity.capstone.musicapp.ui.SongSelectedListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongSelectionAdaptor extends RecyclerView.Adapter<SongSelectionAdaptor.RecyclerViewHolder>{

    private ArrayList<Song> musicList, selectedList;
    private Context mContext;
    private SongSelectedListener mListener;

    public SongSelectionAdaptor(Fragment fragment){
        selectedList = new ArrayList<>();
        this.mListener = (SongSelectedListener) fragment;
        this.mContext = fragment.getActivity();
    }

    public void setMusicList(ArrayList<Song> songs){
        this.musicList = songs;
    }

    @Override
    public SongSelectionAdaptor.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.song_selection_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent,  false);
        return new SongSelectionAdaptor.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongSelectionAdaptor.RecyclerViewHolder holder, int position) {
        Song current = musicList.get(position);
        holder.title.setText(current.getTitle());
        holder.art.setText(current.getArtist());
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder  {

        @BindView(R.id.textView)
        TextView title;

        @BindView(R.id.textView2)
        TextView art;

        @BindView(R.id.imageView)
        ImageView imageView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(v -> {
                if(imageView.getVisibility() == View.INVISIBLE){
                    imageView.setVisibility(View.VISIBLE);
                   addSong(getAdapterPosition());
                }else {
                    imageView.setVisibility(View.INVISIBLE);
                    removeSong(getAdapterPosition());
                }
            });
        }
    }

    private void removeSong(int adapterPosition) {
        selectedList.remove(musicList.get(adapterPosition));
    }

    private void addSong(int adapterPosition) {
        selectedList.add(musicList.get(adapterPosition));
    }

    public ArrayList<Song> getSelectedList(){
        return selectedList;
    }
}
