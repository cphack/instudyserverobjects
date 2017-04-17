package com.codepath.android.instudy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.fragments.LectionListFragment;
import com.codepath.android.instudy.fragments.OverviewCourseFragment;


public class LectionsListActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lection_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String courseid = getIntent().getStringExtra("courseid");

        if (savedInstanceState == null) {
            LectionListFragment fragment = (LectionListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment);
            fragment.populateLectionList(courseid);
        }
    }
}
