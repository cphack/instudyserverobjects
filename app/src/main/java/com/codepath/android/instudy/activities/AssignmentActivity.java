package com.codepath.android.instudy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.fragments.AssignmentFragment;
import com.codepath.android.instudy.fragments.LectionFragment;

public class AssignmentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            AssignmentFragment frmFragment = (AssignmentFragment)
                    getSupportFragmentManager().findFragmentById(R.id.frmAssignment);
        }
    }
}
