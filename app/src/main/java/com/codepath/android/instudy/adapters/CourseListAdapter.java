package com.codepath.android.instudy.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.android.instudy.R;
import com.codepath.android.instudy.RoundedCornersTransformation;
import com.codepath.android.instudy.models.Course;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import static com.codepath.android.instudy.R.drawable.user;
import static com.codepath.android.instudy.R.string.teacher;

/**
 * Created by alex_ on 4/10/2017.
 */

//Taking the Course object and turning them into views displayed in the list;
public class CourseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int TEA = 0, STU = 1, SEA = 2;
    // List of tweets
    private List<Course> mCourses;
    // Store the context for easy access
    private Context mContext;

    private String type;

    public CourseListAdapter(@NonNull Context context, @NonNull List<Course> objects, String type) {
        mCourses = objects;
        mContext = context;
        listener = null;
        this.type = type;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    // inflate xml layout and return  viewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        // Inflate the custom layout
        switch (viewType) {
            case SEA:
                View v1 = inflater.inflate(R.layout.item_course_search, parent, false);
                viewHolder = new ViewHolder_sea(v1);
                break;
            case TEA:
                View v2 = inflater.inflate(R.layout.item_my_course, parent, false);
                viewHolder = new ViewHolder_simple(v2);
                break;
            /* case VID:
                View v3 = inflater.inflate(R.layout.item_tweet_video, parent, false);
                viewHolder = new ViewHolder_video(v3);
                break;*/
            default:
                View v3 = inflater.inflate(R.layout.item_my_course, parent, false);
                viewHolder = new ViewHolder_simple(v3);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {

        switch (type) {
            case "SEA":
                return SEA;
            case "TEA":
                return TEA;
            case "STU":
                return STU;
            default:
                return SEA;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {


        switch (viewHolder.getItemViewType()) {
            case TEA:
                ViewHolder_simple vh1 = (ViewHolder_simple) viewHolder;
                configureViewHolder_simple(vh1, position);
                break;
            case SEA:
                ViewHolder_sea vh2 = (ViewHolder_sea) viewHolder;
                configureViewHolder_sea(vh2, position);
                break;

            default:
                ViewHolder_simple vh = (ViewHolder_simple) viewHolder;
                configureViewHolder_simple(vh, position);
                break;
        }

    }

    private void configureViewHolder_simple(ViewHolder_simple vh, int position) {
        // Get the data model based on position
        Course course = mCourses.get(position);
        fetchCommon(vh, course);
    }

    private void configureViewHolder_sea(ViewHolder_sea vh, int position) {
        // Get the data model based on position
        final ViewHolder_sea vh1 = vh;
        final Course course = mCourses.get(position);
        fetchCommon(vh, course);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("objectId", course.getTeachers());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    ParseUser teacher = (ParseUser) objects.get(0);
                    vh1.tvTeacherName.setText(teacher.getString("FullName"));
                    String imagePath = teacher.getString("ImageUrl");
                    //load image from media

                    Glide.with(getContext()).load(imagePath)
                            .bitmapTransform(new RoundedCornersTransformation(mContext, 15, 2))
                            .placeholder(R.drawable.default_user_white)
                            .into(vh1.ivTeacherImage);
                } else {
                }
            }
        });

        vh1.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyCourse(course);
            }
        });
    }
    //populate main fields

    public void fetchCommon(ViewHolder_simple viewHolder, Course course) {

        viewHolder.tvTitle.setText(course.getTitle());
        viewHolder.tvSubTitle.setText(course.getSubTitle());


      /*  if (viewHolder.ivProfileImage != null) {
            Glide.with(getContext()).load(tweet.getUser().getProfileImageUrl())
                    .bitmapTransform(new RoundedCornersTransformation(getContext(), 15, 2))
                    .into(viewHolder.ivProfileImage);

            viewHolder.ivProfileImage.setClickable(true);
            viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  Log.i(SystemSettings.APP_TAG + " : " + HomeActivity.class.getName(), "Entered onClick method");

                   *//* if(mContext instanceof TimelineActivity){
                        ((TimelineActivity)mContext).lunchProfileActivity(tweet1.getUser());
                    }*//*
                    if (listener != null) {
                        listener.onProfileImageClick(tweet1.getUser());
                    }
                }
            });
        }*/
    }

    public static class ViewHolder_simple extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvTitle;
        public TextView tvSubTitle;

        //Define constructor wichi accept entire row and find sub views
        public ViewHolder_simple(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvSubTitle = (TextView) itemView.findViewById(R.id.tvSubTitle);
        }
    }

    public static class ViewHolder_sea extends ViewHolder_simple {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        public TextView tvTeacherName;
        public ImageView ivTeacherImage;
        public Button btnApply;

        //Define constructor wichi accept entire row and find sub views
        public ViewHolder_sea(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvTeacherName = (TextView) itemView.findViewById(R.id.tvTeacherName);
            ivTeacherImage = (ImageView) itemView.findViewById(R.id.ivTeacher);
            btnApply = (Button) itemView.findViewById(R.id.btnApply);
        }
    }

    private void applyCourse(Course course){
        if(listener!=null){
            listener.onCourseApply(course);
        }
    }



    public interface CourseListListener {
        public void onCourseApply(Course course);
    }



    // Assign the listener implementing events interface that will receive the events
    public void setCourseListListener(CourseListListener listener) {
        this.listener = listener;
    }

    private CourseListListener listener;
}
