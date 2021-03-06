package com.codepath.android.instudy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.activities.MainActivity;
import com.codepath.android.instudy.adapters.CourseListAdapter;
import com.codepath.android.instudy.models.Assignment;
import com.codepath.android.instudy.models.Assignment_User;
import com.codepath.android.instudy.models.Chat;
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
        /*fabReturn = (FloatingActionButton) v.findViewById(R.id.fabReturn);
        fabReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = null;
                i = new Intent(getContext(), MainActivity.class);
                if (i != null) {
                    getContext().startActivity(i);
                }
                ;
            }
        });*/
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
        aCourses.setOnUserListClickListener(new CourseListAdapter.OnUserClickListListener() {
            @Override
            public void onUserListClick(ArrayList<String> userids) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    activity.openUserList(userids);
                }
            }

            @Override
            public void onCourseTeacherLectionsClick(String courseid, String courseTitle) {
            }

            @Override
            public void onCourseTeacherManageClick(String courseid) {
            }

            @Override
            public void onCourseTeacherNotificationClick(String courseid) {
            }

            @Override
            public void onCourseSearchOverviewClick(String courseid) {
            }

            @Override
            public void onCourseSearchApplyClick(String courseid) {}

            @Override
            public void onCourseStudentLectionsClick(String courseid, String courseTitle) {
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    activity.openLectionList(courseid, courseTitle);
                }
            }

            @Override
            public void onCourseStudentSubmitClick(String courseid) {
                ParseQuery<Assignment> aquery = ParseQuery.getQuery(Assignment.class);
                aquery.whereEqualTo(Assignment.COURSE_ID_KEY, courseid);
                aquery.whereGreaterThan(Assignment.DUE_DATE_KEY, new Date());
                aquery.findInBackground(new FindCallback<Assignment>() {
                    public void done(List<Assignment> assigns, ParseException e) {
                        if (e == null) {
                            if (assigns.size()>0) {
                                final Assignment assignment = assigns.get(0);
                                Assignment_User au = new Assignment_User();
                                au.setUserId(ParseUser.getCurrentUser().getObjectId());
                                au.setAssignment(assignment.getObjectId());
                                au.setSubmit(true);
                                au.saveInBackground();
                                Toast.makeText(getContext(),"Thank you, this assignment was submitted!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }

            @Override
            public void onCourseTeacherAssignmentsClick(String courseid, String courseTitle) {}

            @Override
            public void onCourseStudentChatClick(String courseid) {

                //get chatid based on courseidif doesn't exist create one'
                //and return chatid
                ParseQuery<Course> query = ParseQuery.getQuery(Course.class);

                query.getInBackground(courseid, new GetCallback<Course>() {
                    public void done(final Course course, ParseException e) {
                        if (e == null) {
                            // Access data using the `get` methods for the object
                            String chatid = course.getChatId();
                            if (chatid == null) {
                                //create chatid and return

                                final Chat chat = new Chat();
                                ArrayList<String> recipients = new ArrayList<String>();
                                recipients.addAll(course.getStudents());
                                recipients.add(course.getTeachers());
                                chat.setRecipients(recipients);
                                chat.setLastDate(new Date());
                                chat.setChatName(String.format("Chat: %s", course.getTitle()));
                                chat.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            course.setChatId(chat.getObjectId());
                                            course.saveInBackground();
                                            MainActivity activity = (MainActivity) getActivity();
                                            if (activity != null) {
                                                activity.openCourseChatActivity(chat.getObjectId());
                                            }
                                        }
                                    }
                                });
                            } else {
                                MainActivity activity = (MainActivity) getActivity();
                                if (activity != null) {
                                    activity.openCourseChatActivity(chatid);
                                }
                            }
                        } else {
                            // something went wrong
                        }
                    }
                });
            }
        });
    }

    public void populateCourseList() {
        ParseQuery<Course> query = ParseQuery.getQuery(Course.class);
        query.whereContains("students", ParseUser.getCurrentUser().getObjectId());
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

