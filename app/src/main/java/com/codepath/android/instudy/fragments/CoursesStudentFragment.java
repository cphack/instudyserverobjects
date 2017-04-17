package com.codepath.android.instudy.fragments;
import android.content.SharedPreferences;


import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.activities.MainActivity;
import com.codepath.android.instudy.adapters.CourseListAdapter;
import com.codepath.android.instudy.models.Course;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CoursesStudentFragment extends BaseCoursesFragment {

    ArrayList<Course> courses;
    CourseListAdapter aCourses;

    private RecyclerView lvCourses;
    private LinearLayoutManager linearLayoutManager;

    private FloatingActionButton fabReturn;

    public static CoursesStudentFragment newInstance(int page, String title) {
        CoursesStudentFragment fragment = new CoursesStudentFragment();
     /*   Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);*/
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_coursesstudent, container, false);
        fabReturn = (FloatingActionButton) v.findViewById(R.id.fabReturn);
        fabReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = null;
                i = new Intent(getContext(), MainActivity.class);
                if(i!=null){
                    getContext().startActivity(i);
                };
            }
        });
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
        aCourses = new CourseListAdapter(getActivity(), courses, "STU");
    }

    public void populateCourseList() {
        ParseQuery<Course> query = ParseQuery.getQuery(Course.class);
        query.whereContains("students", ParseUser.getCurrentUser().getObjectId()) ;
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

}

