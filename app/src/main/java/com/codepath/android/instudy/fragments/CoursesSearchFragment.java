package com.codepath.android.instudy.fragments;


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
import com.codepath.android.instudy.activities.UserListActivity;
import com.codepath.android.instudy.adapters.CourseListAdapter;
import com.codepath.android.instudy.adapters.UserListAdapter;
import com.codepath.android.instudy.models.Course;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class CoursesSearchFragment extends BaseCoursesFragment {

    ArrayList<Course> courses;
    CourseListAdapter aCourses;

    private RecyclerView lvCourses;
    private LinearLayoutManager linearLayoutManager;

    public static CoursesSearchFragment newInstance(int page, String title) {
        CoursesSearchFragment fragment = new CoursesSearchFragment();
     /*   Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);*/
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_coursesteacher, container, false);
        findControls(v);
        initControls();
        return v;
    }

    //creation lifecycle events
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAdapter();
        populateCourseList();
    }

    private void findControls(View v) {

        lvCourses = (RecyclerView) v.findViewById(R.id.lvCourses);
        linearLayoutManager = new LinearLayoutManager(getActivity());
    }

    private void initControls() {        //connect adapter with recyclerView
        lvCourses.setAdapter(aCourses);
        lvCourses.setLayoutManager(linearLayoutManager);
    }

    private void initAdapter() {
        courses = new ArrayList<Course>();
        //construct adapter from datasource
        aCourses = new CourseListAdapter(getActivity(), courses, "SEA");
        aCourses.setOnUserListClickListener(new CourseListAdapter.OnUserClickListListener() {
            @Override
            public void onUserListClick(ArrayList<String> userids) {
                if (userListener != null) {
                    userListener.onUserListClick(userids);
                }
            }
        });
    }

    private void populateCourseList() {

        List<String> users = new ArrayList<>();
        users.add(ParseUser.getCurrentUser().getObjectId());
        ParseQuery<Course> query = ParseQuery.getQuery(Course.class);
        query.whereNotContainedIn("students", users);

        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Course>() {
            public void done(List<Course> itemList, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                    int curSize = aCourses.getItemCount();
                    courses.addAll(itemList);
                    // replace this line with wherever you get new records

                    //notify adapter to reflect changes
                    aCourses.notifyItemRangeInserted(curSize, itemList.size());
                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    // Define listener member variable
    private OnUserClickListListener userListener;

    // Define the listener interface
    public interface OnUserClickListListener {
        void onUserListClick(ArrayList<String> userids);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnUserListClickListener(OnUserClickListListener listener) {
        this.userListener = listener;
    }
}

