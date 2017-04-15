package com.codepath.android.instudy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex_ on 4/14/2017.
 */

public  class BaseCoursesFragment extends Fragment {
    // Define listener member variable
    protected OnUserListClickListener userListClickListener;
    // Define the listener interface
    public interface OnUserListClickListener{
        void onUserListClick(ArrayList<String> userids);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnUserListClickListener(OnUserListClickListener listener) {
        this.userListClickListener = listener;
    }

}
