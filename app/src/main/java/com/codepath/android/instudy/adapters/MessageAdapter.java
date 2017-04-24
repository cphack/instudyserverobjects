package com.codepath.android.instudy.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
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
import com.codepath.android.instudy.models.Message;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int RIGHT_MSG = 0;
    private static final int LEFT_MSG = 1;
    private static final int RIGHT_MSG_IMG = 2;
    private static final int LEFT_MSG_IMG = 3;

    private ClickListenerChat mClickListenerChat;
    private String mUserId;
    private List<Message> mMessages;
    private Context mContext;


    public MessageAdapter(@NonNull Context context, @NonNull List<Message> objects,ClickListenerChat mClickListenerChat) {
        mMessages = objects;
        mContext = context;
        this.mClickListenerChat=mClickListenerChat;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == RIGHT_MSG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
            return new MessageViewHolder(view);
        } else if (viewType == LEFT_MSG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
            return new MessageViewHolder(view);
        } else if (viewType == RIGHT_MSG_IMG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right_img, parent, false);
            return new MessageViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left_img, parent, false);
            return new MessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MessageViewHolder vh1 = (MessageViewHolder) holder;
        Message msg = mMessages.get(position);
        populateViewHolder(vh1, msg, position);
    }

    @Override
    public int getItemViewType(int position) {
        String userid = ParseUser.getCurrentUser().getObjectId();
        Message msg = mMessages.get(position);
        if (msg.getMapData() != null) {
            if (msg.getUserId().equals(userid)) {
                return RIGHT_MSG_IMG;
            } else {
                return LEFT_MSG_IMG;
            }
        } else if (msg.getFileName() != null) {
            if (msg.getUserId().equals(userid)) {
                return RIGHT_MSG_IMG;
            } else {
                return LEFT_MSG_IMG;
            }
        } else if (msg.getUserId().equals(userid)) {
            return RIGHT_MSG;
        } else {
            return LEFT_MSG;
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public void addMsg(Message m) {
            mMessages.add(0, m);
    }

    public long getLastMessageTime(int position) {
        if( mMessages.size() > 0 ) {
            Date lMsg = mMessages.get(position).getUpdatedAt();
            if (lMsg == null) {
                return 10;
            } // any number greater than 2
            else { // return last updated time in secs
                Log.d("DEBUG", "Now : "+(System.currentTimeMillis()/1000)
                +" lMsg "+(lMsg.getTime()/1000)
                );
                return (System.currentTimeMillis() - lMsg.getTime()) / 1000;
            }
        } else {
            return 10;
        }
    }

    public String getUsrId(int position) {
        if( mMessages.size() > 0 ) {
            return String.valueOf(mMessages.get(position).getUserId());
        } else {
            return "None";
        }
    }

    public String getMsgId(int position) {
        if( mMessages.size() > 0 ) {
            return String.valueOf(mMessages.get(position).getChatId());
        } else {
            return "None";
        }
    }


    protected void populateViewHolder(final MessageViewHolder vh, Message msg, int position) {

        vh.setTxtMessage(msg.getBody());
        vh.setTvTimestamp(msg.getUpdatedAt());
        if (!msg.getUserId().equals(ParseUser.getCurrentUser().getObjectId())) {
            ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
            query.getInBackground(msg.getUserId(), new GetCallback<ParseUser>() {
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        vh.setIvUser(user.getString("ProfileImage"));
                    }
                }
            });
        }

       // vh.tvIsLocation(View.GONE);
        ParseFile imageFile = msg.getFileName();
        if (imageFile != null) {
        //    vh.tvIsLocation(View.GONE);
            vh.setIvChatPhoto(imageFile.getUrl());
        } else if (msg.getMapData() != null) {

        }
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTimestamp, tvLocation;
        TextView txtMessage;
        ImageView ivUser, ivChatPhoto;

        public MessageViewHolder(View itemView) {
            super(itemView);
            tvTimestamp = (TextView) itemView.findViewById(R.id.timestamp);
            txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);
            tvLocation = (TextView) itemView.findViewById(R.id.tvLocation);
            ivChatPhoto = (ImageView) itemView.findViewById(R.id.img_chat);
            ivUser = (ImageView) itemView.findViewById(R.id.ivUserChat);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Message msg = mMessages.get(position);
            if(msg.getFileName()!=null) {
                mClickListenerChat.clickImageChat(view, position, msg);
            }
           /*if (model.getMapData() != null) {
                mClickListenerChatFirebase.clickImageMapChat(view, position, model.getMapModel().getLatitude(), model.getMapModel().getLongitude());
            } else {

            }*/
        }

        public void setTxtMessage(String message) {
            if (txtMessage == null) return;
            txtMessage.setText(message);
        }

        public void setIvUser(String urlPhotoUser) {
            if (ivUser == null) return;
            Glide.with(mContext).load(urlPhotoUser).asBitmap().centerCrop().placeholder(R.drawable.default_user_white).into(new BitmapImageViewTarget(ivUser) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    ivUser.setImageDrawable(circularBitmapDrawable);
                }
            });


        }

        public void setTvTimestamp(Date timestamp) {
            if (tvTimestamp == null) return;
            tvTimestamp.setText(DateTimeHelper.getRelativeTimeAgo(timestamp));
        }

        public void setIvChatPhoto(String url) {
            if (ivChatPhoto == null) return;
            Glide.with(ivChatPhoto.getContext()).load(url)
                    .override(100, 100)
                    .fitCenter()
                    .into(ivChatPhoto);
            ivChatPhoto.setOnClickListener(this);
        }

        public void tvIsLocation(int visible) {
            if (tvLocation == null) return;
            tvLocation.setVisibility(visible);
        }

    }

    public interface ClickListenerChat {

        /**
         * Quando houver click na imagem do chat
         *
         * @param view
         * @param position
         */
        void clickImageChat(View view, int position, Message msg);

        /**
         * Quando houver click na imagem de mapa
         *
         * @param view
         * @param position
         */
        void clickImageMapChat(View view, int position, String latitude, String longitude);

    }


}


