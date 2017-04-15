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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.codepath.android.instudy.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

import static android.R.attr.type;

/**
 * Created by alex_ on 4/10/2017.
 */

//Taking the Course object and turning them into views displayed in the list;
public class UserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Define listener member variable
    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    // Define listener member variable
    private OnChatButtonClickListener chatButtonClickListener;
    // Define the listener interface
    public interface OnChatButtonClickListener {
        void onChatButtonClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnChatButtonClickListener(OnChatButtonClickListener listener) {
        this.chatButtonClickListener = listener;
    }






    private List<ParseObject> mUsers;
    // Store the context for easy access
    private Context mContext;

    public UserListAdapter(@NonNull Context context, @NonNull List<ParseObject> objects) {
        mUsers = objects;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    @Override
    public int getItemViewType(int position) {

        return 1;
    }

    // inflate xml layout and return  viewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v1 = inflater.inflate(R.layout.item_user, parent, false);
        RecyclerView.ViewHolder viewHolder=new ViewHolder_User(v1);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ViewHolder_User vh1 = (ViewHolder_User) viewHolder;
        configure_user_item(vh1, position);
    }

    private void configure_user_item(ViewHolder_User vh1, int position) {
        // Get the data model based on position
        final ViewHolder_User vh=vh1;
        ParseUser user = (ParseUser)mUsers.get(position);
        vh.tvUserName.setText(user.getString("FullName"));
        Glide.with(mContext).load(user.getString("ProfileImage")).asBitmap().centerCrop()
                .into(new BitmapImageViewTarget(vh.ivUser) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                vh.ivUser.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    public  class ViewHolder_User extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvUserName;
        public ImageView ivUser;
        public Button btnChat;

        //Define constructor wichi accept entire row and find sub views
        public ViewHolder_User(final View itemView) {

            super(itemView);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            ivUser = (ImageView) itemView.findViewById(R.id.ivUser);
            btnChat = (Button) itemView.findViewById(R.id.btnChat);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });

            btnChat.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (chatButtonClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            chatButtonClickListener.onChatButtonClick(itemView, position);
                        }
                    }
                }
            });
        }
    }
}
