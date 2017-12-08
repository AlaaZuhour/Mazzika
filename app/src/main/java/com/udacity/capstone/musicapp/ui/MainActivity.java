package com.udacity.capstone.musicapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.udacity.capstone.musicapp.R;
import com.udacity.capstone.musicapp.ui.fragment.HomeFragment;
import com.udacity.capstone.musicapp.ui.fragment.YouTubeFragment;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        toolbar = findViewById(R.id.toolbar);


        toolbar.inflateMenu(R.menu.option_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.music:
                    Log.d("hi", item.getItemId() + "");
                    //songView.setVisibility(View.VISIBLE);
                    break;
                case R.id.end:
                    System.exit(0);

            }
            return true;
        });


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.home);

        FirebaseAuth.AuthStateListener authListener =  firebaseAuth -> {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
        };


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
                        break;
                    case R.id.favorite:
                        break;
                    case R.id.home:
                        HomeFragment fragment = new HomeFragment();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment)
                                .commit();
                        break;
                    case R.id.youtube:
                        YouTubeFragment fragment1 = new YouTubeFragment();
                        FragmentManager fragmentManager1 = getFragmentManager();
                        fragmentManager1.beginTransaction()
                                .replace(R.id.fragment_container, fragment1)
                                .commit();
                        break;


                }

                return false;
            };

}