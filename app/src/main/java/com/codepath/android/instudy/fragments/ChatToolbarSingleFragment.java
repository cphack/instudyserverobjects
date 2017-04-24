package com.codepath.android.instudy.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.android.instudy.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;



/**
 * Created by alex_ on 4/24/2017.
 */

public class ChatToolbarSingleFragment extends Fragment {

    ImageView ivUser;
    TextView tvUserName;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_chat_toolbar_single, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        ivUser = (ImageView) view.findViewById(R.id.ivUserImage);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
    }


    public static ChatToolbarSingleFragment newInstance(String userid) {
        ChatToolbarSingleFragment fragmentDemo = new ChatToolbarSingleFragment();
        Bundle args = new Bundle();
        args.putString("userid", userid);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String userid = getArguments().getString("userid", "");

        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.getInBackground(userid, new GetCallback<ParseUser>() {
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    tvUserName.setText(user.getString("FullName"));
                    String urlPhotoUser = user.getString("ProfileImage");
                    Glide.with(getContext()).load(urlPhotoUser).asBitmap().centerCrop()
                            .placeholder(R.drawable.default_user_white).into(new BitmapImageViewTarget(ivUser) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivUser.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
            }
        });
    }
}
