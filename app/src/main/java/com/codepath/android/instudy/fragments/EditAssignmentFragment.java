package com.codepath.android.instudy.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.models.Assignment;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codepath.android.instudy.R.id.etDueTime;
// ...

public class EditAssignmentFragment extends DialogFragment  {
    Button btnUpdate;
    EditText etAssignmentName;
    EditText etOverview;
    EditText etDate;
    EditText etTime;
    EditText etDueDate;
    EditText etDueTime;

    String assignmentId="0";

    public EditAssignmentFragment() {
    }

    public static EditAssignmentFragment newInstance(String assignmentId) {
        EditAssignmentFragment frag = new EditAssignmentFragment();
        Bundle args = new Bundle();
        args.putString("assignmentid", assignmentId);
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
        etOverview=(EditText) view.findViewById(R.id.etOverview);
        etDate=(EditText) view.findViewById(R.id.etDate);
        etTime=(EditText) view.findViewById(R.id.etTime);
        etDate=(EditText) view.findViewById(R.id.etDueDate);
        etTime=(EditText) view.findViewById(R.id.etDueTime);
        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);

        assignmentId = getArguments().getString("assignmentid","0");
        if(!assignmentId.equals("0")){
            populateFields();
        }else
        {
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
                Date startDate = new Date();
                listener.onFinishEditDialog(etAssignmentName.getText().toString(),
                        etOverview.getText().toString(),
                        startDate,assignmentId);
                dismiss();
            }
        });
    }

    private void populateFields()
    {
        // Specify which class to query
        ParseQuery<Assignment> query = ParseQuery.getQuery(Assignment.class);
        // Specify the object id
        query.getInBackground(assignmentId, new GetCallback<Assignment>() {
            public void done(Assignment l, ParseException e) {
                if (e == null) {

                    etAssignmentName.setText(l.getTitle());

                    DateFormat datef = new SimpleDateFormat("MMM-dd");
                    DateFormat timef = new SimpleDateFormat(" HH:mm");
                    etDate.setText(datef.format(l.getStartDate()));
                    etTime.setText(timef.format(l.getStartDate()));



                    etDueDate.setText(datef.format(l.getDueDate()));
                    etDueTime.setText(timef.format(l.getDueDate()));

                } else {
                    // something went wrong
                }
            }
        });
    }

    public interface EditAssignmentDialogListener {
        void onFinishEditDialog(String title, String overview, Date startDate, String assignmentid);
    }


}