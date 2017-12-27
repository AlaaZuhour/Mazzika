package com.udacity.capstone.musicapp.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.udacity.capstone.musicapp.R;
import com.udacity.capstone.musicapp.ui.fragment.ArtistFragment;
import com.udacity.capstone.musicapp.ui.fragment.FavoriteFragment;
import com.udacity.capstone.musicapp.ui.fragment.HomeFragment;
import com.udacity.capstone.musicapp.ui.fragment.PlayFragment;
import com.udacity.capstone.musicapp.ui.fragment.PlayListFragment;
import com.udacity.capstone.musicapp.ui.fragment.YouTubeFragment;
import com.udacity.capstone.musicapp.utilities.FragmentState;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private FirebaseAuth auth;
    private static final String FRAGMENT_SAVED_STATE = "FRAGMENT_SAVED_STATE";
    private HomeFragment homeFragment;
    private PlayListFragment playListFragment;
    private YouTubeFragment youTubeFragment;
    private ArtistFragment artistFragment;
    public PlayFragment playFragment;
    private FavoriteFragment favoriteFragment;
    public FragmentState mFragmentState;
    private boolean fromSavedInstanceState;

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();

        fromSavedInstanceState = savedInstanceState != null;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.inflateMenu(R.menu.option_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.logout:
                    if (auth.getCurrentUser() != null) {
                        auth.signOut();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    }
                    break;
            }
            return true;
        });
        toolbar.setNavigationOnClickListener(v -> {
            FragmentManager fm = getFragmentManager();
            Fragment currentFragment = fm.findFragmentById(R.id.fragment_container);
            if (currentFragment != null) {
                if (currentFragment instanceof HomeFragment) {
                    return;
                } else {
                    fm.popBackStack(null, 0);
                }
            }


        });


        if (savedInstanceState != null) {
            mFragmentState = FragmentState.values()[savedInstanceState.getInt(FRAGMENT_SAVED_STATE)];
            setFragmentState(mFragmentState);
            switch (mFragmentState) {
                case Home:
                    homeFragment = (HomeFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
                    showHomeFragment();
                    break;
                case Play:
                    playFragment = (PlayFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
                    break;
                case Favorite:
                    favoriteFragment = (FavoriteFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
                    showFavoriteFragment();
                    break;
                case PlayList:
                    playListFragment = (PlayListFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
                    showPlayListFragment();
                    break;
                case YouTube:
                    youTubeFragment = (YouTubeFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
                    showYouTubeFragment();
                    break;
                case Artist:
                    artistFragment = (ArtistFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
                    showArtistFragment();
                    break;


            }
            if (homeFragment != null) {
                homeFragment.setRecyclerPostion(savedInstanceState.getInt("recycler"));
            }
            if (playFragment != null) {
                playFragment.setPlayerPostion(savedInstanceState.getLong("player"));
                playFragment.setSongPos(savedInstanceState.getInt("position"));
            }
            if (favoriteFragment != null) {
                favoriteFragment.setRecyclerPostion(savedInstanceState.getInt("recycler_favorite"));
            }


        }

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            navigation.setSelectedItemId(R.id.home);
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (homeFragment != null) {
            outState.putInt("recycler", homeFragment.getRecyclerPostion());
        }
        if (playFragment != null) {
            outState.putInt("position", playFragment.getSongPos());
            outState.putLong("player", playFragment.getPlayerPostion());
        }
        if (favoriteFragment != null) {
            outState.putInt("recycler_favorite", favoriteFragment.getRecyclerPostion());
        }

        outState.putInt(FRAGMENT_SAVED_STATE, mFragmentState.ordinal());


    }

    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {


        return true;
    }


    @Override
    protected void onResume() {

        super.onResume();

    }


    //connect to the service


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.playlist:
                setFragmentState(FragmentState.PlayList);
                return true;
            case R.id.favorite:
                setFragmentState(FragmentState.Favorite);
                return true;
            case R.id.home:
                setFragmentState(FragmentState.Home);
                return true;
            case R.id.youtube:
                setFragmentState(FragmentState.YouTube);
                return true;
            case R.id.artiste:
                setFragmentState(FragmentState.Artist);
                return true;
            default:
                setFragmentState(FragmentState.Play);
        }
        return false;
    };

    public void setFragmentState(FragmentState fragmentState) {
        mFragmentState = fragmentState;
        switch (mFragmentState) {
            case Home:
                showHomeFragment();
                break;
            case Play:
                showPlayFragment();
                break;
            case Artist:
                showArtistFragment();
                break;
            case YouTube:
                showYouTubeFragment();
                break;
            case PlayList:
                showPlayListFragment();
                break;
            case Favorite:
                showFavoriteFragment();
                break;
        }
    }

    private void showFavoriteFragment() {
        favoriteFragment = new FavoriteFragment();
        FragmentManager fm2 = getFragmentManager();
        fm2.beginTransaction()
                .replace(R.id.fragment_container, favoriteFragment)
                .addToBackStack(null)
                .commit();
    }

    private void showPlayListFragment() {
        playListFragment = new PlayListFragment();
        FragmentManager fragmentManager2 = getFragmentManager();
        fragmentManager2.beginTransaction()
                .replace(R.id.fragment_container, playListFragment)
                .addToBackStack(null)
                .commit();
    }

    private void showYouTubeFragment() {
        youTubeFragment = new YouTubeFragment();
        FragmentManager fragmentManager1 = getFragmentManager();
        fragmentManager1.beginTransaction()
                .replace(R.id.fragment_container, youTubeFragment)
                .addToBackStack(null)
                .commit();
    }

    private void showArtistFragment() {
        artistFragment = new ArtistFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragment_container, artistFragment)
                .addToBackStack(null)
                .commit();
    }

    private void showPlayFragment() {
        if(!fromSavedInstanceState) {
            playFragment = new PlayFragment();
            FragmentManager fm1 = getFragmentManager();
            fm1.beginTransaction()
                    .replace(R.id.fragment_container, playFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void showHomeFragment() {
        homeFragment = new HomeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .addToBackStack(null)
                .commit();
    }
}