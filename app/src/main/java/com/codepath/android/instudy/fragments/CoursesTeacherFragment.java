package com.codepath.android.instudy.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class CoursesTeacherFragment extends BaseCoursesFragment {

    private RecyclerView lvCourses;
    private LinearLayoutManager linearLayoutManager;


    public static CoursesTeacherFragment newInstance(int page, String title) {
        CoursesTeacherFragment fragment = new CoursesTeacherFragment();
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
        aCourses = new CourseListAdapter(getActivity(), courses, "TEA");
        aCourses.setOnUserListClickListener(new CourseListAdapter.OnUserClickListListener() {
            @Override
            public void onUserListClick(ArrayList<String> userids) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    activity.openUserList(userids);
                }
            }


            @Override
            public void onCourseStudentChatClick(String courseid){

            }
            @Override
            public void onCourseSearchOverviewClick(String courseid) {}
            @Override
            public void onCourseSearchApplyClick(String courseid) {}

            @Override
            public void onCourseStudentLectionsClick(String courseid){}
            @Override
            public void onCourseStudentSubmitClick(String courseid){}


            @Override
            public void onCourseTeacherLectionsClick(String courseid) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    activity.openLectionsOverview(courseid);
                }
            }

            @Override
            public void onCourseTeacherAssignmentsClick(String courseid) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    activity.openAssignmentsList(courseid);
                }
            }

            @Override
            public void onCourseTeacherManageClick(String courseid) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    activity.openManageCourse(courseid);
                }
            }

            @Override
            public void onCourseTeacherNotificationClick(String courseid) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    activity.openSendNotification(courseid);
                }
            }
        });
    }






    public void populateCourseList() {
        ParseQuery<Course> query = ParseQuery.getQuery(Course.class);
        query.whereEqualTo("teachers", ParseUser.getCurrentUser().getObjectId());
        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Course>() {
            public void done(List<Course> itemList, ParseException e) {
                if (e == null) {
                    // Access the array of results here
                    courses.clear();
                    courses.addAll(itemList);
                    lvCourses.getRecycledViewPool().clear();
                    aCourses.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
