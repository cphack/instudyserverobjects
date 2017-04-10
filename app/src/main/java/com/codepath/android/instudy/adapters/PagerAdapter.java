package com.codepath.android.instudy.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.android.instudy.R;
import com.codepath.android.instudy.fragments.CoursesSearch;
import com.codepath.android.instudy.fragments.CoursesStudent;
import com.codepath.android.instudy.fragments.CoursesTeacher;
import com.codepath.android.instudy.fragments.MessagesListFragment;


public class PagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

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

        Fragment f;
        if (position == 0) {
            f = new CoursesTeacher();
        } else if (position == 1) {
            f = new MessagesListFragment();
        }else if (position == 2) {
            f = new CoursesStudent();
        } else  if (position == 3) {
            f = new CoursesSearch();
        } else{
            return null;
        }
        return f;
    }
   /* @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }*/

    @Override
    public int getPageIconResId(int position) {
        return icons[position];
    }

}
