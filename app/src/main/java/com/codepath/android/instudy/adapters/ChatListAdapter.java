package com.codepath.android.instudy.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.util.Util;
import com.codepath.android.instudy.R;
import com.codepath.android.instudy.models.Chat;
import com.codepath.android.instudy.models.Message;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



//Taking the tweets object and turning them into views displayed in the list;
public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int GROUP = 0, SINGLE = 1;

    private List<Chat> mChats;
    // Store the context for easy access
    private Context mContext;

    public ChatListAdapter(@NonNull Context context, @NonNull List<Chat> objects) {
        mChats = objects;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mChats.size();
    }

    // inflate xml layout and return  viewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        // Inflate the custom layout
        switch (viewType) {
            case GROUP:
                View v1 = inflater.inflate(R.layout.item_chat_group, parent, false);
                viewHolder = new ViewHolder_group(v1);
                break;
            case SINGLE:
                View v3 = inflater.inflate(R.layout.item_chat_single, parent, false);
                viewHolder = new ViewHolder_single(v3);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
    Chat chat= mChats.get(position);

        if(chat.getRecipients().size()>2){
            return GROUP;
        }else
        {
            return SINGLE;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case GROUP:
                ViewHolder_group vh = (ViewHolder_group) viewHolder;
                configureViewHolder_group(vh, position);
                break;

            case SINGLE:
                ViewHolder_single vh1 = (ViewHolder_single) viewHolder;
                configureViewHolder_single(vh1, position);
                break;
        }
    }

    private void configureViewHolder_group(final ViewHolder_group vh, int position) {
        // Get the data model based on position
        Chat chat = mChats.get(position);
        ArrayList<String> users = chat.getRecipients();

        vh.tvGroupNum.setText(String.format("%s members"));

        ParseQuery<Message> query = ParseQuery.getQuery("Message");
        query.getInBackground(chat.getLastMessageId(), new GetCallback<Message>() {
            public void done(Message msg, ParseException e) {
                if (e == null) {
                    vh.tvLastMessage.setText(msg.getBody());

                    ParseQuery<ParseUser> query1 = ParseQuery.getQuery("_User");
                    query1.getInBackground(msg.getUserId(), new GetCallback<ParseUser>() {
                        public void done(ParseUser user, ParseException e) {
                            if (e == null) {

                                Glide.with(mContext).load(user.getString("ProfileImage")).asBitmap()
                                        .centerCrop().into(new BitmapImageViewTarget(vh.ivLastMessageUser) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                        circularBitmapDrawable.setCircular(true);
                                        vh.ivLastMessageUser.setImageDrawable(circularBitmapDrawable);
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });


    }

    private void configureViewHolder_single(final ViewHolder_single vh, int position) {
        // Get the data model based on position
        Chat chat = mChats.get(position);
        ArrayList<String> users = chat.getRecipients();
        users.remove(ParseUser.getCurrentUser().getObjectId());
        String otherUser = users.get(0);


        ParseQuery<Message> query = ParseQuery.getQuery("Message");
        query.getInBackground(chat.getLastMessageId(), new GetCallback<Message>() {
            public void done(Message msg, ParseException e) {
                if (e == null) {
                    vh.tvLastMessage.setText(msg.getBody());
                }
            }
        });

        ParseQuery<ParseUser> query1 = ParseQuery.getQuery("_User");
        query1.getInBackground(otherUser, new GetCallback<ParseUser>() {
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    vh.tvName.setText(user.getString("FullName"));
                    Glide.with(mContext).load(user.getString("ProfileImage")).asBitmap()
                            .centerCrop().into(new BitmapImageViewTarget(vh.ivProfileOther) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            vh.ivProfileOther.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
            }
        });
    }

    public  class ViewHolder_single extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView ivProfileOther;
        public TextView tvName;
        public TextView tvLastMessage;

        //Define constructor wichi accept entire row and find sub views
        public ViewHolder_single(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ivProfileOther=(ImageView) itemView.findViewById(R.id.ivProfileOther);
            tvName=(TextView ) itemView.findViewById(R.id.tvName);
            tvLastMessage=(TextView ) itemView.findViewById(R.id.tvLastMessage);
        }
    }

    public  class ViewHolder_group extends  RecyclerView.ViewHolder {

        public TextView tvGroupNum;
        public ImageView ivLastMessageUser;
        public TextView tvLastMessage;

        //Define constructor wichi accept entire row and find sub views
        public ViewHolder_group(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);


            tvGroupNum = (TextView) itemView.findViewById(R.id.tvGroupNum);
            ivLastMessageUser=(ImageView) itemView.findViewById(R.id.ivLastMessageUser);
            tvLastMessage=(TextView) itemView.findViewById(R.id.tvLastMessage);
        }
    }

}
