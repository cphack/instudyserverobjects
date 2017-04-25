package com.codepath.android.instudy.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.android.instudy.R;
import com.parse.ParseUser;
// ...

public class EditStatusFragment extends DialogFragment   {

    private EditText etStatusUpdate;
    Button btnUpdate;

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }



    public EditStatusFragment() {
    }

    public static EditStatusFragment newInstance() {
        EditStatusFragment frag = new EditStatusFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_status, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etStatusUpdate = (EditText) view.findViewById(R.id.etStatusUpdate);
        btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        etStatusUpdate.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        final ParseUser cUser = ParseUser.getCurrentUser();
        String status =cUser.getString("statusLine");
        if(status!=null){
            etStatusUpdate.setText(status);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cUser.put("statusLine",etStatusUpdate.getText().toString());
                cUser.saveInBackground();
                EditNameDialogListener listener = (EditNameDialogListener) getTargetFragment();
                listener.onFinishEditDialog(etStatusUpdate.getText().toString());


                // Close the dialog and return back to the parent activity
                dismiss();
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
}