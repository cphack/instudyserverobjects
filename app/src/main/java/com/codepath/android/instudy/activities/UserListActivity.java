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
import com.codepath.android.instudy.models.Chat;
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
import static com.codepath.android.instudy.R.id.start;
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
                 String id= users.get(position).getObjectId();
                openUserProfile(id);
            }
        });
        aUsers.setOnChatButtonClickListener(new UserListAdapter.OnChatButtonClickListener() {
            @Override
            public void onChatButtonClick(View view, int position) {
                String id= users.get(position).getObjectId();
                openChat(id);
            }
        });
    }

    //TODO:move to fragment
    private void populateUsers() {
        if (userids.length == 0) return;
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

    private void openUserProfile(String userid) {
        Intent i = new Intent(UserListActivity.this,UserProfileActivity.class);
        i.putExtra("userid",userid);
        startActivity(i);
    }

    private void openChat(final String userid) {
        //check if i don't have already chats with this user

        //if there is chat where recepients only i and this person - open this chat
        //otherrwise create new chat

        //open ChatActivity
        ArrayList<String> recipients = new ArrayList<>();
        recipients.add(userid);
        recipients.add(ParseUser.getCurrentUser().getObjectId());
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Chat");
        query.whereContainsAll(Chat.RECIPIENTS_KEY, recipients);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() == 1) {
                        openExistingChat(objects.get(0).getObjectId());
                    } else if (objects.size() == 0) {
                        openNewChat(userid);
                    }
                }
            }
        });
    }

    private void openExistingChat(String chatId) {
        Intent i = new Intent(UserListActivity.this,ChatActivity.class);
        i.putExtra("chatid",chatId);
        startActivity(i);
    }

    private void openNewChat(String userid) {
        Intent i = new Intent(UserListActivity.this,ChatActivity.class);
        i.putExtra("userid",userid);
        startActivity(i);
    }
}
