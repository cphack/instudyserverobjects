package com.codepath.android.instudy.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.android.instudy.R;
import com.codepath.android.instudy.activities.UserListActivity;
import com.codepath.android.instudy.adapters.GroupAdapter;
import com.codepath.android.instudy.adapters.PagerAdapter;
import com.codepath.android.instudy.models.Message;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.codepath.android.instudy.R.id.btSend;
import static com.codepath.android.instudy.R.id.etMessage;
import static com.codepath.android.instudy.R.id.lvChat;
import static com.codepath.android.instudy.fragments.Groups.MAX_CHAT_MESSAGES_TO_SHOW;
import static com.codepath.android.instudy.fragments.Groups.POLL_INTERVAL;

public class MainTabsFragment extends Fragment {
    ViewPager viewPager;
    PagerAdapter adapter;
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
                Toast.makeText(getActivity(), String.valueOf(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        addListeners();

        return v;
    }



    private void addListeners() {
       /* CoursesSearchFragment fragment = (CoursesSearchFragment) adapter.getRegisteredFragment(3);

        fragment.setOnUserListClickListener(new BaseCoursesFragment.OnUserListClickListener() {
            @Override
            public void onUserListClick(ArrayList<String> userids) {
                //call activity  //after that change all
            }
        });*/
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


  /* adapter.setOnUserListClickListener(new PagerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(itemView, position);
                    }
                }
            }
        });*/