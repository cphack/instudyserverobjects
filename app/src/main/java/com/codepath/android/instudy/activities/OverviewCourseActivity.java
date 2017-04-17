package com.codepath.android.instudy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.fragments.OverviewCourseFragment;

public class OverviewCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_course);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String courseid = getIntent().getStringExtra("courseid");

        if (savedInstanceState == null) {
            OverviewCourseFragment fragment = (OverviewCourseFragment)
                    getSupportFragmentManager().findFragmentById(R.id.frmFragment);
            fragment.getCourseData(courseid);

        }

    }
}
