package com.codepath.android.instudy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.adapters.UserListAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static android.R.attr.name;


public class UserListActivity extends AppCompatActivity {

    RecyclerView lvUsers;
    UserListAdapter aUsers;
    ArrayList<ParseUser> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        lvUsers = (RecyclerView) findViewById(R.id.lvUsers);

        initAdapter();

        populateUsers();
    }


    private void initAdapter() {
        users = new ArrayList<ParseUser>();
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


    private void populateUsers() {

    }
}
