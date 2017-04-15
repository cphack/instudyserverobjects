package com.codepath.android.instudy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.android.instudy.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignUpActivity extends AppCompatActivity {

    EditText etFullName;
    EditText etEmail;
    EditText etPassword;
    Button btnSignUp;
    TextView tvSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etEmail = (EditText)findViewById(R.id.etEmail);
        etFullName = (EditText)findViewById(R.id.etFullName);
        etPassword = (EditText)findViewById(R.id.etPassword);
        tvSignIn = (TextView) findViewById(R.id.tvSignIn);
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToLogin();
            }
        });
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser(){
        // Create the ParseUser

        String fullName = etFullName.getText().toString();
        String email = etEmail.getText().toString();
        String pass = etPassword.getText().toString();
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(email);
        user.setPassword(pass);
        user.setEmail(email);
        // Set custom properties
        user.put("FullName", fullName);
        user.put("Profile","None");
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    redirectToLogin();
                } else {
                    Toast.makeText(SignUpActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void redirectToLogin(){
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
