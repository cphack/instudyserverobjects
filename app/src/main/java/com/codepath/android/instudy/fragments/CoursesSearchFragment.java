package com.codepath.android.instudy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.activities.MainActivity;
import com.codepath.android.instudy.adapters.CourseListAdapter;
import com.codepath.android.instudy.models.Course;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.id.message;

public class CoursesSearchFragment extends BaseCoursesFragment {

    private RecyclerView lvCourses;
    private LinearLayoutManager linearLayoutManager;

    public static CoursesSearchFragment newInstance(int page, String title) {
        CoursesSearchFragment fragment = new CoursesSearchFragment();
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
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    activity.openUserList(userids);
                }
            }

            @Override
            public void onCourseSearchOverviewClick(String courseid) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    activity.openCourseOverview(courseid);
                }
            }

            @Override
            public void onCourseSearchApplyClick(String courseid) {

                // Specify which class to query
                ParseQuery<Course> query = ParseQuery.getQuery(Course.class);
                // Specify the object id
                query.getInBackground(courseid, new GetCallback<Course>() {
                    public void done(Course item, ParseException e) {
                        if (e == null) {
                            // Access data using the `get` methods for the object
                            ArrayList students = item.getStudents();
                            if(!students.contains(ParseUser.getCurrentUser().getObjectId())){
                                students.add(ParseUser.getCurrentUser().getObjectId());
                                item.setStudents(students);

                                item.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            populateCourseList();
                                            Toast.makeText(getActivity(), "You have successfully registered for this course. Thank you!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                        } else {
                            // something went wrong
                        }
                    }
                });



            }

            @Override
            public void onCourseStudentChatClick(String courseid){

            }

            @Override
            public void onCourseTeacherAssignmentsClick(String courseid) {}
            @Override
            public void onCourseStudentLectionsClick(String courseid) {            }
            @Override
            public void onCourseStudentSubmitClick(String courseid) {            }
            @Override
            public void onCourseTeacherLectionsClick(String courseid) {            }
            @Override
            public void onCourseTeacherManageClick(String courseid) {            }
            @Override
            public void onCourseTeacherNotificationClick(String courseid) {            }
        });
    }

    public void populateCourseList() {

        List<String> users = new ArrayList<>();
        users.add(ParseUser.getCurrentUser().getObjectId());
        ParseQuery<Course> query = ParseQuery.getQuery(Course.class);
        query.whereNotContainedIn("students", users);

        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Course>() {
            public void done(List<Course> itemList, ParseException e) {
                if (e == null) {
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

