package com.codepath.android.instudy.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.models.Lection;

import java.util.List;


//Taking the tweets object and turning them into views displayed in the list;
public class LectionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Lection> mLections;
    // Store the context for easy access
    private Context mContext;

    public LectionListAdapter(@NonNull Context context, @NonNull List<Lection> objects) {
        mLections= objects;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mLections.size();
    }

    // inflate xml layout and return  viewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        View v1 = inflater.inflate(R.layout.item_lection, parent, false);
        return new ItemLection(v1);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ItemLection vh = (ItemLection) viewHolder;
        configureItem(vh, position);
    }

    private void configureItem(final ItemLection vh, int position) {

        Lection l = mLections.get(position);

        vh.tvLectionName.setText(l.getTitle());
        vh.tvLocation.setText(l.getLocation());

//        DateFormat datef = new SimpleDateFormat("MMM-dd");
////        DateFormat timef = new SimpleDateFormat(" HH:mm");
//        vh.tvDate.setText(datef.format(l.getStartDate()));
//        vh.tvTime.setText(timef.format(l.getStartDate()));
        Log.d("DEBUG","Adapter sD "+l.getStartDate()+" sT "+l.getStartTime());
        vh.tvDate.setText(l.getStartDate());
        vh.tvTime.setText(l.getStartTime());
    }

    public  class ItemLection extends  RecyclerView.ViewHolder {

        public TextView tvDate;
        public TextView tvTime;
        public TextView tvLectionName;
        public TextView tvLocation;

        //Define constructor wichi accept entire row and find sub views
        public ItemLection(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvDate=(TextView) itemView.findViewById(R.id.tvDate);
            tvTime=(TextView) itemView.findViewById(R.id.tvTime);
            tvLectionName=(TextView) itemView.findViewById(R.id.tvLectionName);
            tvLocation=(TextView) itemView.findViewById(R.id.tvLocation);
        }
    }

}
