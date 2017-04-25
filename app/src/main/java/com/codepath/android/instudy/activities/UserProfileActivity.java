package com.codepath.android.instudy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.adapters.UserListAdapter;
import com.codepath.android.instudy.fragments.UserProfile;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.attr.name;
import static com.codepath.android.instudy.R.id.lvUsers;


public class UserProfileActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("User profile");
        String userid = getIntent().getStringExtra("userid");

        if (savedInstanceState == null) {
            UserProfile f = (UserProfile)
                    getSupportFragmentManager().findFragmentById(R.id.fragment);
            if(f!=null){
                f.setProfile(userid);
            }
        }
    }


}
