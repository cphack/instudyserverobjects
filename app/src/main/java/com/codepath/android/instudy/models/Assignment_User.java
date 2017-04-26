package com.codepath.android.instudy.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import javax.security.auth.Subject;

import static com.codepath.android.instudy.models.Assignment.ASSIGNMENTDESC_KEY;
import static com.codepath.android.instudy.models.Assignment.DUE_DATE_KEY;
import static com.codepath.android.instudy.models.Assignment.DUE_TIME_KEY;

@ParseClassName("Assignment_User")
public class Assignment_User extends ParseObject {
    public static final String ASSIGNMENT_ID_KEY = "assignment_id";
    public static final String USER_ID_KEY = "user_id";
    public static final String ISSUBMIT_KEY= "wasSubmit";

    public String getAssignment() {
        return getString(ASSIGNMENT_ID_KEY);
    }
    public void setAssignment(String val) {
       put(ASSIGNMENT_ID_KEY,val);
    }

    public String getUserId() {
        return getString(USER_ID_KEY);
    }
    public void setUserId(String val) {
        put(USER_ID_KEY,val);
    }

    public Boolean wasSubmited() {
        return getBoolean(ISSUBMIT_KEY);
    }
    public void setSubmit(Boolean val) {
        put(ISSUBMIT_KEY,val);
    }

}
