package com.codepath.android.instudy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.adapters.ChatMessageAdapter;
import com.codepath.android.instudy.fragments.LectionFragment;
import com.codepath.android.instudy.fragments.NewCourseFragment;
import com.codepath.android.instudy.models.Chat;
import com.codepath.android.instudy.models.Lection;
import com.codepath.android.instudy.models.Message;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.codepath.android.instudy.activities.ChatActivity.MAX_CHAT_MESSAGES_TO_SHOW;

public class LectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lection);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            LectionFragment frmFragment = (LectionFragment)
                    getSupportFragmentManager().findFragmentById(R.id.frmLection);
            /*frmNewCourse.setNewCourseFragmentListener(new NewCourseFragment.NewCourseFragmentListener() {
                @Override
                public void onSaveCourse() {
                    Intent i = new Intent(NewCourseActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });*/
        }
    }
}
