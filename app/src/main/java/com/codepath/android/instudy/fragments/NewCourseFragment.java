package com.codepath.android.instudy.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.activities.MainActivity;
import com.codepath.android.instudy.models.Course;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.data;
import static android.R.id.message;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.codepath.android.instudy.R.drawable.user;

public class NewCourseFragment extends Fragment implements DatePickerFragment.DatePickerFragmentListener {

    EditText etTitle;
    EditText etDescription;
    Button  btnSave;
    Date dStartDate;
    boolean isStartDateSet=false;
    TextView tvStartDate;
    ParseUser currentUser;// = ParseUser.getCurrentUser();
    public NewCourseFragment(){listener=null;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v  = inflater.inflate(R.layout.fragment_new_course, container, false);
        // Inflate the layout for this fragment
        etTitle = (EditText)v.findViewById(R.id.etTitle);
        etDescription = (EditText) v.findViewById(R.id.etDescription);
        tvStartDate=(TextView)v.findViewById(R.id.tvStartDate);

        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDateDialog(v);
            }
        });

        btnSave = (Button) v.findViewById(R.id.btnSaveCourse);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCourse();
                if(listener!=null){
                    listener.onSaveCourse();
                }
            }
        });
        currentUser = ParseUser.getCurrentUser();
        return v;
    }

    // attach to an onclick handler to show the date picker
    public void showStartDateDialog(View v) {

      /*  FragmentManager fm = getFragmentManager();
        DatePickerFragment editNameDialogFragment = EditNameDialog.newInstance("Some Title");
        // SETS the target fragment for use later when sending results
        editNameDialogFragment.setTargetFragment(MyParentFragment.this, 300);
        editNameDialogFragment.show(fm, "fragment_edit_name");
*/
        DatePickerFragment newFragment = new DatePickerFragment();

        newFragment.setTargetFragment(NewCourseFragment.this, 300);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onSetDate(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat("MMM-dd-yyyy");
        isStartDateSet = true;
        dStartDate = date;
        tvStartDate.setText(formater.format(date));
    }

    private void saveCourse(){

        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();

        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        course.setTeachers(ParseUser.getCurrentUser().getObjectId());
        if(isStartDateSet) {
            course.setStartDate(dStartDate);
        }

        course.saveInBackground();
    }




    public interface NewCourseFragmentListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onSaveCourse();
    }

    // Assign the listener implementing events interface that will receive the events
    public void setNewCourseFragmentListener(NewCourseFragmentListener listener) {
        this.listener = listener;
    }

    private NewCourseFragmentListener listener;

}
