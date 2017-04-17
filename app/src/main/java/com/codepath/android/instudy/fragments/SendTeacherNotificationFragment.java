package com.codepath.android.instudy.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.android.instudy.R;

import static com.codepath.android.instudy.R.id.btnUpdate;
import static com.codepath.android.instudy.R.id.etStatusUpdate;
// ...

public class SendTeacherNotificationFragment extends DialogFragment   {

    private EditText etMessage;
    Button btnSend;
    String courseId;
    public interface SendNotificationListener {
        void onFinishEditDialog(String message,String courseid);
    }


    public SendTeacherNotificationFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static SendTeacherNotificationFragment newInstance(String courseid) {
        SendTeacherNotificationFragment frag = new SendTeacherNotificationFragment();
        Bundle args = new Bundle();
        args.putString("courseid", courseid);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send_notification, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etMessage= (EditText) view.findViewById(R.id.etMessage);
        btnSend = (Button) view.findViewById(R.id.btnSend);

        courseId = getArguments().getString("courseid");

        // Show soft keyboard automatically and request focus to field
        etMessage.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendNotificationListener listener = (SendNotificationListener) getActivity();
                if(listener!=null) {
                    listener.onFinishEditDialog(etMessage.getText().toString(),courseId);
                }
                // Close the dialog and return back to the parent activity
                dismiss();
            }
        });
    }
}