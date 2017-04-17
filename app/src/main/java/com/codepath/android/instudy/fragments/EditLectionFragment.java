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
import android.widget.Toast;

import com.codepath.android.instudy.R;
import com.codepath.android.instudy.models.Lection;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codepath.android.instudy.R.id.etStatusUpdate;
import static com.codepath.android.instudy.R.id.start;
// ...

public class EditLectionFragment extends DialogFragment  {
    Button btnUpdate;
    EditText etLectionName;
    EditText etOverview;
    EditText etDate;
    EditText etTime;
    String lectionId="0";

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
        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);

        lectionId = getArguments().getString("lectionid","0");
        if(!lectionId.equals("0")){
            populateFields();
        }else
        {
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
                Date startDate = new Date();
                listener.onFinishEditDialog(etLectionName.getText().toString(),
                        etOverview.getText().toString(),
                        startDate,lectionId);
                dismiss();
            }
        });
    }

    private void populateFields()
    {
        // Specify which class to query
        ParseQuery<Lection> query = ParseQuery.getQuery(Lection.class);
        // Specify the object id
        query.getInBackground(lectionId, new GetCallback<Lection>() {
            public void done(Lection l, ParseException e) {
                if (e == null) {

                    etLectionName.setText(l.getTitle());
                    etOverview.setText(l.getLocation());
                    DateFormat datef = new SimpleDateFormat("MMM-dd");
                    DateFormat timef = new SimpleDateFormat(" HH:mm");
                    etDate.setText(datef.format(l.getStartDate()));
                    etTime.setText(timef.format(l.getStartDate()));

                } else {
                    // something went wrong
                }
            }
        });
    }

    public interface EditLectionDialogListener {
        void onFinishEditDialog(String title, String overview,Date startDate,String lectionid);
    }


}