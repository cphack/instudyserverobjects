package com.codepath.android.instudy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_tabs, container, false);

        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        adapter = new PagerAdapter(getFragmentManager());
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

    void refreshFragmentScreen(int screen) {
        switch(screen){
            case 0:
                CoursesTeacherFragment fragment1 = (CoursesTeacherFragment) adapter.getRegisteredFragment(screen);
                if(fragment1!=null){
                fragment1.populateCourseList();}
                break;

            case 1:
                ChatListFragment fragment = (ChatListFragment) adapter.getRegisteredFragment(1);
                if(fragment!=null){
                fragment.populateChatsList();}
                break;
            case 2:
                CoursesStudentFragment fragment2= (CoursesStudentFragment) adapter.getRegisteredFragment(screen);
                if(fragment2!=null){
                fragment2.populateCourseList();}
                break;

            case 3:
                CoursesSearchFragment fragment3= (CoursesSearchFragment) adapter.getRegisteredFragment(screen);
                if(fragment3!=null){
                fragment3.populateCourseList();}
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