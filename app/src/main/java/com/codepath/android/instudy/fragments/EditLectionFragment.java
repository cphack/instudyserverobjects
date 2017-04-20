package com.codepath.android.instudy.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.models.Lection;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.Calendar;
// ...

public class EditLectionFragment extends DialogFragment  {
    Button btnUpdate;
    EditText etLectionName;
    EditText etOverview;
    EditText etDate;
    EditText etTime;
    DatePicker dPStart;
    TimePicker tPStart;
    int mHour;
    int mMinute;
    private int cYear;
    String lectionId="0";
    String newLectionName;
    String newOverview;
    String newDate;
    String newTime;

    public EditLectionFragment() {
    }

    public static EditLectionFragment newInstance(String lectionId) {
        EditLectionFragment frag = new EditLectionFragment();
        Bundle args = new Bundle();
        args.putString("lectionid", lectionId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_lection, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view

        etLectionName=(EditText) view.findViewById(R.id.etLectionName);
        etOverview=(EditText) view.findViewById(R.id.etOverview);
        etDate=(EditText) view.findViewById(R.id.etDate);
        etTime=(EditText) view.findViewById(R.id.etTime);
        tPStart = (TimePicker) view.findViewById(R.id.tPStart);
        dPStart = (DatePicker) view.findViewById(R.id.dPStart);
        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);

        lectionId = getArguments().getString("lectionid","0");
        if(!lectionId.equals("0")){
            populateFields();
        }else
        {
            Log.d("DEBUG", "First entry of new Lection");
            getNewLection();
            btnUpdate.setText("add lection");
        }
        // Show soft keyboard automatically and request focus to field
        etLectionName.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditLectionDialogListener listener = (EditLectionDialogListener) getTargetFragment();
               //TODO implement data taken from controls
                String startDate = String.valueOf(etDate.getText());
                String StartTime = String.valueOf(etTime.getText());
                listener.onFinishEditDialog(etLectionName.getText().toString(),
                        etOverview.getText().toString(),
                        startDate,StartTime,lectionId);
                dismiss();
                Log.d("DEBUG", "Exiting butUpdate On click listener Lection");
            }
        });
    }

    private void getNewLection() {
        etLectionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newLectionName = String.valueOf(etLectionName.getText());
            }
        });
        etLectionName.setText(newLectionName);
        etOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newOverview = String.valueOf(etOverview.getText());
            }
        });
        etOverview.setText(newOverview);
        Calendar cal = Calendar.getInstance();
        cYear = cal.get(Calendar.YEAR);
        MyOnDateChangeListener onDateChangeListener = new MyOnDateChangeListener();
        dPStart.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH), onDateChangeListener);
        tPStart.clearFocus();
        tPStart.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                int hour = hourOfDay;
                int min = minute;
                String HH_MM= String.valueOf(hour)+":"+ String.valueOf(min);
                etTime.setText(HH_MM);
            }
        });
    }

    private void populateFields()
    {
        // Specify which class to query
        ParseQuery<Lection> query = ParseQuery.getQuery(Lection.class);
        // Specify the object id
        query.getInBackground(lectionId, new GetCallback<Lection>() {

            @SuppressLint("NewApi")
            public void done(Lection l, ParseException e) {
                if (e == null) {
                    etLectionName.setText(l.getTitle());
                    etOverview.setText(l.getLocation());
                    Calendar cal = Calendar.getInstance();
                    cYear = cal.get(Calendar.YEAR);
                    MyOnDateChangeListener onDateChangeListener = new MyOnDateChangeListener();
                    dPStart.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH), onDateChangeListener);
                    tPStart.clearFocus();
                    tPStart.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                        @Override
                        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                            int hour = hourOfDay;
                            int min = minute;
                            String HH_MM= String.valueOf(hour)+":"+ String.valueOf(min);
                            etTime.setText(HH_MM);
                        }
                    });
                } else {
                    // something went wrong
                }
            }
        });
    }

    public interface EditLectionDialogListener {
        void onFinishEditDialog(String title, String overview, String startDate, String startTime, String lectionid);
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
            etDate.setText(mon+"-"+day);
        }
    }


}