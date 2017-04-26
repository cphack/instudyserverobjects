package com.codepath.android.instudy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.codepath.android.instudy.R;


public class ChatListActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String setTitle = getIntent().getStringExtra("title");
        if(setTitle==null|| TextUtils.isEmpty(setTitle)) {
            getSupportActionBar().setTitle("Chats");
        }else
        {
            getSupportActionBar().setTitle(setTitle);
        }

    }
}

