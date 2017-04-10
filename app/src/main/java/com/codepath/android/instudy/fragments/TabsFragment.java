package com.codepath.android.instudy.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.android.instudy.R;
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

public class TabsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = (View) inflater.inflate(R.layout.fragment_tabs, container, false);

        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        PagerAdapter adapter = new PagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);


        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
        tabStrip.setViewPager(viewPager);

        return v;
    }
}
