package com.codepath.android.instudy.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.codepath.android.instudy.R.id.tvStartDate;

/**
 * Created by alex_ on 4/9/2017.
 */


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    Boolean isStartDate;
    Calendar cStartDate;

    public static DatePickerFragment newInstance(boolean isStartDate) {
        DatePickerFragment frag = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putBoolean("isstartdate", isStartDate);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        isStartDate= getArguments().getBoolean("isstartdate");

        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Activity needs to implement this interface
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) this;

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        cStartDate = Calendar.getInstance();
        cStartDate.set(Calendar.YEAR, year);
        cStartDate.set(Calendar.MONTH, monthOfYear);
        cStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        sendBackResult(cStartDate.getTime());
    }


   public interface DatePickerFragmentListener {
        public void onSetStartDate(Date date);
       public void onSetEndDate(Date date);
    }
    // Call this method to send the data back to the parent fragment
    public void sendBackResult(Date date) {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        DatePickerFragmentListener listener = (DatePickerFragmentListener) getTargetFragment();

        if(isStartDate){
        listener.onSetStartDate(date);
        }else{
            listener.onSetEndDate(date);
        }
        dismiss();
    }


}