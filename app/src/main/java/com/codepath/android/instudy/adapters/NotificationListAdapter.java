package com.codepath.android.instudy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.android.instudy.R;
import com.codepath.android.instudy.activities.NotificationActivity;
import com.codepath.android.instudy.helpers.DateTimeHelper;
import com.codepath.android.instudy.models.Chat;
import com.codepath.android.instudy.models.Message;
import com.codepath.android.instudy.models.Notification;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.codepath.android.instudy.R.id.tvName;


public class NotificationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int UNIV = 0, CHALL = 1, WORK = 2;

    private List<Notification> mNotes;
    // Store the context for easy access
    private Context mContext;

    public NotificationListAdapter(@NonNull Context context, @NonNull List<Notification> objects) {
        mNotes = objects;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    // inflate xml layout and return  viewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        // Inflate the custom layout
        switch (viewType) {
            case UNIV:
                View v1 = inflater.inflate(R.layout.item_note_univ, parent, false);
                viewHolder = new ItemNoteUniv(v1);
                break;
            case CHALL:
                View v2 = inflater.inflate(R.layout.item_note_chall, parent, false);
                viewHolder = new ItemNoteChall(v2);
                break;
            case WORK:
                View v3 = inflater.inflate(R.layout.item_note_work, parent, false);
                viewHolder = new ItemNoteWork(v3,getContext());
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        Notification note = mNotes.get(position);

        switch (note.getType()) {
            case "univ":
                return UNIV;
            case "work":
                return WORK;
            case "chall":
                return CHALL;
            default:
                return UNIV;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case UNIV:
                ItemNoteUniv vh = (ItemNoteUniv) viewHolder;
                configureNoteUniv(vh, position);
                break;

            case CHALL:
                ItemNoteChall vh1 = (ItemNoteChall) viewHolder;
                configureNoteChall(vh1, position);
                break;

            case WORK:
                ItemNoteWork vh2 = (ItemNoteWork) viewHolder;
                configureNoteWork(vh2, position);
                break;
        }
    }

    private void configureNoteUniv(final ItemNoteUniv vh, int position) {
        // Get the data model based on position
        Notification n = mNotes.get(position);
        String title = n.getTitle();
        vh.tvTitle.setText(title);
        if (title.contains("assignment")) {
            Glide.with(getContext())
                    .load("")
                    .placeholder(R.drawable.starting_icon)
                    .into(vh.ivImage);
        } else {
            Glide.with(getContext())
                    .load("")
                    .placeholder(R.drawable.task_icon)
                    .into(vh.ivImage);
        }
    }

    private void configureNoteChall(final ItemNoteChall vh, int position) {
        // Get the data model based on position
        Notification n = mNotes.get(position);
        String title = n.getTitle();
        vh.tvTitle.setText(title);

        Glide.with(getContext())
                .load("")
                .placeholder(R.drawable.starting_icon)
                .into(vh.ivImage);
    }

    private void configureNoteWork(final ItemNoteWork vh, int position) {
        Notification n = mNotes.get(position);

        vh.rootView.setTag(n);
        String title = n.getTitle();
        vh.tvTitle.setText(title);
        vh.tvDescrip.setText(n.getDescrip());
        Glide.with(getContext())
                .load(n.getImageUrlKey())
                .into(vh.ivLogo);
    }


    public class ItemNoteUniv extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public ImageView ivImage;

        public ItemNoteUniv(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        }
    }

    public class ItemNoteChall extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public ImageView ivImage;


        public ItemNoteChall(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        }
    }

    public class ItemNoteWork extends RecyclerView.ViewHolder {

        final View rootView;
        public TextView tvTitle;
        public TextView tvDescrip;
        public ImageView ivLogo;

        public ItemNoteWork(View itemView, final Context context) {
            super(itemView);

            rootView = itemView;
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDescrip = (TextView) itemView.findViewById(R.id.tvDescrip);
            ivLogo = (ImageView) itemView.findViewById(R.id.ivLogo);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Notification n = (Notification)v.getTag();
                    if (n != null) {

                        Intent i = new Intent(context, NotificationActivity.class);
                        i.putExtra("notificationurl", n.getLink());


                        context.startActivity(i);
                    }
                }
            });
        }
    }

}
