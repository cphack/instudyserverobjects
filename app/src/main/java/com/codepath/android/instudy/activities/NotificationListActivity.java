package com.codepath.android.instudy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.adapters.ChatListAdapter;
import com.codepath.android.instudy.adapters.NotificationListAdapter;
import com.codepath.android.instudy.helpers.ItemClickSupport;
import com.codepath.android.instudy.models.Chat;
import com.codepath.android.instudy.models.Notification;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.codepath.android.instudy.R.id.lvChats;


public class NotificationListActivity extends AppCompatActivity {


    ArrayList<Notification> notes;
    NotificationListAdapter aNotes;
    private RecyclerView lvNotifications;
    private LinearLayoutManager linearLayoutManager;
    private String cTitle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notifications");

        lvNotifications = (RecyclerView) findViewById(R.id.lvNotifications);
        linearLayoutManager = new LinearLayoutManager(this);

        notes = new ArrayList<Notification>();
        //construct adapter from datasource
        aNotes = new NotificationListAdapter(this, notes);

        lvNotifications.setAdapter(aNotes);
        lvNotifications.setLayoutManager(linearLayoutManager);

       /* ItemClickSupport.addTo(lvChats).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i = new Intent(getActivity(), ChatActivity.class);
                Chat chat = chats.get(position);
                i.putExtra("chatid", chat.getObjectId());
                startActivity(i);
            }
        });*/



        populateList();

    }



    public void populateList() {
        ParseQuery<Notification> query = ParseQuery.getQuery(Notification.class);
        query.orderByAscending("updatedAt");
        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Notification>() {
            public void done(List<Notification> itemList, ParseException e) {
                if (e == null) {
                    notes.clear();
                    notes.addAll(itemList);
                    aNotes.notifyDataSetChanged();

                } else {
                    Toast.makeText(NotificationListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
