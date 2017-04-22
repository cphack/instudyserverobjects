package com.codepath.android.instudy.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.models.Assignment;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
// ...

public class EditAssignmentFragment extends DialogFragment  {
    Button btnUpdate;
    EditText etAssignmentName;
    EditText etAssignmentDescription;
    EditText etDueDate;
    EditText etDueTime;
    DatePicker dPDueDate;
    TimePicker tPDueTime;
    int mHour;
    int mMinute;
    private int cYear;
    String assignmentId="0";
    String newAssignmentName;
    String newAssignmentDescription;

    public EditAssignmentFragment() {
    }

    public static EditAssignmentFragment newInstance(int position,String assignmentId) {
        EditAssignmentFragment frag = new EditAssignmentFragment();
        Bundle args = new Bundle();
        args.putString("assignmentid", assignmentId);
        args.putInt("position",position);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_assignment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view

        etAssignmentName=(EditText) view.findViewById(R.id.etAssignmentName);
        etAssignmentDescription=(EditText) view.findViewById(R.id.etAssignmentDescription);
        etDueDate=(EditText) view.findViewById(R.id.etDueDate);
        etDueTime=(EditText) view.findViewById(R.id.etDueTime);
        tPDueTime = (TimePicker) view.findViewById(R.id.tPDueTime);
        dPDueDate = (DatePicker) view.findViewById(R.id.dpDueDate);
        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);

        assignmentId = getArguments().getString("assignmentid","0");
        final int pos = getArguments().getInt("position");
        if(!assignmentId.equals("0")){
            populateFields();
        }else
        {
            getNewAssignment();
            btnUpdate.setText("add assignment");
        }
        // Show soft keyboard automatically and request focus to field
        etAssignmentName.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditAssignmentDialogListener listener = (EditAssignmentDialogListener) getTargetFragment();
               //TODO implement data taken from controls
                String dueDate = String.valueOf(etDueDate.getText());;
                String dueTime = String.valueOf(etDueTime.getText());;
                listener.onFinishEditDialog(pos,etAssignmentName.getText().toString(),
                        etAssignmentDescription.getText().toString(),
                        dueDate,dueTime, assignmentId);
                dismiss();
            }
        });
    }
    private void getNewAssignment() {
        final NumberFormat formatter = new DecimalFormat("00");
        etAssignmentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAssignmentName = String.valueOf(etAssignmentName.getText());
            }
        });

        etAssignmentDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAssignmentDescription = String.valueOf(etAssignmentDescription.getText());
            }
        });

        Calendar cal = Calendar.getInstance();
        cYear = cal.get(Calendar.YEAR);
        String MM_DD = formatter.format(cal.get(Calendar.MONTH)+1)+"-"+formatter.format(cal.get(Calendar.DAY_OF_MONTH));
        etDueDate.setText(MM_DD);
        Time now = new Time();
        String hh_mm= formatter.format(now.hour)+":"+ formatter.format(now.minute);
        etDueTime.setText(hh_mm);
        MyOnDateChangeListener onDateChangeListener = new MyOnDateChangeListener();
        dPDueDate.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH), onDateChangeListener);
        tPDueTime.clearFocus();
        tPDueTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
                String HH_MM= formatter.format(mHour)+":"+ formatter.format(mMinute);
                etDueTime.setText(HH_MM);
            }
        });
    }
    private void populateFields()
    {
        final NumberFormat formatter = new DecimalFormat("00");
        // Specify which class to query
        ParseQuery<Assignment> query = ParseQuery.getQuery(Assignment.class);
        // Specify the object id
        query.getInBackground(assignmentId, new GetCallback<Assignment>() {
            public void done(Assignment a, ParseException e) {
                if (e == null) {
                    etAssignmentName.setText(a.getAssignment());
                    etAssignmentDescription.setText(a.getAssignmentDescription());
                    etDueDate.setText(a.getDueDate());
                    etDueTime.setText(a.getDueTime());
                    Calendar cal = Calendar.getInstance();
                    cYear = cal.get(Calendar.YEAR);
                    if(a.getDueDate()!=null ) {
                        etDueDate.setText(a.getDueDate());
                    } else {
                        String MM_DD = formatter.format(cal.get(Calendar.MONTH)+1)+"-"+formatter.format(cal.get(Calendar.DAY_OF_MONTH));
                        etDueDate.setText(MM_DD);
                    }
                    if(a.getDueTime()!=null ) {
                        etDueTime.setText(a.getDueTime());
                    } else {
                        Time now = new Time();
                        String hh_mm= formatter.format(now.hour)+":"+ formatter.format(now.minute);
                        etDueTime.setText(hh_mm);
                    }
                    MyOnDateChangeListener onDateChangeListener = new MyOnDateChangeListener();
                    dPDueDate.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH), onDateChangeListener);
                    tPDueTime.clearFocus();
                    tPDueTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            mHour = hourOfDay;
                            mMinute = minute;
                            String HH_MM= formatter.format(mHour)+":"+ formatter.format(mMinute);
                            etDueTime.setText(HH_MM);
                        }
                    });
                } else {
                    // something went wrong
                }
            }
        });
    }

    public interface EditAssignmentDialogListener {
        void onFinishEditDialog(final int position,String assignmentName, String assignmentDescription, String dueDate, String dueTime, String assignmentid);
    }

    // Get date picker's status change and reflect into due date set
    // Make sure that we check for date in the past and not allow a past date
    public class MyOnDateChangeListener implements DatePicker.OnDateChangedListener {
        @Override
        public void onDateChanged(DatePicker view, int year, int month, int day) {
            int mon=month+1;
            if(year < cYear) {
                year=cYear;
            }
            etDueDate.setText(mon+"-"+day);
        }
    }

}