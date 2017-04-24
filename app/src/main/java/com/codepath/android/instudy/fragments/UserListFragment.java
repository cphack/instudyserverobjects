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
import com.codepath.android.instudy.activities.UserListActivity;
import com.codepath.android.instudy.adapters.ChatListAdapter;
import com.codepath.android.instudy.adapters.UserListAdapter;
import com.codepath.android.instudy.helpers.ItemClickSupport;
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

import static com.codepath.android.instudy.R.id.lvChats;

public class UserListFragment extends Fragment {

    RecyclerView lvUsers;
    UserListAdapter aUsers;
    ArrayList<ParseObject> users;
    String[] userids;
    private LinearLayoutManager linearLayoutManager;

    public static UserListFragment newInstance(int page, String title) {
        UserListFragment fragment = new UserListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_users_list, container, false);
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
        lvUsers = (RecyclerView) v.findViewById(R.id.lvUsers);
        linearLayoutManager = new LinearLayoutManager(getActivity());
    }

    private void initControls() {        //connect adapter with recyclerView
        lvUsers.setAdapter(aUsers);
        lvUsers.setLayoutManager(linearLayoutManager);

        ItemClickSupport.addTo(lvUsers).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent i = new Intent(getActivity(), ChatActivity.class);
                ParseObject user = users.get(position);
                i.putExtra("userid", user.getObjectId());
                startActivity(i);
            }
        });
    }

    private void initAdapter() {
        users = new ArrayList<ParseObject>();
        //construct adapter from datasource
        aUsers = new UserListAdapter(getActivity(), users);

        aUsers.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id= users.get(position).getObjectId();
                //openUserProfile(id);
            }
        });
        aUsers.setOnChatButtonClickListener(new UserListAdapter.OnChatButtonClickListener() {
            @Override
            public void onChatButtonClick(View view, int position) {
                String id= users.get(position).getObjectId();
                //openChat(id);
            }
        });
    }


    //TODO:move to fragment
    public void populateUserList(String[] userids) {
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
                        String[] userids = users.toArray(new String[users.size()]);
                        populateUsers(userids);
                    } else {

                    }
                }
            });
        }else{
            populateUsers(userids);
        }








    }

    private void populateUsers(String[] userids){
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
}

