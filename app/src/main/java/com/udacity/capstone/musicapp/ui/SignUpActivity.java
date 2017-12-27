package com.udacity.capstone.musicapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.udacity.capstone.musicapp.R;
import com.udacity.capstone.musicapp.utilities.DataValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.email_address)
    EditText email;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.login)
    TextView login;

    @BindView(R.id.sign_up)
    Button signUp;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        login.setOnClickListener(v->{
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
        signUp.setOnClickListener(v->{

            String emailText = email.getText().toString().trim();
            String passwordText = password.getText().toString().trim();

            if(!DataValidator.isValidEmail(emailText)){
                email.setError(getString(R.string.invalid_email));
                return;
            }else if(!DataValidator.isValidPassword(passwordText)){
                password.setError(getString(R.string.invalid_password));
                return;
            }

            auth.createUserWithEmailAndPassword(emailText,passwordText)
                    .addOnCompleteListener(SignUpActivity.this, task ->{
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, getString(R.string.auth_failed),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(SignUpActivity.this, task ->{
                       Log.d("message",task.getMessage());
            });
        });
    }


}
