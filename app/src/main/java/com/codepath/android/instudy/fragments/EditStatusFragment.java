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
// ...

public class EditStatusFragment extends DialogFragment  implements TextView.OnEditorActionListener {

    private EditText etStatusUpdate;
    Button btnUpdate;
    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }


    public EditStatusFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditStatusFragment newInstance(String status) {
        EditStatusFragment frag = new EditStatusFragment();
        Bundle args = new Bundle();
        args.putString("status", status);
        frag.setArguments(args);
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
        // Show soft keyboard automatically and request focus to field
        etStatusUpdate.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        etStatusUpdate.setOnEditorActionListener(this);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditNameDialogListener listener = (EditNameDialogListener) getParentFragment();
                if(listener!=null) {
                    listener.onFinishEditDialog(etStatusUpdate.getText().toString());
                }
                // Close the dialog and return back to the parent activity
                dismiss();
            }
        });

    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text back to activity through the implemented listener
            EditNameDialogListener listener = (EditNameDialogListener) getActivity();
            listener.onFinishEditDialog(etStatusUpdate.getText().toString());
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }

}