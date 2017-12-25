package com.udacity.capstone.musicapp.ui.fragment;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.udacity.capstone.musicapp.R;
import com.udacity.capstone.musicapp.data.DataManeger;
import com.udacity.capstone.musicapp.data.MusicContract;
import com.udacity.capstone.musicapp.model.Playlist;
import com.udacity.capstone.musicapp.model.Song;
import com.udacity.capstone.musicapp.ui.MainActivity;
import com.udacity.capstone.musicapp.ui.SongSelectedListener;
import com.udacity.capstone.musicapp.ui.adapter.MusicAdapter;
import com.udacity.capstone.musicapp.ui.adapter.SongSelectionAdaptor;
import com.udacity.capstone.musicapp.utilities.DataValidator;
import com.udacity.capstone.musicapp.utilities.FragmentState;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayListFragment extends Fragment implements SongSelectedListener {



    @BindView(R.id.playlist_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.add_playlist_button)
    FloatingActionButton addPlaylistButton;

    @BindView(R.id.no_playlist)
    TextView noPlaylists;


    private ArrayList<Playlist> playlists;
    private RecyclerView songsRecyclerView;
    private ArrayList<Song> songs;
    private SongSelectionAdaptor selectionAdaptor;


    public PlayListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_play_list, container, false);
        ButterKnife.bind(this,rootView);

        int ot = getResources().getConfiguration().orientation;
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), ot == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2);
        mRecyclerView.setLayoutManager(layoutManager);

        if (savedInstanceState != null) {
            playlists = savedInstanceState.getParcelableArrayList("playlist");
        }else{
           playlists = DataManeger.queryPlaylist(getActivity());
        }

        if (playlists == null || playlists.size() == 0) {
            noPlaylists.setVisibility(View.VISIBLE);
        } else {
            MusicAdapter musicAdapter = new MusicAdapter(this);
            noPlaylists.setVisibility(View.GONE);
            musicAdapter.setPlaylists(playlists);
            mRecyclerView.setAdapter(musicAdapter);
        }
        addPlaylistButton.setOnClickListener(v -> openNewPlaylistDialog());

        return rootView;
    }
   
    EditText edt;
    private void openNewPlaylistDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_new_playlist_dialog, null);
        dialogBuilder.setView(dialogView);
        edt = dialogView.findViewById(R.id.playlist_name);
        dialogBuilder.setTitle(getString(R.string.new_playlist));
        dialogBuilder.setMessage(getString(R.string.new_playlist_name));
        dialogBuilder.setPositiveButton("Done", (dialog, whichButton) -> {
            addPlaylist(edt.getText().toString());
            openAddSongsDialog(edt.getText().toString());
            noPlaylists.setVisibility(View.GONE);
        });
        dialogBuilder.setNegativeButton("Cancel", (dialog, whichButton) ->
                dialog.dismiss());
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void openAddSongsDialog(String s) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_songs_dialog, null);
        dialogBuilder.setView(dialogView);

        songs = DataManeger.querySongs(getActivity());
        songsRecyclerView = dialogView.findViewById(R.id.song_list_dialog);
        prepareRecycler(songsRecyclerView);

        dialogBuilder.setTitle(getString(R.string.add_songs));
        dialogBuilder.setMessage(String.format(getString(R.string.add_playlist_songs), s));

        dialogBuilder.setPositiveButton("Done", (dialog, whichButton) -> {
            ArrayList<Song> selectedSongs = selectionAdaptor.getSelectedList();
            updateSongs(selectedSongs);
        });

        dialogBuilder.setNegativeButton("Cancel", (dialog, whichButton) ->
                dialog.dismiss());
        AlertDialog b = dialogBuilder.create();
        b.show();

    }

    private void prepareRecycler(RecyclerView songsRecyclerView) {
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity());
        selectionAdaptor = new SongSelectionAdaptor(this);
        selectionAdaptor.setMusicList(songs);
        songsRecyclerView.setLayoutManager(linearLayoutManager);
        songsRecyclerView.setAdapter(selectionAdaptor);
    }

    private void updateSongs(ArrayList<Song> selectedSongs) {
        int playlistID = DataManeger.queryPlaylistWithName(getActivity(), edt.getText().toString());

        for (int i = 0; i < selectedSongs.size(); i++) {
            DataManeger.updateSongsPlayList(getActivity(), selectedSongs.get(i), playlistID);
        }
        prepareRecycler(songsRecyclerView);
    }

    private void addPlaylist(String s) {
        ContentValues[] contentValues = new ContentValues[1];
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(MusicContract.PlaylistEntry.COLUMN_PLAYLIST_NAME, s);
        contentValues[0] = contentValues1;
        DataManeger.addPlaylists(getActivity(), contentValues);
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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("playlist", playlists);

    }

    @Override
    public void onSongSelectedListener(int postion) {
        ArrayList<Playlist> playlists = DataManeger.queryPlaylist(getActivity());
        ArrayList<Song> songs = DataManeger.querySongsWithPlayList(getActivity(), playlists.get(postion).getId());
        Bundle args = new Bundle();
        args.putParcelableArrayList("songs", songs);
        args.putInt("position", postion);
        PlayFragment playFragment = new PlayFragment();
        playFragment.setArguments(args);
        ((MainActivity) getActivity()).mFragmentState = FragmentState.Play;
        ((MainActivity) getActivity()).playFragment = playFragment;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, playFragment);
        ft.addToBackStack(null);
        ft.commit();
    }



}
