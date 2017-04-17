package com.codepath.android.instudy.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.activities.MainActivity;
import com.codepath.android.instudy.models.Course;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.data;
import static android.R.id.message;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.codepath.android.instudy.R.drawable.user;
import static com.codepath.android.instudy.R.id.tvEndDate;
import static com.codepath.android.instudy.R.id.tvStartDate;

public class NewCourseFragment extends Fragment implements DatePickerFragment.DatePickerFragmentListener {

    EditText etName;
    EditText etDescription;
    Button btnSave;
    boolean isStartDateSet = false;
    boolean isEndDateSet = false;
    TextView dtStartDate;
    TextView dtEndDate;
    Date dStartDate;
    Date dEndDate;

    ParseUser currentUser;// = ParseUser.getCurrentUser();

    public NewCourseFragment() {
        listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_new_course, container, false);
        // Inflate the layout for this fragment
        etName = (EditText) v.findViewById(R.id.etName);
        etDescription = (EditText) v.findViewById(R.id.etDescription);

        dtStartDate = (TextView) v.findViewById(R.id.dtStartDate);
        dtEndDate = (TextView) v.findViewById(R.id.dtEndDate);

        dtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v, true);
            }
        });
        dtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v, false);
            }
        });

        btnSave = (Button) v.findViewById(R.id.btnSaveCourse);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCourse();
                if (listener != null) {
                    listener.onSaveCourse();
                }
            }
        });

        return v;
    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v, boolean isStartDate) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(isStartDate);
        newFragment.setTargetFragment(NewCourseFragment.this, 300);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onSetStartDate(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat("MMM-dd-yyyy");
        isStartDateSet = true;
        dStartDate = date;
        dtStartDate.setText(formater.format(date));
    }

    @Override
    public void onSetEndDate(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat("MMM-dd-yyyy");
        isEndDateSet = true;
        dEndDate = date;
        dtEndDate.setText(formater.format(date));
    }

    private void saveCourse() {

        String name = etName.getText().toString();
        String description = etDescription.getText().toString();

        Boolean isError = false;
        if(TextUtils.isEmpty(name)){
            Toast.makeText(getActivity(),"Please enter name fo the course",
                    Toast.LENGTH_SHORT).show();
            isError=true;
        }

        if(TextUtils.isEmpty(name)){
            Toast.makeText(getActivity(),"Please enter short description fo the course",
                    Toast.LENGTH_SHORT).show();
            isError=true;
        }

        if(!isStartDateSet){
            Toast.makeText(getActivity(),"Please specify when this course will start",
                    Toast.LENGTH_SHORT).show();
            isError=true;
        }

        if(!isError) {
            Course course = new Course();
            course.setTitle(name);
            course.setDescription(description);
            course.setTeachers(ParseUser.getCurrentUser().getObjectId());
            if (isStartDateSet) {
                course.setStartDate(dStartDate);
            }
            if (isEndDateSet) {
                course.setEndDate(dEndDate);
            }

            course.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null) {
                        closeFragment();
                    } else {
                        Log.e("ERROR", "Failed to save message", e);
                    }
                }
            });
        }
    }

    private void closeFragment(){
        getActivity().finish();
    }

    public interface NewCourseFragmentListener {
        public void onSaveCourse();
    }

    // Assign the listener implementing events interface that will receive the events
    public void setNewCourseFragmentListener(NewCourseFragmentListener listener) {
        this.listener = listener;
    }

    private NewCourseFragmentListener listener;
}
