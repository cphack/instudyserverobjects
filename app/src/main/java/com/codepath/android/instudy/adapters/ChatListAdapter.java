package com.codepath.android.instudy.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.text.TextUtilsCompat;
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
import com.codepath.android.instudy.helpers.DateTimeHelper;
import com.codepath.android.instudy.models.Chat;
import com.codepath.android.instudy.models.Message;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;



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
                viewHolder = new ItemChatGroup(v1);
                break;
            case SINGLE:
                View v3 = inflater.inflate(R.layout.item_chat_single, parent, false);
                viewHolder = new ItemChatSingle(v3);
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = mChats.get(position);

        if (chat.getRecipients().size() > 2) {
            return GROUP;
        } else {
            return SINGLE;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case GROUP:
                ItemChatGroup vh = (ItemChatGroup) viewHolder;
                configureViewHolder_group(vh, position);
                break;

            case SINGLE:
                ItemChatSingle vh1 = (ItemChatSingle) viewHolder;
                configureViewHolder_single(vh1, position);
                break;
        }
    }

    private void configureViewHolder_group(final ItemChatGroup vh, int position) {
        // Get the data model based on position
        Chat chat = mChats.get(position);
        vh.tvTimestamp.setText(DateTimeHelper.getRelativeTimeAgo(chat.getUpdatedAt()));
        ArrayList<String> users = chat.getRecipients();
        /*
        1.check if chat has name- > set the chat name
        2.get number of participants display it
        3.get lkatest messageid from chat- retrieeve message and popualte controls
        4. if not available get messages by chatid sorted by created at DESC  get first message and populate controls
         */

        String groupName = chat.getChatName();
        if(groupName==null ||TextUtils.isEmpty(groupName)){
            groupName="Group chat";
        }
        vh.tvGroupName.setText(groupName);
//        vh.tvGroupNum.setText(String.format("%s members", users.size()));


        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        String lastMessageID = chat.getLastMessageId();
        if(lastMessageID==null ||TextUtils.isEmpty(lastMessageID)) {

            query.whereEqualTo(Message.CHAT_KEY,chat.getObjectId());
            // get the latest 500 messages, order will show up newest to oldest of this group
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<Message>() {
                public void done(List<Message> msgs, ParseException e) {
                    if (e == null&&msgs.size()>0) {
                        Message msg = msgs.get(0);
                        vh.tvMessage.setText(msg.getBody());
                        ParseQuery<ParseUser> query1 = ParseQuery.getQuery("_User");
                        query1.getInBackground(msg.getUserId(), new GetCallback<ParseUser>() {
                            public void done(ParseUser user, ParseException e) {
                                if (e == null) {
                                    Glide.with(mContext).load(user.getString("ProfileImage")).asBitmap()
                                            .centerCrop().into(new BitmapImageViewTarget(vh.ivMessageUser) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable circularBitmapDrawable =
                                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                            circularBitmapDrawable.setCircular(true);
                                            vh.ivMessageUser.setImageDrawable(circularBitmapDrawable);
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        Log.e("message", "Error Loading Messages" + e);
                    }
                }
            });
        }else{
            query.getInBackground(chat.getLastMessageId(), new GetCallback<Message>() {
                public void done(Message msg, ParseException e) {
                    if (e == null) {
                        vh.tvMessage.setText(msg.getBody());
                        ParseQuery<ParseUser> query1 = ParseQuery.getQuery("_User");
                        query1.getInBackground(msg.getUserId(), new GetCallback<ParseUser>() {
                            public void done(ParseUser user, ParseException e) {
                                if (e == null) {
                                    Glide.with(mContext).load(user.getString("ProfileImage")).asBitmap()
                                            .centerCrop().into(new BitmapImageViewTarget(vh.ivMessageUser) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable circularBitmapDrawable =
                                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                            circularBitmapDrawable.setCircular(true);
                                            vh.ivMessageUser.setImageDrawable(circularBitmapDrawable);
                                        }
                                    });
                                }
                            }
                        });

                    }
                }
            });
        }
    }

    private void configureViewHolder_single(final ItemChatSingle vh, int position) {
        // Get the data model based on position
        Chat chat = mChats.get(position);
        vh.tvTimestamp.setText(DateTimeHelper.getRelativeTimeAgo(chat.getUpdatedAt()));


        ArrayList<String> users = chat.getRecipients();
        users.remove(ParseUser.getCurrentUser().getObjectId());
        String otherUser = users.get(0);

        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        String lastMessageID = chat.getLastMessageId();
        if(lastMessageID==null ||TextUtils.isEmpty(lastMessageID)) {
            query.whereEqualTo(Message.CHAT_KEY,chat.getObjectId());
            // get the latest 500 messages, order will show up newest to oldest of this group
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<Message>() {
                public void done(List<Message> msgs, ParseException e) {
                    if (e == null&&msgs.size()>0) {
                        Message msg = msgs.get(0);
                        vh.tvMessage.setText(msg.getBody());
                    } else {
                        Log.e("message", "Error Loading Messages" + e);
                    }
                }
            });
        }else{
            query.getInBackground(chat.getLastMessageId(), new GetCallback<Message>() {
                public void done(Message msg, ParseException e) {
                    if (e == null) {
                        vh.tvMessage.setText(msg.getBody());
                    }
                }
            });
        }
       /* ParseQuery<Message> query = ParseQuery.getQuery("Message");
        query.getInBackground(chat.getLastMessageId(), new GetCallback<Message>() {
            public void done(Message msg, ParseException e) {
                if (e == null) {
                    vh.tvLastMessage.setText(msg.getBody());
                }
            }
        });
*/
        ParseQuery<ParseUser> query1 = ParseQuery.getQuery("_User");
        query1.getInBackground(otherUser, new GetCallback<ParseUser>() {
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    vh.tvMessageUserName.setText(user.getString("FullName"));
                    Glide.with(mContext).load(user.getString("ProfileImage")).asBitmap()
                            .centerCrop().into(new BitmapImageViewTarget(vh.ivMessageUser) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            vh.ivMessageUser.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
            }
        });
    }

    public class ItemChatSingle extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView ivMessageUser;
        public TextView tvMessageUserName;
        public TextView tvMessage;
        public TextView tvTimestamp;

        public ItemChatSingle(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ivMessageUser = (ImageView) itemView.findViewById(R.id.ivMessageUser);
            tvMessageUserName = (TextView) itemView.findViewById(R.id.tvMessageUserName );
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            tvTimestamp= (TextView) itemView.findViewById(R.id.tvTimestamp);
        }
    }

    public class ItemChatGroup extends RecyclerView.ViewHolder {

        public TextView tvGroupNum;
        public TextView tvGroupName;
        public ImageView ivMessageUser;
        public TextView tvMessage;
        public TextView tvTimestamp;

        //Define constructor wichi accept entire row and find sub views
        public ItemChatGroup(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            /*tvGroupNum = (TextView) itemView.findViewById(R.id.tvGroupNum);*/
            tvGroupName= (TextView) itemView.findViewById(R.id.tvGroupName);
            ivMessageUser = (ImageView) itemView.findViewById(R.id.ivMessageUser);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            tvTimestamp= (TextView) itemView.findViewById(R.id.tvTimestamp);
        }
    }

}
