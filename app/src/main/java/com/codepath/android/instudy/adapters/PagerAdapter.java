package com.codepath.android.instudy.adapters;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.android.instudy.R;
import com.codepath.android.instudy.fragments.CoursesSearchFragment;
import com.codepath.android.instudy.fragments.CoursesStudentFragment;
import com.codepath.android.instudy.fragments.CoursesTeacherFragment;
import com.codepath.android.instudy.fragments.ChatListFragment;


public class PagerAdapter extends SmartFragmentStatePagerAdapter
        implements PagerSlidingTabStrip.IconTabProvider {

    Boolean showTeacherTab = false;

    private int icons[] = {R.drawable.professor_tab, R.drawable.student_tab,R.drawable.mail_tab,
            R.drawable.search_tab};
    private int icons1[] = { R.drawable.student_tab,R.drawable.mail_tab,R.drawable.search_tab};


    public PagerAdapter(FragmentManager fm, Boolean showTeacherTabFlag) {
        super(fm);
        showTeacherTab = showTeacherTabFlag;
    }

    @Override
    public int getCount() {
        return showTeacherTab?4:3;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if(showTeacherTab){
                return CoursesTeacherFragment.newInstance(0, "p1");}
                else{return CoursesStudentFragment.newInstance(1, "p2");}
            case 1:
                if(showTeacherTab){
                return CoursesStudentFragment.newInstance(1, "p2");}
                else{return ChatListFragment.newInstance(2, "p3");}
            case 2:
                if(showTeacherTab){
                return ChatListFragment.newInstance(2, "p3");}
                else{return CoursesSearchFragment.newInstance(3, "p4");}
            case 3:
                if(showTeacherTab){
                return CoursesSearchFragment.newInstance(3, "p4");}
                else{return null;}
            default:
                return null;
        }
    }

    @Override
    public int getPageIconResId(int position) {

        if(showTeacherTab){
            return icons[position];
        }else
        {
            return icons1[position];
        }
    }
}
