

package com.codepath.android.instudy.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.android.instudy.R;
import com.parse.FindCallback;

import com.parse.ParseException;

import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by alex_ on 4/24/2017.
 */

public class ChatToolbarGroupFragment extends Fragment {

    ImageView ivChatter1, ivChatter2;
    TextView tvGroupCount;
    TextView tvchatnaming;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_chat_toolbar_group, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        ivChatter1 = (ImageView) view.findViewById(R.id.ivChatter1);
        ivChatter2 = (ImageView) view.findViewById(R.id.ivChatter2);
        tvGroupCount = (TextView) view.findViewById(R.id.tvGroupCount);
        tvchatnaming= (TextView) view.findViewById(R.id.tvchatnaming);
    }


    public static ChatToolbarGroupFragment newInstance(ArrayList<String> userids,String chatName) {
        ChatToolbarGroupFragment fragmentDemo = new ChatToolbarGroupFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("userids", userids);
        args.putString("chatname", chatName);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> userids = getArguments().getStringArrayList("userids");
        final String chatName = getArguments().getString("chatname", "");




        userids.remove(ParseUser.getCurrentUser().getObjectId());

        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        query.whereContainedIn("objectId", userids);
        query.whereExists("ProfileImage");

        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {

                    if (users.size() > 0) {
                        String urlPhotoUser = users.get(0).getString("ProfileImage");
                        Glide.with(getContext()).load(urlPhotoUser).asBitmap().centerCrop()
                                .placeholder(R.drawable.default_user_white).into(new BitmapImageViewTarget(ivChatter1) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                ivChatter1.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                    }
                    if (users.size() > 1) {
                        String urlPhotoUser = users.get(1).getString("ProfileImage");
                        Glide.with(getContext()).load(urlPhotoUser).asBitmap().centerCrop()
                                .placeholder(R.drawable.default_user_white).into(new BitmapImageViewTarget(ivChatter2) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                ivChatter2.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                    }
                    if (users.size() > 2) {
                        tvGroupCount.setText(String.format("+%s", users.size() - 2));
                    }

                    if(!TextUtils.isEmpty(chatName)&&tvchatnaming!=null){
                        tvchatnaming.setText(chatName);
                    }

                }
            }
        });


    }
}
