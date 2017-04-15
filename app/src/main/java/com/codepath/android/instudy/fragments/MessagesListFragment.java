package com.codepath.android.instudy.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.android.instudy.R;


public class MessagesListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages_list, container, false);
    }

    // newInstance constructor for creating fragment with arguments
    public static MessagesListFragment newInstance(int page, String title) {
        MessagesListFragment fragment = new MessagesListFragment();
      /*  Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragment.setArguments(args);*/
        return fragment;
    }
}
