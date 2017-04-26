package com.codepath.android.instudy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.adapters.AssignmentListAdapter;
import com.codepath.android.instudy.helpers.ItemClickSupport;
import com.codepath.android.instudy.models.Assignment;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class AssignmentListFragment extends Fragment implements EditAssignmentFragment.EditAssignmentDialogListener {
    ArrayList<Assignment> assignments;
    AssignmentListAdapter aAssignments;
    private RecyclerView lvAssignments;
    private FloatingActionButton fab;
    //Button btnAddAssignment;
    private LinearLayoutManager linearLayoutManager;
    String courseId;
    public static AssignmentListFragment newInstance(String courseid) {
        AssignmentListFragment fragment = new AssignmentListFragment();
        Bundle args = new Bundle();
        args.putString("courseid", courseid);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_assignment_list, container, false);
        findControls(v);
        initControls();
        return v;
    }

    //creation lifecycle events
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAdapter();
    }

    private void findControls(View v) {
        lvAssignments = (RecyclerView) v.findViewById(R.id.lvAssignments);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        fab = (FloatingActionButton) v.findViewById(R.id.fbAddAssignment);
//        btnAddAssignment = (Button) v.findViewById(R.id.btnAddAssignment);
//        btnAddAssignment.setVisibility(View.INVISIBLE);
    }

    private void initControls() {        //connect adapter with recyclerView
        lvAssignments.setAdapter(aAssignments);
        lvAssignments.setLayoutManager(linearLayoutManager);

        ItemClickSupport.addTo(lvAssignments).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Assignment assignment = assignments.get(position);
                showEditAssignmentDialog(position, assignment.getObjectId());
            }
        });

//        btnAddAssignment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int pos = aAssignments.getItemCount();
//                if( pos == 0) {
//                    showEditAssignmentDialog(0, "0");
//                } else {
//                    showEditAssignmentDialog(pos, "0");
//                }
//            }
//        });

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int pos = aAssignments.getItemCount();
                if( pos == 0) {
                    showEditAssignmentDialog(0, "0");
                } else {
                    showEditAssignmentDialog(pos, null);
                }
            }
        });
    }

    private void initAdapter() {
        assignments = new ArrayList<Assignment>();
        //construct adapter from datasource
        aAssignments = new AssignmentListAdapter(getActivity(), assignments);
    }

    public void populateAssignmentList(String courseid) {

        courseId = courseid;
        ParseQuery<Assignment> query = ParseQuery.getQuery(Assignment.class);
        query.whereEqualTo(Assignment.COURSE_ID_KEY, courseid);

        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Assignment>() {
            public void done(List<Assignment> itemList, ParseException e) {
                if (e == null) {
                    assignments.clear();
                    assignments.addAll(itemList);
                    aAssignments.notifyDataSetChanged();

                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


//        // Specify which class to query
//        ParseQuery<Course> query1 = ParseQuery.getQuery(Course.class);
//        // Specify the object id
//        query1.getInBackground(courseid, new GetCallback<Course>() {
//            public void done(Course course, ParseException e) {
//                if (e == null) {
//                    // Access data using the `get` methods for the object
//                    if (course.getTeachers().equals(ParseUser.getCurrentUser().getObjectId())) {
//                        btnAddAssignment.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    // something went wrong
//                }
//            }
//        });
    }


    private void showEditAssignmentDialog(int position, String assignmentId) {
        FragmentManager fm = getFragmentManager();
        EditAssignmentFragment editAssignmentDialogFragment = EditAssignmentFragment.newInstance(position,assignmentId);
        editAssignmentDialogFragment.setTargetFragment(AssignmentListFragment.this, 300);
        editAssignmentDialogFragment.show(fm, "fragment_edit_assignment");
    }

    @Override
    public void onFinishEditDialog(final int position,final String assignmentName, final String assignmentDescription, final String dueDate, final String dueTime, String assignmentId) {


        Assignment a;
        if(assignmentId.equals("0")){
            a = new Assignment();
            a.setCourseId(courseId);
            a.setAssignment(assignmentName);
            a.setAssignmentDescription(assignmentDescription);
            a.setDueDate(dueDate);
            a.setDueTime(dueTime);
            a.saveInBackground();
            aAssignments.addAssignment(0,a);
            aAssignments.notifyDataSetChanged();
        } else if(assignmentId == null){
            a = new Assignment();
            a.setCourseId(courseId);
            a.setAssignment(assignmentName);
            a.setAssignmentDescription(assignmentDescription);
            a.setDueDate(dueDate);
            a.setDueTime(dueTime);
            a.saveInBackground();
            aAssignments.addAssignment(position,a);
            aAssignments.notifyDataSetChanged();
        } else{
            ParseQuery<Assignment> query = ParseQuery.getQuery(Assignment.class);
            // Specify the object id
            query.getInBackground(assignmentId, new GetCallback<Assignment>() {
                public void done(Assignment a, ParseException e) {
                    if (e == null) {
                        a.setCourseId(courseId);
                        a.setAssignment(assignmentName);
                        a.setAssignmentDescription(assignmentDescription);
                        a.setDueDate(dueDate);
                        a.setDueTime(dueTime);
                        a.saveInBackground();
                        aAssignments.addAssignment(position,a);
                        aAssignments.notifyDataSetChanged();
                    } else {
                        // something went wrong
                    }
                }
            });
        }
    }
}

