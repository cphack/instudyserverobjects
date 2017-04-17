package com.codepath.android.instudy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.fragments.NewCourseFragment;

public class NewCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            NewCourseFragment frmNewCourse = (NewCourseFragment)
                    getSupportFragmentManager().findFragmentById(R.id.frmNewCourse);
            frmNewCourse.setNewCourseFragmentListener(new NewCourseFragment.NewCourseFragmentListener(){
                @Override
                public void onSaveCourse() {
                    Intent i = new Intent(NewCourseActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }
    }
}
