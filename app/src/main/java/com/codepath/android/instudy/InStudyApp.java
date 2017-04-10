package com.codepath.android.instudy;

import android.app.Application;
import android.content.Context;

import com.codepath.android.instudy.models.Assignment;
import com.codepath.android.instudy.models.Course;
import com.codepath.android.instudy.models.Lection;
import com.codepath.android.instudy.models.Message;
import com.codepath.android.instudy.models.Profile;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.interceptors.ParseLogInterceptor;


/**
 * Created by hkanekal on 4/6/2017.
 */

public class InStudyApp extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        InStudyApp.context = this;

        // Register your parse models here
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(Course.class);
        ParseObject.registerSubclass(Lection.class);
        ParseObject.registerSubclass(Assignment.class);
        ParseObject.registerSubclass(Profile.class);
        // Existing initialization happens after all classes are registere

        // set applicationId and server based on the values in the Heroku settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("myAppId") // should correspond to APP_ID env variable
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("https://instudy.herokuapp.com/parse/").build());
    }

}