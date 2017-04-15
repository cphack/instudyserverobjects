package com.codepath.android.instudy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.codepath.android.instudy.R;
import com.parse.ParseUser;


public class ChatActivity extends AppCompatActivity {

    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser!=null) {
            currentUser.logOut();

            Intent i = new Intent(ChatActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }
}
