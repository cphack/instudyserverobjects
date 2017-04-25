package com.codepath.android.instudy.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.android.instudy.R;
import com.codepath.android.instudy.models.Message;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStorageDirectory;
import static com.codepath.android.instudy.R.drawable.studyowl;
import static com.codepath.android.instudy.R.drawable.theaderowl;
import static com.codepath.android.instudy.R.id.ivCamera;
import static com.codepath.android.instudy.R.id.ivGallery;


public class UserProfile extends Fragment {

    TextView etUserName;
    TextView tvStatusLine;
    LinearLayout llStatusLine;
    RelativeLayout rlAddNewEvent;
    ImageView profileImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = (View) inflater.inflate(R.layout.fragment_user_profile, container, false);
        etUserName = (TextView) v.findViewById(R.id.tvFullName);
        tvStatusLine = (TextView) v.findViewById(R.id.tvStatusLine);
        llStatusLine = (LinearLayout) v.findViewById(R.id.llStatusLine);
        profileImg = (ImageView) v.findViewById(R.id.ivProfileImage);
        rlAddNewEvent = (RelativeLayout) v.findViewById(R.id.rlAddNewEvent);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }


    public void setProfile(String userid) {
        ParseQuery<ParseUser> query = new ParseQuery<>("_User");
        query.getInBackground(userid, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser me, ParseException e) {
                String profileImageUrl;

                if (me.getParseFile("ImageFile") != null) {
                    ParseFile imageFile = me.getParseFile("ImageFile");
                    //profileImg
                    profileImageUrl = imageFile.getUrl();
                } else {
                    profileImageUrl = me.getString("ProfileImage");
                }

                Glide.with(getContext()).load(profileImageUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(profileImg) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        profileImg.setImageDrawable(circularBitmapDrawable);
                    }
                });

                String value = me.getString("FullName");
                if (value != null) {
                    etUserName.setText(value);
                }

                value = me.getString("statusLine");
                if (value != null) {
                    tvStatusLine.setText(value);
                }
            }
        });

        TimelineFragment f = new TimelineFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.timeline_container, f,"timeline_frag").commit();
        /*TimelineFragment f = (TimelineFragment)
                getChildFragmentManager().findFragmentByTag("timeline_frag");*/
        if (f != null) {
            f.populateTimeline(userid);
        }
    }
}
