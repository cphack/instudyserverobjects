package com.codepath.android.instudy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.android.instudy.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import static com.codepath.android.instudy.R.id.tvSignUp;

public class LoginActivity extends AppCompatActivity {

    EditText etLogin;
    EditText etPassword;
    Button btnLogin;
    TextView tvSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ParseUser currentUser = ParseUser.getCurrentUser();

        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToSignUp();
            }
        });

        etLogin = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        if (currentUser != null) {
            // Send logged in users to Welcome.class
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void loginUser() {
        String user = etLogin.getText().toString();
        String pass = etPassword.getText().toString();

        // Send data to Parse.com for verification
        ParseUser.logInInBackground(user, pass,
                new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Signup failed. Look at the ParseException to see what happened.
                        }
                    }
                });
    }

    private void redirectToSignUp(){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

}
