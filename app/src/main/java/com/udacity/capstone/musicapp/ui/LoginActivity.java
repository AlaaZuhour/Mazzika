package com.udacity.capstone.musicapp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.udacity.capstone.musicapp.R;
import com.udacity.capstone.musicapp.utilities.DataValidator;

public class LoginActivity extends AppCompatActivity {

    private EditText email,password;
    private TextView login;
    private Button signUp;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email_address);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        login.setText(getString(R.string.sign_up));
        login.setOnClickListener(v->{
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            finish();
        });
        signUp = findViewById(R.id.sign_up);
        signUp.setText(getString(R.string.login));
        signUp.setOnClickListener(v->{

            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();

            if(!DataValidator.isValidEmail(emailText)){
                email.setError(getString(R.string.invalid_email));
            }else if(!DataValidator.isValidPassword(passwordText)){
                password.setError(getString(R.string.invalid_password));
            }

            auth.signInWithEmailAndPassword(emailText,passwordText)
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
