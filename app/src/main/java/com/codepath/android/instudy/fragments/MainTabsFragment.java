package com.codepath.android.instudy.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.android.instudy.R;
import com.codepath.android.instudy.adapters.PagerAdapter;
import com.codepath.android.instudy.adapters.SmartFragmentStatePagerAdapter;

import java.util.ArrayList;

public class MainTabsFragment extends Fragment {
    ViewPager viewPager;
    SmartFragmentStatePagerAdapter adapter;
    Boolean isTeacherTabDisplayed = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_tabs, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);

        isTeacherTabDisplayed = showTeacherTab();


        adapter = new PagerAdapter(getFragmentManager(), isTeacherTabDisplayed);
        viewPager.setAdapter(adapter);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                refreshFragmentScreen(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        return v;
    }


    private Boolean showTeacherTab() {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        String settings1 = pref.getString("showTeacherTab", "");
        if (TextUtils.isEmpty(settings1) || settings1.equals("false")) {
            return false;
        } else {
            return true;
        }
    }

    void refreshFragmentScreen(int screen) {
        switch (screen) {
            case 0:
                if (isTeacherTabDisplayed) {
                    CoursesTeacherFragment fragment1 = (CoursesTeacherFragment) adapter.getRegisteredFragment(screen);
                    if (fragment1 != null) {
                        fragment1.populateCourseList();
                    }
                } else {
                    CoursesStudentFragment fragment2 = (CoursesStudentFragment) adapter.getRegisteredFragment(screen);
                    if (fragment2 != null) {
                        fragment2.populateCourseList();
                    }
                }
                break;


            case 1:
                if (isTeacherTabDisplayed) {
                    CoursesStudentFragment fragment2 = (CoursesStudentFragment) adapter.getRegisteredFragment(screen);
                    if (fragment2 != null) {
                        fragment2.populateCourseList();
                    }
                } else {
                    ChatListFragment fragment = (ChatListFragment) adapter.getRegisteredFragment(screen);
                    if (fragment != null) {
                        fragment.populateChatsList();
                    }
                }
                break;

            case 2:
                if (isTeacherTabDisplayed) {
                    ChatListFragment fragment = (ChatListFragment) adapter.getRegisteredFragment(screen);
                    if (fragment != null) {
                        fragment.populateChatsList();
                    }
                } else {
                    CoursesSearchFragment fragment3 = (CoursesSearchFragment) adapter.getRegisteredFragment(screen);
                    if (fragment3 != null) {
                        fragment3.populateCourseList();
                    }
                }
                break;

            case 3:
                CoursesSearchFragment fragment3 = (CoursesSearchFragment) adapter.getRegisteredFragment(screen);
                if (fragment3 != null) {
                    fragment3.populateCourseList();
                }
                break;

        }
    }

    // Define listener member variable
    private OnUserListClickListener listener;

    // Define the listener interface
    public interface OnUserListClickListener {
        void onUserListClick(ArrayList<String> userids);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnUserListClickListener(OnUserListClickListener listener) {
        this.listener = listener;
    }
}