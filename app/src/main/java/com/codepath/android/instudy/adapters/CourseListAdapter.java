package com.codepath.android.instudy.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alex_ on 4/10/2017.
 */

//Taking the Course object and turning them into views displayed in the list;
public class CourseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Define listener member variable
    private OnUserClickListListener userListener;

    // Define the listener interface
    public interface OnUserClickListListener {
        void onUserListClick(ArrayList<String> userids);

        void onCourseTeacherLectionsClick(String courseid);

        void onCourseTeacherAssignmentsClick(String courseid);

        void onCourseTeacherManageClick(String courseid);

        void onCourseTeacherNotificationClick(String courseid);

        void onCourseSearchOverviewClick(String courseid);

        void onCourseSearchApplyClick(String courseid);

        void onCourseStudentLectionsClick(String courseid);

        void onCourseStudentSubmitClick(String courseid);

        void onCourseStudentChatClick(String courseid);


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
                View v2 = inflater.inflate(R.layout.item_course_teacher, parent, false);
                viewHolder = new ViewHolder_tea(v2);
                break;

            case STU:
                View v3 = inflater.inflate(R.layout.item_course_student, parent, false);
                viewHolder = new ViewHolder_stu(v3);
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
                ViewHolder_tea vh1 = (ViewHolder_tea) viewHolder;
                configureViewHolder_tea(vh1, position);
                break;
            case SEA:
                ViewHolder_sea vh2 = (ViewHolder_sea) viewHolder;
                configureViewHolder_sea(vh2, position);
                break;
            case STU:
                ViewHolder_stu vh = (ViewHolder_stu) viewHolder;
                configureViewHolder_stu(vh, position);
                break;
        }

    }


    private void configureViewHolder_sea(final ViewHolder_sea vh, int position) {
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

    }

    private void configureViewHolder_tea(final ViewHolder_tea vh, int position) {
        // Get the data model based on position
        final Course course = mCourses.get(position);
        fetchCommon(vh, course);

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("_User");
        query1.whereContainedIn("objectId", course.getStudents());
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                vh.populateUserList(objects, mContext);
            }
        });

        vh.setUserListIds(course.getStudents());
    }

    private void configureViewHolder_stu(final ViewHolder_stu vh, int position) {
        // Get the data model based on position
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

                        return;
                    }
                    ParseUser teacher = (ParseUser) objects.get(0);
                    vh.tvTeacherName.setText(teacher.getString("FullName"));
                    String profileImage = teacher.getString("ProfileImage");

                    Glide.with(mContext).load(profileImage).asBitmap().centerCrop().into(new BitmapImageViewTarget(vh.ivTeacherImage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            vh.ivTeacherImage.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
            }
        });

        final String curUserId = ParseUser.getCurrentUser().getObjectId();
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("_User");
        query1.whereContainedIn("objectId", course.getStudents());
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                ArrayList<ParseObject> users = new ArrayList<ParseObject>();

                for (ParseObject obj : objects) {
                    if (!obj.getObjectId().equals(curUserId)) {
                        users.add(obj);
                    }
                }

                vh.populateUserList(users, mContext);
            }
        });
        ArrayList<String> students = course.getStudents();
        students.remove(curUserId);
        vh.setUserListIds(students);


        String endDate ="2017-4-30";
        vh.countDownStart(endDate);
    }


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
        Button btnOverview;
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

        public ViewHolder_sea(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            userIds = new ArrayList<>();
            tvTeacherName = (TextView) itemView.findViewById(R.id.tvTeacherName);
            ivTeacherImage = (ImageView) itemView.findViewById(R.id.ivTeacher);
            btnApply = (Button) itemView.findViewById(R.id.btnApply);
            btnOverview = (Button) itemView.findViewById(R.id.btnOverview);
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

            btnApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userListener != null) {
                        int position = getAdapterPosition();
                        Course course = mCourses.get(position);
                        if (position != RecyclerView.NO_POSITION) {
                            userListener.onCourseSearchApplyClick(course.getObjectId());
                        }
                    }
                }
            });

            btnOverview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userListener != null) {
                        int position = getAdapterPosition();
                        Course course = mCourses.get(position);
                        if (position != RecyclerView.NO_POSITION) {
                            userListener.onCourseSearchOverviewClick(course.getObjectId());
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
                    ivMoreIcon.setVisibility(View.VISIBLE);
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

    public class ViewHolder_tea extends ViewHolder_simple {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        ImageView ivUser2;
        ImageView ivUser1;
        ImageView ivUser3;
        ImageView ivUser4;
        ImageView ivUser5;
        ImageView ivUser6;
        ImageView ivUser7;
        ImageView ivMoreIcon;
        TextView tvMessage;
        LinearLayout llUsers;
        ArrayList<String> userIds;

        Button btnLections;
        Button btnManage;
        Button btnNotifications;
        Button btnAssignments;

        public void setUserListIds(ArrayList<String> userids) {
            this.userIds = userids;
        }

        public ViewHolder_tea(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            userIds = new ArrayList<>();
            ivUser1 = (ImageView) itemView.findViewById(R.id.ivPerson1);
            ivUser2 = (ImageView) itemView.findViewById(R.id.ivPerson2);
            ivUser3 = (ImageView) itemView.findViewById(R.id.ivPerson3);
            ivUser4 = (ImageView) itemView.findViewById(R.id.ivPerson4);
            ivUser5 = (ImageView) itemView.findViewById(R.id.ivPerson5);
            ivUser6 = (ImageView) itemView.findViewById(R.id.ivPerson6);
            ivUser7 = (ImageView) itemView.findViewById(R.id.ivPerson7);
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

            btnLections = (Button) itemView.findViewById(R.id.btnLections);
            btnAssignments = (Button) itemView.findViewById(R.id.btnAssignments);
            btnManage = (Button) itemView.findViewById(R.id.btnManage);
            btnNotifications = (Button) itemView.findViewById(R.id.btnNotify);

            btnLections.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userListener != null) {
                        int position = getAdapterPosition();
                        Course course = mCourses.get(position);
                        if (position != RecyclerView.NO_POSITION) {
                            userListener.onCourseTeacherLectionsClick(course.getObjectId());
                        }
                    }
                }
            });

            btnManage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userListener != null) {
                        int position = getAdapterPosition();
                        Course course = mCourses.get(position);
                        if (position != RecyclerView.NO_POSITION) {
                            userListener.onCourseTeacherManageClick(course.getObjectId());
                        }
                    }
                }
            });

            btnNotifications.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userListener != null) {
                        int position = getAdapterPosition();
                        Course course = mCourses.get(position);
                        if (position != RecyclerView.NO_POSITION) {
                            userListener.onCourseTeacherNotificationClick(course.getObjectId());
                        }
                    }
                }
            });

            btnAssignments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userListener != null) {
                        int position = getAdapterPosition();
                        Course course = mCourses.get(position);
                        if (position != RecyclerView.NO_POSITION) {
                            userListener.onCourseTeacherAssignmentsClick(course.getObjectId());
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
            ivUser6.setVisibility(View.INVISIBLE);
            ctrls.add(ivUser6);
            ivUser7.setVisibility(View.INVISIBLE);
            ctrls.add(ivUser7);
            ivMoreIcon.setVisibility(View.INVISIBLE);

            tvMessage.setText("No students registered yet.");

            if (users.size() < 1) {
                llUsers.setVisibility(View.INVISIBLE);
            } else {
                llUsers.setVisibility(View.VISIBLE);
            }
            if (users.size() == 1) {
                tvMessage.setText("1 student attends this course.");
                loadImage(users.get(0), ivUser1, context);
            } else if (users.size() > 1) {
                int count = users.size() > 7 ? 6 : users.size();
                tvMessage.setText(String.format("%s students attend this course", count));
                for (int i = 0; i < count; i++) {
                    loadImage(users.get(i), ctrls.get(i), context);
                }
                if (count > 7) {
                    ivMoreIcon.setVisibility(View.VISIBLE);
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

    public class ViewHolder_stu extends ViewHolder_simple {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        public TextView tvTeacherName;
        public ImageView ivTeacherImage;
        Button btnLections;
        Button btnGroupChat;
        ImageView ivUser2;
        ImageView ivUser1;
        ImageView ivUser3;
        ImageView ivUser4;
        ImageView ivUser5;
        ImageView ivMoreIcon;
        TextView tvMessage;
        LinearLayout llUsers;
        ArrayList<String> userIds;

        public TextView tvDayStu, tvHourStu, tvMinuteStu;
        public Handler handler;
        public Runnable runnable;
        public LinearLayout llCtr;
        Button btnAssignSubmit;

        public void setUserListIds(ArrayList<String> userids) {
            this.userIds = userids;
        }

        public ViewHolder_stu(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            userIds = new ArrayList<>();
            tvTeacherName = (TextView) itemView.findViewById(R.id.tvTeacherName);
            ivTeacherImage = (ImageView) itemView.findViewById(R.id.ivTeacher);
            btnLections = (Button) itemView.findViewById(R.id.btnLections);
            btnGroupChat = (Button) itemView.findViewById(R.id.btnGroupChat);

            btnAssignSubmit = (Button) itemView.findViewById(R.id.btnAssignSubmit);
            ivUser1 = (ImageView) itemView.findViewById(R.id.ivPerson1);
            ivUser2 = (ImageView) itemView.findViewById(R.id.ivPerson2);
            ivUser3 = (ImageView) itemView.findViewById(R.id.ivPerson3);
            ivUser4 = (ImageView) itemView.findViewById(R.id.ivPerson4);
            ivUser5 = (ImageView) itemView.findViewById(R.id.ivPerson5);
            ivMoreIcon = (ImageView) itemView.findViewById(R.id.ivMore);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            llUsers = (LinearLayout) itemView.findViewById(R.id.llUsers);

            tvDayStu = (TextView) itemView.findViewById(R.id.txtTimerDayStu);
            tvHourStu = (TextView) itemView.findViewById(R.id.txtTimerHourStu);
            tvMinuteStu = (TextView) itemView.findViewById(R.id.txtTimerMinuteStu);
            llCtr = (LinearLayout) itemView.findViewById(R.id.ll1Stu);

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
            btnLections.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userListener != null) {
                        int position = getAdapterPosition();
                        Course course = mCourses.get(position);
                        if (position != RecyclerView.NO_POSITION) {
                            userListener.onCourseStudentLectionsClick(course.getObjectId());
                        }
                    }
                }
            });

            btnGroupChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userListener != null) {
                        int position = getAdapterPosition();
                        Course course = mCourses.get(position);
                        if (position != RecyclerView.NO_POSITION) {
                            userListener.onCourseStudentChatClick(course.getObjectId());
                        }
                    }
                }
            });

            btnAssignSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userListener != null) {
                        int position = getAdapterPosition();
                        Course course = mCourses.get(position);
                        if (position != RecyclerView.NO_POSITION) {
                            userListener.onCourseStudentSubmitClick(course.getObjectId());
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

            tvMessage.setText("This is new course!You are the first one to register for the course!");

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
                    ivMoreIcon.setVisibility(View.VISIBLE);
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

        public void countDownStart(final String endDate) {
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(this, 1000);
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        // Here Set your Event Date
                        Date eventDate = dateFormat.parse(endDate);
                        Date currentDate = new Date();
                        if (!currentDate.after(eventDate)) {
                            long diff = eventDate.getTime()
                                    - currentDate.getTime();
                            long days = diff / (24 * 60 * 60 * 1000);
                            diff -= days * (24 * 60 * 60 * 1000);
                            long hours = diff / (60 * 60 * 1000);
                            diff -= hours * (60 * 60 * 1000);
                            long minutes = diff / (60 * 1000);
                            tvDayStu.setText("" + String.format("%02d", days));
                            tvHourStu.setText("" + String.format("%02d", hours));
                            tvMinuteStu.setText("" + String.format("%02d", minutes));
                        } else {
                            llCtr.setVisibility(View.GONE);
                            btnAssignSubmit.setVisibility(View.GONE);
                            handler.removeCallbacks(runnable);
                            // handler.removeMessages(0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            handler.postDelayed(runnable, 0);
        }
    }
}

