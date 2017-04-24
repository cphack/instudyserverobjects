package com.codepath.android.instudy.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.codepath.android.instudy.R;


public class SettingsActivity extends AppCompatActivity {

    Toolbar toolbar;
    Switch switchSettings1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        switchSettings1 = (Switch)findViewById(R.id.swShowTeacherTab);

        //try to initialize from  SharedPreferences

        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(this);
        String settings1 = pref.getString("showTeacherTab", "");
        if(TextUtils.isEmpty(settings1)||settings1.equals("false")){
            switchSettings1.setChecked(false);
        }else{
            switchSettings1.setChecked(true);
        }

        switchSettings1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String settingsValue = switchSettings1.isChecked()?"true":"false";

                SharedPreferences pref =
                        PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("showTeacherTab", settingsValue);
                edit.commit();
            }
        });
    }
}
