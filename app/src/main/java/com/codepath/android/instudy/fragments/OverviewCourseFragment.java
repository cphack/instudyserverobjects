package com.codepath.android.instudy.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.android.instudy.R;
import com.codepath.android.instudy.models.Course;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OverviewCourseFragment extends Fragment {

    TextView tvCourseName;
    TextView tvDescription;
    TextView tvNumLections;

    TextView tvNumAssignments;

    ImageView ivTeacherImage;
    TextView tvTeacherName;

    String courseId;

    public static OverviewCourseFragment newInstance(String courseid) {
        OverviewCourseFragment fragment = new OverviewCourseFragment();
        Bundle args = new Bundle();
        args.putString("courseid", courseid);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_overview_course, container, false);
        // Inflate the layout for this fragment
        tvCourseName = (TextView) v.findViewById(R.id.tvCourseName);
        tvDescription = (TextView) v.findViewById(R.id.tvDescription);
        tvNumLections = (TextView) v.findViewById(R.id.tvNumLections);
        tvNumAssignments = (TextView) v.findViewById(R.id.tvNumAssignments);
        ivTeacherImage = (ImageView) v.findViewById(R.id.ivTeacherImage);
        tvTeacherName = (TextView) v.findViewById(R.id.tvTeacherName);

        return v;
    }

    public void getCourseData(String courseid) {
        // Specify which class to query
        ParseQuery<Course> query = ParseQuery.getQuery(Course.class);
        // Specify the object id
        query.getInBackground(courseid, new GetCallback<Course>() {
            public void done(Course item, ParseException e) {
                if (e == null) {
                    populateCourseFields(item);
                } else {

                }
            }
        });
    }

    void populateCourseFields(Course c) {
        tvCourseName.setText(c.getTitle());
        tvDescription.setText(c.getDescription());
        /*tvNumLections=(TextView) v.findViewById(R.id. tvNumLections);
        tvNumAssignments=(TextView) v.findViewById(R.id.tvNumAssignments);*/
        ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
        // Specify the object id
        query.getInBackground(c.getTeachers(), new GetCallback<ParseUser>() {
            public void done(ParseUser item, ParseException e) {
                if (e == null) {
                    Glide.with(getContext()).load(item.getString("ProfileImage")).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivTeacherImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivTeacherImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                    tvTeacherName.setText(item.getString("FullName"));

                } else {
                    // something went wrong
                }
            }
        });


    }
}
