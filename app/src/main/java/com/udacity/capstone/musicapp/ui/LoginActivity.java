package com.udacity.capstone.musicapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.udacity.capstone.musicapp.R;
import com.udacity.capstone.musicapp.utilities.DataValidator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email_address)
    EditText email;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.login)
    TextView login;

    @BindView(R.id.sign_up)
    Button signUp;

    private FirebaseAuth auth;
    private MobileAds mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        MobileAds.initialize(this, getString(R.string.app_id));

        AdView mAdView = findViewById(R.id.adView);

        if (mAdView != null) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }


        login.setText(getString(R.string.sign_up));
        login.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            finish();
        });
        signUp.setText(getString(R.string.login));
        signUp.setOnClickListener(v -> {

            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();

            if (!DataValidator.isValidEmail(emailText)) {
                email.setError(getString(R.string.invalid_email));
                return;
            } else if (!DataValidator.isValidPassword(passwordText)) {
                password.setError(getString(R.string.invalid_password));
                return;
            }

            auth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(LoginActivity.this, task -> {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        });


    }


}
