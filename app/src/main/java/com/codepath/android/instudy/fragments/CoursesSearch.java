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
import com.codepath.android.instudy.adapters.CourseListAdapter;
import com.codepath.android.instudy.models.Course;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class CoursesSearch extends Fragment implements CourseListAdapter.CourseListListener {

    ArrayList<Course> courses;
    CourseListAdapter aCourses;

    private RecyclerView lvCourses;
    private LinearLayoutManager linearLayoutManager;

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
    }

    private void populateCourseList() {

        List<String> users = new ArrayList<>();
        users.add( ParseUser.getCurrentUser().getObjectId());
        ParseQuery<Course> query = ParseQuery.getQuery(Course.class);
        query.whereNotContainedIn("students",users);

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

    @Override
    public void onCourseApply(Course course) {
        List<String> students = course.getStudents();
        if(students==null){
            students=new ArrayList<>();
        }
        if(!students.contains(ParseUser.getCurrentUser().getObjectId())){
            students.add(ParseUser.getCurrentUser().getObjectId());
        }
        course.saveInBackground();
        Toast.makeText(getActivity(), "You successfully registered on this course", Toast.LENGTH_SHORT).show();
        courses.remove(course);
        aCourses.notifyDataSetChanged();
    }
}

