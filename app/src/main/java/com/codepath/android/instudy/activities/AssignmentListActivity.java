package com.codepath.android.instudy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.fragments.AssignmentListFragment;


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
        String courseTitle = getIntent().getStringExtra("courseTitle");
        TextView tvCourseTitle = (TextView) toolbar.findViewById(R.id.tvCourseTitle);
        if(courseTitle != null) tvCourseTitle.setText(courseTitle);

        if (savedInstanceState == null) {
            AssignmentListFragment fragment = (AssignmentListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment);
            fragment.populateAssignmentList(courseid);
        }
    }
}
