package com.codepath.android.instudy.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.models.Event;
import com.parse.ParseUser;

import java.util.Date;

import static com.codepath.android.instudy.R.id.btnUpdate;
import static com.codepath.android.instudy.R.id.etStatusUpdate;
// ...

public class AddEventFragment extends DialogFragment {

    public interface AddEventDialogListener {
        void onCloseDialog();
    }


    private EditText etEventName, etLink;
    private TextView tvStartDate, tvEndDate;
    private RadioButton rbWork, rbChallenge, rbConference, rbCourse;
    Date startDate,endDate;
    Boolean isStartDateSet=false,isEndDateSet=false;


    Button btnAddEvent;

    public AddEventFragment() {
    }

    public static AddEventFragment newInstance() {
        AddEventFragment frag = new AddEventFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_new_event, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEventName = (EditText) view.findViewById(R.id.etEventName);
        etLink = (EditText) view.findViewById(R.id.etLink);
        rbWork = (RadioButton) view.findViewById(R.id.rbWork);
        rbChallenge = (RadioButton) view.findViewById(R.id.rbChallenge);
        rbConference = (RadioButton) view.findViewById(R.id.rbConference);
        rbCourse = (RadioButton) view.findViewById(R.id.rbCourse);
        //// TODO: 4/24/2017  add dates

        btnAddEvent = (Button) view.findViewById(R.id.btnAddEvent);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveEvent()) {
                    AddEventDialogListener listener = (AddEventDialogListener) getTargetFragment();
                    listener.onCloseDialog();
                    dismiss();
                }
            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private Boolean saveEvent() {

        //let's make title required field

        String eventName = etEventName.getText().toString();
        if(eventName==null|| TextUtils.isEmpty(eventName)){
            Toast.makeText(getContext(),"Please provide event name ",Toast.LENGTH_SHORT).show();
            return false;
        }
        Event e = new Event();
        e.setUserId(ParseUser.getCurrentUser().getObjectId());
        e.setTitle(eventName);
        e.setLink(etLink.getText().toString());

        String type = "other";
        if(rbWork.isChecked()){type="work";}
        else if(rbChallenge.isChecked()){type="challenge";}
        else if(rbConference.isChecked()){type="conference";}
        else if(rbCourse.isChecked()){type="course";}

        e.setType(type);

        if(isStartDateSet){
            e.setStartDate(this.startDate);
        }else{
            e.setStartDate(new Date());
        }

        if(isEndDateSet){
            e.setEndDate(this.endDate);
        }

        e.saveInBackground();
        return true;


    }
}