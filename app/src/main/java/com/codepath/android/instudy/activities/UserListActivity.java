package com.codepath.android.instudy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.adapters.UserListAdapter;
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
    String[] userids;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayoutManager = new LinearLayoutManager(this);

        lvUsers = (RecyclerView) findViewById(R.id.lvUsers);

        initAdapter();
        lvUsers.setAdapter(aUsers);
        lvUsers.setLayoutManager(linearLayoutManager);

        userids = getIntent().getStringArrayExtra("users");

        if(userids==null){

            ParseQuery<Course> query = new ParseQuery<Course>("Course");
            query.whereContains("students", ParseUser.getCurrentUser().getObjectId());

            query.findInBackground(new FindCallback<Course>() {
                public void done(List<Course> itemList, ParseException e) {
                    if (e == null) {
                        ArrayList<String> users = new  ArrayList();
                       for(Course c:itemList){
                           ArrayList<String> students = c.getStudents();
                           for(String student : students){
                               if(student.equals(ParseUser.getCurrentUser().getObjectId())){
                                   continue;
                               }
                               if(!users.contains(student)){
                                   users.add(student);
                               }
                           }
                       }
                       userids = users.toArray(new String[users.size()]);
                        populateUsers();
                    } else {
                        Toast.makeText(UserListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            populateUsers();
        }



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
