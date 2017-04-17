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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.android.instudy.R;
import com.codepath.android.instudy.fragments.ChatListFragment;
import com.codepath.android.instudy.fragments.MainTabsFragment;
import com.codepath.android.instudy.fragments.MyProfile;
import com.parse.ParseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
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
        ivUser = (ImageView)  headerView.findViewById(R.id.ivUser);
        Log.d("DEBUG","Going to LoadUserProfile");
        loadUserProfile();
        Log.d("DEBUG","Done LoadUserProfile");
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        FragmentManager fragmentManager = getSupportFragmentManager();
        MainTabsFragment fragment = new MainTabsFragment();

        fragment.setOnUserListClickListener(new MainTabsFragment.OnUserListClickListener() {
            @Override
            public void onUserListClick(ArrayList<String> userids) {
                Log.d("DEBUG","Trigg onUserListClick Listener");
                openUserList(userids);
            }
        });
        fragmentManager.beginTransaction().replace(R.id.flContent,fragment).commit();
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
            case R.id.dvAddNewCourse:
                i = new Intent(MainActivity.this, NewCourseActivity.class);
                break;

            case R.id.dvMyProfile:
                i = new Intent(MainActivity.this, MyProfileActivity.class);
                break;

            case R.id.dvMessages :
                i = new Intent(MainActivity.this, ChatListActivity.class);
                break;

           /* case R.id.dvTest:
               runTest();
                break;*/


          /*  case R.id.nav_third_fragment:
                Log.d("DEBUG", "Got to Groups");
                fragmentClass = Groups.class;
                break;*/
            default:

                fragmentClass = MyProfile.class;
        }
        if(i!=null){
            startActivity(i);
            finish();
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
            setTitle(menuItem.getTitle());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        /* // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
        */
        //We also need to change the onOptionsItemSelected() method
        // and allow the ActionBarToggle to handle the events.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    public void openUserList(ArrayList<String> userids){
        String[] users =new String[userids.size()];
        Log.d("DEBUG","OpenUsers number : "+users.length);
        userids.toArray(users);
        Intent i  = new Intent(MainActivity.this,UserListActivity.class);
        i.putExtra("users",users);
        startActivity(i);
    }



    private void openTeacherFragment(){

    }

    private void openCourseDetails(){

    }

    private void loadUserProfile(){
        if (ParseUser.getCurrentUser() != null) {
            ParseUser user =ParseUser.getCurrentUser();
            String fullName = user.getString("FullName");
            String profileImage = user.getString("ProfileImage");
            Log.d("DEBUG","LoadUserProfile user "+fullName+" ProfImg "+profileImage);
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

    private void runTest(){
        ParseUser user = ParseUser.getCurrentUser();
        user.put("ProfileImage","http://i.imgur.com/DGTOSfL.png");
        user.saveInBackground();
       /* Course course = new Course();
        course.setTitle("CSE 120 Computer Science Principles (5) NW, QSR");
        course.setDescription("Introduces fundamental concepts of computer science and computational thinking. Includes logical reasoning, problem solving, data representation, abstraction, the creation of digital artifacts such as web pages and programs, managing complexity, operation of computers and networks, effective web searching, ethical, legal and social aspects of information technology. May not be taken for credit if credit earned in CSE 100/INFO 100.");
        course.setTeachers(ParseUser.getCurrentUser().getObjectId());
        course.saveInBackground();


        course = new Course();
        course.setTitle("CSE 131 Science and Art of Digital Photography (4) VLPA Hemingway");
        course.setDescription("Covers the fundamentals of digital photography, including computational imaging; the elements of photographic composition and design; and the future of internet-enabled photography.");
        course.setTeachers(ParseUser.getCurrentUser().getObjectId());
        course.saveInBackground();

        course = new Course();
        course.setTitle("CSE 142 Computer Programming I (4) NW, QSR");
        course.setDescription("Basic programming-in-the-small abilities and concepts including procedural programming (methods, parameters, return, values), basic control structures (sequence, if/else, for loop, while loop), file processing, arrays, and an introduction to defining objects. Intended for students without prior programming experience. Offered: AWSpS.");
        course.setTeachers(ParseUser.getCurrentUser().getObjectId());
        course.saveInBackground();

        course = new Course();
        course.setTitle("CSE 143 Computer Programming II (5) NW, QSR");
        course.setDescription("Continuation of CSE 142. Concepts of data abstraction and encapsulation including stacks, queues, linked lists, binary trees, recursion, instruction to complexity and use of predefined collection classes. Prerequisite: CSE 142. Offered: AWSpS.");
        course.setTeachers(ParseUser.getCurrentUser().getObjectId());
        course.saveInBackground();

        course = new Course();
        course.setTitle("CSE 154 Web Programming (5) QSR");
        course.setDescription("Covers languages, tools, and techniques for developing interactive and dynamic web pages. Topics include page styling, design, and layout; client and server side scripting; web security; and interacting with data sources such as databases. Prerequisite: minimum grade of 2.0 in either CSE 142, CSE 143, or CSE 160.");
        course.setTeachers(ParseUser.getCurrentUser().getObjectId());
        course.saveInBackground();

        course = new Course();
        course.setTitle("CSE 160 Data Programming (4) NW, QSR");
        course.setDescription("Introduction to computer programming. Assignments solve real data manipulation tasks from science, engineering, business, and the humanities. Concepts of computational thinking, problem-solving, data analysis, Python programming, control and data abstraction, file processing, and data visualization. Intended for students without prior programming experience. No credit if CSE 143 has been taken.");
        course.setTeachers(ParseUser.getCurrentUser().getObjectId());
        course.saveInBackground();

        course = new Course();
        course.setTitle("CSE 190 Current Topics in Computer Science and Engineering (1-5, max. 15)");
        course.setDescription("View course details in MyPlan: CSE 190");
        course.setTeachers(ParseUser.getCurrentUser().getObjectId());
        course.saveInBackground();

        course = new Course();
        course.setTitle("CSE 301 CSE Internship Education (1-2, max. 12)");
        course.setDescription("CSE Internship practicum; integration of classroom theory with on-the-job training. Periods of full-time work alternate with periods of full-time study. Open only to students who have been admitted to CSE Internship Program or by special permission of the Department. Offered credit/no credit only. Credit/no-credit only. Offered: AWSpS.");
        course.setTeachers(ParseUser.getCurrentUser().getObjectId());
        course.saveInBackground();

        course = new Course();
        course.setTitle("CSE 311 Foundations of Computing I (4) QSR");
        course.setDescription("Examines fundamentals of logic, set theory, induction, and algebraic structures with applications to computing; finite state machines; and limits of computability. Prerequisite: CSE 143; either MATH 126 or MATH 136.");
        course.setTeachers(ParseUser.getCurrentUser().getObjectId());
        course.saveInBackground();

        course = new Course();
        course.setTitle("CSE 312 Foundations of Computing II (4) QSR");
        course.setDescription("Examines fundamentals of enumeration and discrete probability; applications of randomness to computing; polynomial-time versus NP; and NP-completeness. Prerequisite: CSE 311.");
        course.setTeachers(ParseUser.getCurrentUser().getObjectId());
        course.saveInBackground();*/
    }




}
