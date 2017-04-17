package com.codepath.android.instudy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.fragments.AssignmentListFragment;
import com.codepath.android.instudy.fragments.LectionListFragment;


public class AssignmentListActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String courseid = getIntent().getStringExtra("courseid");

        if (savedInstanceState == null) {
            AssignmentListFragment fragment = (AssignmentListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment);
            fragment.populateAssignmentList(courseid);
        }
    }
}
