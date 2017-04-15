package com.codepath.android.instudy.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.android.instudy.R;
import com.codepath.android.instudy.helpers.RoundedCornersTransformation;

import com.codepath.android.instudy.models.Course;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex_ on 4/10/2017.
 */

//Taking the Course object and turning them into views displayed in the list;
public class CourseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface CourseListListener {
        public void onCourseApply(Course course);
    }

    // Assign the listener implementing events interface that will receive the events
    public void setCourseListListener(CourseListListener listener) {
        this.listener = listener;
    }

    private CourseListListener listener;


    // Define listener member variable
    private OnUserClickListListener userListener;

    // Define the listener interface
    public interface OnUserClickListListener {
        void onUserListClick(ArrayList<String> userids);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnUserListClickListener(OnUserClickListListener listener) {
        this.userListener = listener;
    }






    private final int TEA = 0, STU = 1, SEA = 2;

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
        String teacher = course.getTeachers();
        final String courseName = course.getTitle();
        query.whereEqualTo("objectId", teacher);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() < 1) {
                        Toast.makeText(mContext, courseName, Toast.LENGTH_LONG).show();
                        return;
                    }
                    ParseUser teacher = (ParseUser) objects.get(0);
                    vh1.tvTeacherName.setText(teacher.getString("FullName"));
                    String profileImage = teacher.getString("ProfileImage");

                    Glide.with(mContext).load(profileImage).asBitmap().centerCrop().into(new BitmapImageViewTarget(vh1.ivTeacherImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            vh1.ivTeacherImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
            }
        });

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("_User");
        query1.whereContainedIn("objectId", course.getStudents());
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                vh1.populateUserList(objects, mContext);
            }
        });

        vh1.setUserListIds(course.getStudents());
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

    public class ViewHolder_sea extends ViewHolder_simple {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        public TextView tvTeacherName;
        public ImageView ivTeacherImage;
        public Button btnApply;
        ImageView ivUser2;
        ImageView ivUser1;
        ImageView ivUser3;
        ImageView ivUser4;
        ImageView ivUser5;
        ImageView ivMoreIcon;
        TextView tvMessage;
        LinearLayout llUsers;
        ArrayList<String> userIds;

        public void setUserListIds(ArrayList<String> userids) {
            this.userIds = userids;
        }

        //Define constructor wichi accept entire row and find sub views
        public ViewHolder_sea(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            userIds = new ArrayList<>();
            tvTeacherName = (TextView) itemView.findViewById(R.id.tvTeacherName);
            ivTeacherImage = (ImageView) itemView.findViewById(R.id.ivTeacher);
            btnApply = (Button) itemView.findViewById(R.id.btnApply);
            ivUser1 = (ImageView) itemView.findViewById(R.id.ivPerson1);
            ivUser2 = (ImageView) itemView.findViewById(R.id.ivPerson2);
            ivUser3 = (ImageView) itemView.findViewById(R.id.ivPerson3);
            ivUser4 = (ImageView) itemView.findViewById(R.id.ivPerson4);
            ivUser5 = (ImageView) itemView.findViewById(R.id.ivPerson5);
            ivMoreIcon = (ImageView) itemView.findViewById(R.id.ivMore);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            llUsers = (LinearLayout) itemView.findViewById(R.id.llUsers);
            llUsers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            userListener.onUserListClick(userIds);
                        }
                    }
                }
            });
        }

        public void populateUserList(List<ParseObject> users, Context context) {
            ArrayList<ImageView> ctrls = new ArrayList<>();
            ivUser1.setVisibility(View.INVISIBLE);
            ctrls.add(ivUser1);
            ivUser2.setVisibility(View.INVISIBLE);
            ctrls.add(ivUser2);
            ivUser3.setVisibility(View.INVISIBLE);
            ctrls.add(ivUser3);
            ivUser4.setVisibility(View.INVISIBLE);
            ctrls.add(ivUser4);
            ivUser5.setVisibility(View.INVISIBLE);
            ctrls.add(ivUser5);
            ivMoreIcon.setVisibility(View.INVISIBLE);

            tvMessage.setText("This is new course! Be the first to register on it.");

            if (users.size() == 1) {
                tvMessage.setText("1 friend will attend this course.");
                loadImage(users.get(0), ivUser1, context);
            } else if (users.size() > 1) {
                int count = users.size() > 5 ? 4 : users.size();
                tvMessage.setText(String.format("%s friends will attend this course", count));
                for (int i = 0; i < count; i++) {
                    loadImage(users.get(i), ctrls.get(i), context);
                }
                if (count > 5) {
                    ivMoreIcon.setVisibility(View.INVISIBLE);
                }
            }
        }

        private void loadImage(ParseObject user, ImageView ivView, Context context) {
            String imagePath = user.getString("ProfileImage");
            ivView.setVisibility(View.VISIBLE);
            Glide.with(context).load(imagePath)
                    .bitmapTransform(new RoundedCornersTransformation(context, 15, 2))
                    .placeholder(R.drawable.default_user_white)
                    .into(ivView);
        }
    }

    private void applyCourse(Course course) {
        if (listener != null) {
            listener.onCourseApply(course);
        }
    }


}
