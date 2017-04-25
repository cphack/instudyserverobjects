package com.codepath.android.instudy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.activities.ChatActivity;
import com.codepath.android.instudy.adapters.ChatListAdapter;
import com.codepath.android.instudy.adapters.EventListAdapter;
import com.codepath.android.instudy.helpers.ItemClickSupport;
import com.codepath.android.instudy.models.Chat;
import com.codepath.android.instudy.models.Event;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.codepath.android.instudy.R.id.lvChats;

public class TimelineFragment extends Fragment {

    ArrayList<Event> events;
    EventListAdapter aEvents;
    private RecyclerView lvTimeline;
    private LinearLayoutManager linearLayoutManager;

    public TimelineFragment(){}

    public static TimelineFragment newInstance() {
        TimelineFragment fragment = new TimelineFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timeline, container, false);
        findControls(v);
        initControls();
        return v;
    }

    //creation lifecycle events
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAdapter();
    }

    private void findControls(View v) {
        lvTimeline = (RecyclerView) v.findViewById(R.id.lvTimeline);
        linearLayoutManager = new LinearLayoutManager(getActivity());
    }

    private void initControls() {        //connect adapter with recyclerView
        lvTimeline.setAdapter(aEvents);
        lvTimeline.setLayoutManager(linearLayoutManager);


    }

    private void initAdapter() {
        events = new ArrayList<Event>();
        //construct adapter from datasource
        aEvents = new EventListAdapter(getActivity(), events);
    }

    public void populateTimeline(String userid) {
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        query.whereContains(Event.USER_ID_KEY, userid);
        query.orderByDescending(Event.START_DATE_KEY);
        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Event>() {
            public void done(List<Event> itemList, ParseException e) {
                if (e == null) {
                    events.clear();
                    events.addAll(itemList);
                    aEvents.notifyDataSetChanged();

                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

