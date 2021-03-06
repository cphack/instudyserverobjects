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
import com.codepath.android.instudy.adapters.LectionListAdapter;
import com.codepath.android.instudy.helpers.ItemClickSupport;
import com.codepath.android.instudy.models.Lection;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class LectionListFragment extends Fragment implements EditLectionFragment.EditLectionDialogListener {
    ArrayList<Lection> lections;
    LectionListAdapter aLections;
    private RecyclerView lvLections;
    private FloatingActionButton fab;
    //Button btnAddLection;
    private LinearLayoutManager linearLayoutManager;
    String courseId;
    public static LectionListFragment newInstance(String courseid) {
        LectionListFragment fragment = new LectionListFragment();
        Bundle args = new Bundle();
        args.putString("courseid", courseid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lection_list, container, false);
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
        lvLections = (RecyclerView) v.findViewById(R.id.lvLections);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        fab = (FloatingActionButton) v.findViewById(R.id.fbAddLection);
//        btnAddLection = (Button) v.findViewById(R.id.btnAddLection);
//        btnAddLection.setVisibility(View.INVISIBLE);
    }

    private void initControls() {        //connect adapter with recyclerView
        lvLections.setAdapter(aLections);
        lvLections.setLayoutManager(linearLayoutManager);
        ItemClickSupport.addTo(lvLections).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Lection lection = lections.get(position);
                showEditLectionDialog(position, lection.getObjectId());
            }
        });

//        btnAddLection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int pos = aLections.getItemCount();
//                if( pos == 0) {
//                    showEditLectionDialog(0, "0");
//                } else {
//                    showEditLectionDialog(pos, null);
//                }
//            }
//        });
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int pos = aLections.getItemCount();
                if( pos == 0) {
                    showEditLectionDialog(0, "0");
                } else {
                    showEditLectionDialog(pos, null);
                }
            }
        });

    }

    private void initAdapter() {
        lections = new ArrayList<Lection>();
        //construct adapter from datasource
        aLections = new LectionListAdapter(getActivity(), lections);
    }

    public void populateLectionList(String courseid) {

        courseId = courseid;
        ParseQuery<Lection> query = ParseQuery.getQuery(Lection.class);
        query.whereEqualTo(Lection.COURSE_ID_KEY, courseid);
        query.orderByAscending(Lection.START_DATE_KEY);

        // Execute the find asynchronously
        query.findInBackground(new FindCallback<Lection>() {
            public void done(List<Lection> itemList, ParseException e) {
                if (e == null) {
                    lections.clear();
                    lections.addAll(itemList);
                    aLections.notifyDataSetChanged();
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
//                        btnAddLection.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    // something went wrong
//                }
//            }
//        });
    }


    private void showEditLectionDialog(int position, String lectionId) {
        FragmentManager fm = getFragmentManager();
        EditLectionFragment editLectionDialogFragment = EditLectionFragment.newInstance(position, lectionId);
        editLectionDialogFragment.setTargetFragment(LectionListFragment.this, 300);
        editLectionDialogFragment.show(fm, "fragment_edit_lection");
    }

    @Override
    public void onFinishEditDialog(final int position, final String title, final String overview, final String startDate, final String startTime,String lectionId) {
        Lection lection;
        if(lectionId.equals("0")){
            lection = new Lection();
            lection.setCourseId(courseId);
            lection.setLocation(overview);
            lection.setTitle(title);
            lection.setStartDate(startDate);
            lection.setStartTime(startTime);
            lection.saveInBackground();
            aLections.addLection(0,lection);
            aLections.notifyDataSetChanged();
        } else if (lectionId == null ) {
            lection = new Lection();
            lection.setCourseId(courseId);
            lection.setLocation(overview);
            lection.setTitle(title);
            lection.setStartDate(startDate);
            lection.setStartTime(startTime);
            lection.saveInBackground();
            aLections.addLection(position,lection);
            aLections.notifyDataSetChanged();
        } else{
            ParseQuery<Lection> query = ParseQuery.getQuery(Lection.class);
            // Specify the object id
            query.getInBackground(lectionId, new GetCallback<Lection>() {
                public void done(Lection l, ParseException e) {
                    if (e == null) {
                        l.setCourseId(courseId);
                        l.setLocation(overview);
                        l.setTitle(title);
                        l.setStartDate(startDate);
                        l.setStartTime(startTime);
                        l.saveInBackground();
                        aLections.addLection(position,l);
                        aLections.notifyDataSetChanged();
                    } else {
                        // something went wrong
                    }
                }
            });
        }
    }
}

