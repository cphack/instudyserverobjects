package com.codepath.android.instudy.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.models.Assignment;

import java.util.List;


//Taking the tweets object and turning them into views displayed in the list;
public class AssignmentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Assignment> mAssignments;
    // Store the context for easy access
    private Context mContext;

    public AssignmentListAdapter(@NonNull Context context, @NonNull List<Assignment> objects) {
        mAssignments= objects;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mAssignments.size();
    }

    public void addAssignment(int position, Assignment a) {
        if(position == 0 ) { // only if there are no lections
            mAssignments.add(position, a);
        } else { // even if position 0, but lecion exists first delete then add
            mAssignments.remove(position);
            mAssignments.add(position, a);
        }
    }

    // inflate xml layout and return  viewHolder
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        View v1 = inflater.inflate(R.layout.item_assignment, parent, false);
        return new ItemAssignment(v1);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ItemAssignment vh = (ItemAssignment) viewHolder;
        configureItem(vh, position);
    }

    private void configureItem(final ItemAssignment vh, int position) {

        Assignment a = mAssignments.get(position);

        vh.tvAssignmentName.setText(a.getAssignment());
        vh.tvAssignmentDescription.setText(a.getAssignmentDescription());
        vh.tvDate.setText(a.getDueDate());
        vh.tvTime.setText(a.getDueTime());
    }

    public  class ItemAssignment extends  RecyclerView.ViewHolder {

        public TextView tvDate;
        public TextView tvTime;
        public TextView tvAssignmentName;
        public TextView tvAssignmentDescription;

        //Define constructor wichi accept entire row and find sub views
        public ItemAssignment(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            tvDate=(TextView) itemView.findViewById(R.id.tvDate);
            tvTime=(TextView) itemView.findViewById(R.id.tvTime);
            tvAssignmentName=(TextView) itemView.findViewById(R.id.tvAssignmentName);
            tvAssignmentDescription=(TextView) itemView.findViewById(R.id.tvAssignmentDescription);
        }
    }

}
