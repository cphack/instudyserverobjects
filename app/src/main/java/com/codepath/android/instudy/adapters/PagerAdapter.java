package com.codepath.android.instudy.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.android.instudy.R;
import com.codepath.android.instudy.fragments.CoursesSearchFragment;
import com.codepath.android.instudy.fragments.CoursesStudentFragment;
import com.codepath.android.instudy.fragments.CoursesTeacherFragment;
import com.codepath.android.instudy.fragments.MessagesListFragment;


public class PagerAdapter extends SmartFragmentStatePagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

   // private String tabTitles[] = new String[]{"Courses as Teacher", "Courses as Student", "Messages"};
    private int icons[] = {R.drawable.professor_tab, R.drawable.mail_tab, R.drawable.student_tab,R.drawable.search_tab};

    public PagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return CoursesTeacherFragment.newInstance(0, "Page # 1");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return MessagesListFragment.newInstance(1, "Page # 2");
            case 2: // Fragment # 1 - This will show SecondFragment
                return CoursesStudentFragment.newInstance(2, "Page # 3");
            case 3: // Fragment # 1 - This will show SecondFragment
                return CoursesSearchFragment.newInstance(3, "Page # 3");
            default:
                return null;
        }
    }

    @Override
    public int getPageIconResId(int position) {
        return icons[position];
    }

}
