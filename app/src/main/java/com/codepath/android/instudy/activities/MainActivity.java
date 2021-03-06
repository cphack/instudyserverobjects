package com.codepath.android.instudy.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.android.instudy.R;
import com.codepath.android.instudy.fragments.MainTabsFragment;
import com.codepath.android.instudy.fragments.MyProfile;
import com.codepath.android.instudy.fragments.SendTeacherNotificationFragment;
import com.codepath.android.instudy.models.Course;
import com.codepath.android.instudy.models.Notification;
import com.parse.ParseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SendTeacherNotificationFragment.SendNotificationListener {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private TextView tvFullName;
    private ImageView ivUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the toolbar view inside the activity layout
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);
        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        View headerView = nvDrawer.inflateHeaderView(R.layout.nav_header);

        tvFullName = (TextView) headerView.findViewById(R.id.tvFullName);
        ivUser = (ImageView) headerView.findViewById(R.id.ivUser);

        loadUserProfile();

        // Setup drawer view
        setupDrawerContent(nvDrawer);

        FragmentManager fragmentManager = getSupportFragmentManager();
        MainTabsFragment fragment = new MainTabsFragment();

        fragment.setOnUserListClickListener(new MainTabsFragment.OnUserListClickListener() {
            @Override
            public void onUserListClick(ArrayList<String> userids) {
                openUserList(userids);
            }
        });
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    private void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        Intent i = null;
        switch (menuItem.getItemId()) {
            case R.id.dvLogOut:
                logout();
                i = new Intent(MainActivity.this, LoginActivity.class);
                break;
         /*   case R.id.dvTest:
                runTest();
                break;*/
            case R.id.dvAddNewCourse:
                i = new Intent(MainActivity.this, NewCourseActivity.class);
                break;

            case R.id.dvSettings:
                i = new Intent(MainActivity.this, SettingsActivity.class);
                break;


            case R.id.dvMyProfile:
                i = new Intent(MainActivity.this, MyProfileActivity.class);
                break;


            case R.id.dvFriends:
                i = new Intent(MainActivity.this, UserListActivity.class);
                break;

          /*  case R.id.nav_third_fragment:
                Log.d("DEBUG", "Got to Groups");
                fragmentClass = Groups.class;
                break;*/
            default:
                Toast.makeText(this, String.valueOf(menuItem.getItemId()), Toast.LENGTH_SHORT).show();
                fragmentClass = MyProfile.class;
        }
        if (i != null) {
            startActivity(i);
            mDrawer.closeDrawers();

        }

       /* try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

       /* if (fragment != null) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            // Highlight the selected item has been done by NavigationView
            menuItem.setChecked(true);
            // Set action bar title
            setAssignment(menuItem.getAssignment());
            // Close the navigation drawer
            mDrawer.closeDrawers();
        }*/
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case R.id.miNotification:
                openNotifications();
                return true;
        }
        return super.onOptionsItemSelected(item);
        /*miNotification*/


       /*

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);*/
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public void openUserList(ArrayList<String> userids) {
        String[] users = new String[userids.size()];
        userids.toArray(users);
        Intent i = new Intent(MainActivity.this, UserListActivity.class);
        i.putExtra("users", users);
        startActivity(i);
    }

    private void loadUserProfile() {
        if (ParseUser.getCurrentUser() != null) {
            ParseUser user = ParseUser.getCurrentUser();
            String fullName = user.getString("FullName");
            String profileImage = user.getString("ProfileImage");
            Log.d("DEBUG", "LoadUserProfile user " + fullName + " ProfImg " + profileImage);
            if (tvFullName != null) {
                if (ParseUser.getCurrentUser() != null) {
                    tvFullName.setText(fullName);
                }
            }

            if (ivUser != null) {
                {
                    Glide.with(this).load(profileImage).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivUser) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivUser.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
            }
        }
    }

    private void logout() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            currentUser.logOut();
        }
    }

    private void runTest() {

        Notification course = new Notification();
        course.setTitle("The next course will start in 30 min in Campus A");
        course.setType("univ");

        course.saveInBackground();


        course = new Notification();
        course.setTitle("You have only 4 days left to complete assignment for CSE 142");
        course.setType("univ");

        course.saveInBackground();

        course = new Notification();
        course.setTitle("Opportunity for internship in Google");
        course.setDescrip("Ready to apply for an internship or a full-time job at Google? Here is what you’ll need: 1)Resume, 2)University transcript, 3)Cover letter");
        course.setType("work");
        course.setLink("https://careers.google.com/students/");
        course.saveInBackground();



       /* course = new Course();
        course.setAssignment("CSE 154 Web Programming (5) QSR");
        course.setDescription("Covers languages, tools, and techniques for developing interactive and dynamic web pages. Topics include page styling, design, and layout; client and server side scripting; web security; and interacting with data sources such as databases. Prerequisite: minimum grade of 2.0 in either CSE 142, CSE 143, or CSE 160.");
        course.setTeachers(ParseUser.getCurrentUser().getObjectId());
        course.saveInBackground();

        course = new Course();
        course.setAssignment("CSE 160 Data Programming (4) NW, QSR");
        course.setDescription("Introduction to computer programming. Assignments solve real data manipulation tasks from science, engineering, business, and the humanities. Concepts of computational thinking, problem-solving, data analysis, Python programming, control and data abstraction, file processing, and data visualization. Intended for students without prior programming experience. No credit if CSE 143 has been taken.");
        course.setTeachers(ParseUser.getCurrentUser().getObjectId());
        course.saveInBackground();

        course = new Course();
        course.setAssignment("CSE 190 Current Topics in Computer Science and Engineering (1-5, max. 15)");
        course.setDescription("View course details in MyPlan: CSE 190");
        course.setTeachers(ParseUser.getCurrentUser().getObjectId());
        course.saveInBackground();

        course = new Course();
        course.setAssignment("CSE 301 CSE Internship Education (1-2, max. 12)");
        course.setDescription("CSE Internship practicum; integration of classroom theory with on-the-job training. Periods of full-time work alternate with periods of full-time study. Open only to students who have been admitted to CSE Internship Program or by special permission of the Department. Offered credit/no credit only. Credit/no-credit only. Offered: AWSpS.");
        course.setTeachers(ParseUser.getCurrentUser().getObjectId());
        course.saveInBackground();

        course = new Course();
        course.setAssignment("CSE 311 Foundations of Computing I (4) QSR");
        course.setDescription("Examines fundamentals of logic, set theory, induction, and algebraic structures with applications to computing; finite state machines; and limits of computability. Prerequisite: CSE 143; either MATH 126 or MATH 136.");
        course.setTeachers(ParseUser.getCurrentUser().getObjectId());
        course.saveInBackground();

        course = new Course();
        course.setAssignment("CSE 312 Foundations of Computing II (4) QSR");
        course.setDescription("Examines fundamentals of enumeration and discrete probability; applications of randomness to computing; polynomial-time versus NP; and NP-completeness. Prerequisite: CSE 311.");
        course.setTeachers(ParseUser.getCurrentUser().getObjectId());
        course.saveInBackground();*/



    }

    private void runTest2(){


    }

    public void openLectionsOverview(String courseId, String courseTitle) {
        Intent i = new Intent(MainActivity.this, LectionsListActivity.class);
        i.putExtra("courseid", courseId);
        i.putExtra("courseTitle", courseTitle);
        startActivity(i);
    }

    public void openManageCourse(String courseId) {
    }

    public void openSendNotification(String courseId) {
        FragmentManager fm = getSupportFragmentManager();
        SendTeacherNotificationFragment fragment = SendTeacherNotificationFragment.newInstance(courseId);
        fragment.show(fm, "fragment_send_notification");
    }

    public void openCourseOverview(String courseid) {
        Intent i = new Intent(MainActivity.this, OverviewCourseActivity.class);
        i.putExtra("courseid", courseid);
        startActivity(i);
    }

    public void openCourseChatActivity(String chatid) {
        Intent i = new Intent(MainActivity.this, ChatActivity.class);
        i.putExtra("chatid", chatid);
        startActivity(i);
    }


    public void openLectionList(String courseid, String courseTitle) {

        Intent i = new Intent(MainActivity.this, LectionsListActivity.class);
        i.putExtra("courseid", courseid);
        i.putExtra("courseTitle", courseTitle);
        startActivity(i);
    }

    @Override
    public void onFinishEditDialog(String message, String courseid) {

        /*
        TODO get Course by courseid
        send message to all users which attends course
        create message in main group chat
        * */
    }


    public void openAssignmentsList(String courseid, String courseTitle) {
        Intent i = new Intent(MainActivity.this, AssignmentListActivity.class);
        i.putExtra("courseid", courseid);
        i.putExtra("courseTitle", courseTitle);
        startActivity(i);
    }

    private void openNotifications(){
        Intent i  = new Intent(MainActivity.this,NotificationListActivity.class);
        startActivity(i);
    }


}
