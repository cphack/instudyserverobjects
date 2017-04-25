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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.android.instudy.R;
import com.codepath.android.instudy.models.Event;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


public class EventListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int COURSE = 0, OTHER = 1, CONF = 2, UNIV = 3, CHALL = 4, WORK = 5;

    private List<Event> mEvents;
    // Store the context for easy access
    private Context mContext;

    public EventListAdapter(@NonNull Context context, @NonNull List<Event> objects) {
        mEvents = objects;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        // Inflate the custom layout
        switch (viewType) {
            case COURSE:
                View v1 = inflater.inflate(R.layout.item_time_course, parent, false);
                viewHolder = new ItemTimeDefault(v1);
                break;
            case CONF:
                View v2 = inflater.inflate(R.layout.item_time_conf, parent, false);
                viewHolder = new ItemTimeDefault(v2);
                break;
            case UNIV:
                View v3 = inflater.inflate(R.layout.item_time_univ, parent, false);
                viewHolder = new ItemTimeUniv(v3);
                break;
            case CHALL:
                View v4 = inflater.inflate(R.layout.item_time_chall, parent, false);
                viewHolder = new ItemTimeDefault(v4);
                break;
            case WORK:
                View v5 = inflater.inflate(R.layout.item_time_work, parent, false);
                viewHolder = new ItemTimeDefault(v5);
                break;
            default:
                View v6 = inflater.inflate(R.layout.item_time_other, parent, false);
                viewHolder = new ItemTimeDefault(v6);
                break;
        }

        return viewHolder;
    }


    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mEvents.size();
    }


    @Override
    public int getItemViewType(int position) {
        Event e = mEvents.get(position);
        switch (e.getType()) {
            case"work":
                return WORK;
            case"challenge":
                return CHALL;
            case"conference":
                return CONF;
            case"course":
                return COURSE;
            case"univer":
                return UNIV;
            default:
                return OTHER;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {

            case COURSE:
                ItemTimeDefault vh1 = (ItemTimeDefault) viewHolder;
                configureDefault(vh1, position);
                break;
            case CONF:
                ItemTimeDefault vh2 = (ItemTimeDefault) viewHolder;
                configureDefault(vh2, position);
                break;
            case UNIV:
                ItemTimeUniv vh3 = (ItemTimeUniv) viewHolder;
                configureUniv(vh3, position);
                break;
            case CHALL:
                ItemTimeDefault vh4 = (ItemTimeDefault) viewHolder;
                configureDefault(vh4, position);
                break;
            case WORK:
                ItemTimeDefault vh5 = (ItemTimeDefault) viewHolder;
                configureDefault(vh5, position);
                break;
            default:
                ItemTimeDefault vh6 = (ItemTimeDefault) viewHolder;
                configureDefault(vh6, position);
                break;
        }
    }

    private void configureDefault(final ItemTimeDefault vh, int position) {
        // Get the data model based on position
        Event e = mEvents.get(position);
        /*
        vh.tvTimestamp.setText(DateTimeHelper.getRelativeTimeAgo(chat.getUpdatedAt()));
        ArrayList<String> users = chat.getRecipients();


        String groupName = chat.getChatName();
        if (groupName == null || TextUtils.isEmpty(groupName)) {
            groupName = "Group chat";
        }
        vh.tvGroupName.setText(groupName);
//        vh.tvGroupNum.setText(String.format("%s members", users.size()));*/

    }

    private void configureUniv(final ItemTimeUniv vh, int position) {
        // Get the data model based on position
        Event e = mEvents.get(position);
        /*
        vh.tvTimestamp.setText(DateTimeHelper.getRelativeTimeAgo(chat.getUpdatedAt()));
        ArrayList<String> users = chat.getRecipients();


        String groupName = chat.getChatName();
        if (groupName == null || TextUtils.isEmpty(groupName)) {
            groupName = "Group chat";
        }
        vh.tvGroupName.setText(groupName);
//        vh.tvGroupNum.setText(String.format("%s members", users.size()));*/

    }



    public class ItemTimeDefault extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        public TextView tvDate;
        public TextView tvEvent;


        public ItemTimeDefault(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvEvent= (TextView) itemView.findViewById(R.id.tvEvent);
        }
    }

    public class ItemTimeUniv extends ItemTimeDefault {

        ImageView ivUser2;
        ImageView ivUser1;
        ImageView ivUser3;
        ImageView ivUser4;
        ImageView ivUser5;
        ImageView ivMoreIcon;
        TextView tvMessage;
        LinearLayout llUsers;
        ArrayList<String> userIds;

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

        private void loadImage(ParseObject user, final ImageView ivView, Context context) {
            String imagePath = user.getString("ProfileImage");
            ivView.setVisibility(View.VISIBLE);
            Glide.with(context).load(imagePath).asBitmap().centerCrop().placeholder(R.drawable.default_user_white).into(new BitmapImageViewTarget(ivView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    ivView.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        public void setUserListIds(ArrayList<String> userids) {
            this.userIds = userids;
        }

        //Define constructor wichi accept entire row and find sub views
        public ItemTimeUniv(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ivUser1 = (ImageView) itemView.findViewById(R.id.ivPerson1);
            ivUser2 = (ImageView) itemView.findViewById(R.id.ivPerson2);
            ivUser3 = (ImageView) itemView.findViewById(R.id.ivPerson3);
            ivUser4 = (ImageView) itemView.findViewById(R.id.ivPerson4);
            ivUser5 = (ImageView) itemView.findViewById(R.id.ivPerson5);
            ivMoreIcon = (ImageView) itemView.findViewById(R.id.ivMore);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
        }
    }

}
