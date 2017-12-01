package com.udacity.capstone.musicapp.ui.fragment;

import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.udacity.capstone.musicapp.R;
import com.udacity.capstone.musicapp.model.Item;
import com.udacity.capstone.musicapp.model.SearchResponse;
import com.udacity.capstone.musicapp.retrofit.ITube;
import com.udacity.capstone.musicapp.retrofit.RetrofitBuilder;
import com.udacity.capstone.musicapp.ui.SongSelectedListener;
import com.udacity.capstone.musicapp.ui.adapter.MusicAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YouTubeFragment extends Fragment implements SongSelectedListener{

    private OnFragmentInteractionListener mListener;

    private SearchResponse searchResponse;
    private RecyclerView mRecyclerView;
    private MusicAdapter musicAdapter;
    private GridLayoutManager layoutManager;
    private Button searchButton;
    private EditText searchText;
    public YouTubeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_you_tube, container, false);
        mRecyclerView = view.findViewById(R.id.search_list);
        searchButton = view.findViewById(R.id.search_button);
        searchText = view.findViewById(R.id.search_word);
        int ot = getResources().getConfiguration().orientation;
        layoutManager =
                new GridLayoutManager(getActivity(),ot == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2);
        mRecyclerView.setLayoutManager(layoutManager);
        searchButton.setOnClickListener(v -> {
            ITube youtubeService = RetrofitBuilder.RetrieveITube();
            searchResponse = new SearchResponse();
            Call<SearchResponse> call = youtubeService.getVideosList(searchText.getText().toString());
            call.enqueue(new Callback<SearchResponse>() {
                @Override
                public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {
                   searchResponse = response.body();
                   musicAdapter = new MusicAdapter(YouTubeFragment.this);
                   musicAdapter.setList((ArrayList<Item>) searchResponse.getItems());
                   mRecyclerView.setAdapter(musicAdapter);
                }
                @Override
                public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                    Log.d("response","fail");
                }
            });
        });

        return view;
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

    @Override
    public void onSongSelectedListener(int postion) {
        Bundle arg = new Bundle();
        arg.putParcelable(getString(R.string.song_item),searchResponse.getItems().get(postion));
        PlayFragment fragment = new PlayFragment();
        fragment.setArguments(arg);
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
