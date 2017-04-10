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

    Calendar cStartDate;

   /* public DatePickerFragment() {
        listener = null;
    }*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
        //((NewCourseFragment)this.getParentFragment()).onSetDate(cStartDate.getTime());


        /*SimpleDateFormat formater = new SimpleDateFormat("MMM-dd-yyyy");
        isStartDateSet=true;
        tvStartDate.setText(formater.format(cStartDate.getTime()));*/
       /* if (listener != null) {
            listener.onSetDate(cStartDate.getTime());
        }*/
    }


   public interface DatePickerFragmentListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onSetDate(Date date);
    }

    /*// Assign the listener implementing events interface that will receive the events
    public void setDatePickerFragmentListener(DatePickerFragmentListener listener) {
        this.listener = listener;
    }*/

   // private DatePickerFragmentListener listener;

    // Call this method to send the data back to the parent fragment
    public void sendBackResult(Date date) {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        DatePickerFragmentListener listener = (DatePickerFragmentListener) getTargetFragment();
        listener.onSetDate(date);
        dismiss();
    }


}