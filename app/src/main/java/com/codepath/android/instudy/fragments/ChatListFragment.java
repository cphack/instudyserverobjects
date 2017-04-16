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
import com.codepath.android.instudy.activities.MainActivity;
import com.codepath.android.instudy.activities.UserListActivity;
import com.codepath.android.instudy.adapters.ChatListAdapter;
import com.codepath.android.instudy.adapters.CourseListAdapter;
import com.codepath.android.instudy.adapters.UserListAdapter;
import com.codepath.android.instudy.helpers.ItemClickSupport;
import com.codepath.android.instudy.models.Chat;
import com.codepath.android.instudy.models.Course;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.codepath.android.instudy.R.id.lvCourses;

public class ChatListFragment extends Fragment {
    ArrayList<Chat> chats;
    ChatListAdapter aChats;
    private RecyclerView lvChats;
    private LinearLayoutManager linearLayoutManager;

    public static ChatListFragment newInstance(int page, String title) {
        ChatListFragment fragment = new ChatListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chats_list, container, false);
        findControls(v);
        initControls();
        return v;
    }

    //creation lifecycle events
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAdapter();
        populateChatsList();
    }

    private void findControls(View v) {
        lvChats = (RecyclerView) v.findViewById(R.id.lvChats);
        linearLayoutManager = new LinearLayoutManager(getActivity());
    }

    private void initControls() {        //connect adapter with recyclerView
        lvChats.setAdapter(aChats);
        lvChats.setLayoutManager(linearLayoutManager);

        ItemClickSupport.addTo(lvChats).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i = new Intent(getActivity(), ChatActivity.class);
                Chat chat = chats.get(position);
                i.putExtra("chatid", chat.getObjectId());
                startActivity(i);
            }
        });
    }

    private void initAdapter() {
        chats = new ArrayList<Chat>();
        //construct adapter from datasource
        aChats = new ChatListAdapter(getActivity(), chats);
    }

    private void populateChatsList() {

        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        query.whereContains("recipients", ParseUser.getCurrentUser().getObjectId());

        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Chat>() {
            public void done(List<Chat> itemList, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                    int curSize = aChats.getItemCount();
                    chats.addAll(itemList);
                    // replace this line with wherever you get new records

                    //notify adapter to reflect changes
                    aChats.notifyItemRangeInserted(curSize, itemList.size());
                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

