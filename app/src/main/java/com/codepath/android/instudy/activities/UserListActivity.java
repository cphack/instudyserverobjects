package com.codepath.android.instudy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.adapters.UserListAdapter;
import com.codepath.android.instudy.fragments.LectionListFragment;
import com.codepath.android.instudy.fragments.UserListFragment;
import com.codepath.android.instudy.models.Chat;
import com.codepath.android.instudy.models.Course;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    RecyclerView lvUsers;
    UserListAdapter aUsers;
    ArrayList<ParseObject> users;

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String setTitle = getIntent().getStringExtra("title");
        if(setTitle==null|| TextUtils.isEmpty(setTitle)) {
            getSupportActionBar().setTitle("Students:");
        }else
        {
            getSupportActionBar().setTitle(setTitle);
        }



        String[] userids = getIntent().getStringArrayExtra("users");

        if (savedInstanceState == null) {
            UserListFragment fragment = (UserListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment);
            fragment.populateUserList(userids);
        }
    }

    public void openUserProfile(String userid) {
        Intent i = new Intent(UserListActivity.this, UserProfileActivity.class);
        i.putExtra("userid", userid);
        startActivity(i);
    }

    public void openChat(final String userid) {

        ArrayList<String> recipients = new ArrayList<>();
        recipients.add(userid);
        recipients.add(ParseUser.getCurrentUser().getObjectId());
        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        query.whereContainsAll(Chat.RECIPIENTS_KEY, recipients);
        query.findInBackground(new FindCallback<Chat>() {
            public void done(List<Chat> chats, ParseException e) {
                if (e == null) {
                    Boolean chatFound = false;
                    if (chats.size() > 0) {
                        for (Chat c : chats) {
                            if (c.getRecipients().size() == 2) {
                                chatFound = true;
                                openExistingChat(c.getObjectId());
                                break;
                            }
                        }
                    }
                    if (!chatFound) {
                        openNewChat(userid);
                    }
                }
            }
        });
    }

    private void openExistingChat(String chatId) {
        Intent i = new Intent(UserListActivity.this, ChatActivity.class);
        i.putExtra("chatid", chatId);
        startActivity(i);
    }

    private void openNewChat(String userid) {
        Intent i = new Intent(UserListActivity.this, ChatActivity.class);
        i.putExtra("userid", userid);
        startActivity(i);
    }
}
