package com.codepath.android.instudy.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Assignment")
public class Assignment extends ParseObject {
    public static final String COURSE_ID_KEY = "courseId";
    public static final String DUE_DATE_KEY = "duDate";
    public static final String DUE_TIME_KEY = "duTime";
    public static final String ASSIGNMENT_KEY = "assignment";
    public static final String ASSIGNMENTDESC_KEY = "assignmentDesc";

    public String getCourseId() {
        return getString(COURSE_ID_KEY);
    }

    public String getDueTime() {
        return getString(DUE_TIME_KEY);
    }

    public String getDueDate() {
        return getString(DUE_DATE_KEY);
    }

    public String getAssignment() {
        return getString(ASSIGNMENT_KEY);
    }

    public String getAssignmentDescription() {
        return getString(ASSIGNMENTDESC_KEY);
    }


    public void setCourseId(String courseId) {
        put(COURSE_ID_KEY, courseId);
    }

    public void setDueTime(String startDate) {
        put(DUE_TIME_KEY, startDate);
    }

    public void setDueDate(String dueDate) {
        put(DUE_DATE_KEY, dueDate);
    }

    public void setAssignment(String link) {
        put(ASSIGNMENT_KEY, link);
    }

    public void setAssignmentDescription(String link) {
        put(ASSIGNMENTDESC_KEY, link);
    }
}
