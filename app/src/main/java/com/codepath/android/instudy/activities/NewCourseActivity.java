package com.codepath.android.instudy.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.fragments.NewCourseFragment;

public class NewCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course);

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
