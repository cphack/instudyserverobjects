package com.codepath.android.instudy.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.android.instudy.R;
import com.codepath.android.instudy.adapters.UserListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static android.R.attr.name;
import static com.codepath.android.instudy.R.id.lvCourses;
import static com.codepath.android.instudy.R.string.teacher;


public class UserListActivity extends AppCompatActivity {

    RecyclerView lvUsers;
    UserListAdapter aUsers;
    ArrayList<ParseObject> users;
    String[] userids;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
         userids = getIntent().getStringArrayExtra("users");
        linearLayoutManager = new LinearLayoutManager(this);

        lvUsers = (RecyclerView) findViewById(R.id.lvUsers);

        initAdapter();
        lvUsers.setAdapter(aUsers);
        lvUsers.setLayoutManager(linearLayoutManager);

        populateUsers();
    }


    private void initAdapter() {
        users = new ArrayList<ParseObject>();
        //construct adapter from datasource
        aUsers = new UserListAdapter(this, users);
        aUsers.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // String name = users.get(position).name;
                Toast.makeText(UserListActivity.this, "item was clicked", Toast.LENGTH_SHORT).show();
            }
        });
        aUsers.setOnChatButtonClickListener(new UserListAdapter.OnChatButtonClickListener() {
            @Override
            public void onChatButtonClick(View view, int position) {
                // String name = users.get(position).name;
                Toast.makeText(UserListActivity.this, name + "button was clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //TODO:move to fragment
    private void populateUsers() {
        if(userids.length==0)return;
        List<String> userIdList = new ArrayList<String>(Arrays.asList(userids));

        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereContainedIn("objectId", userIdList);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    users.addAll(objects);
                    aUsers.notifyDataSetChanged();
                }
            }
        });
    }
}
