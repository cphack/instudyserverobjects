package com.codepath.android.instudy.models;

import com.parse.ParseClassName;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/*


courseid
startdate
duedate
link
 */


@ParseClassName("Assignment")
public class Assignment extends ParseObject {
    public static final String COURSE_ID_KEY = "courseId";
    public static final String START_DATE_KEY = "startDate";
    public static final String DUE_DATE_KEY = "dueDate";
    public static final String LINK_KEY = "link";

    public String getCourseId() {
        return getString(COURSE_ID_KEY);
    }

    public Date getStartDate() {
        return getDate(START_DATE_KEY);
    }

    public Date getDueDate() {
        return getDate(DUE_DATE_KEY);
    }

    public String getLink() {
        return getString(LINK_KEY);
    }


    public void setCourseId(String courseId) {
        put(COURSE_ID_KEY, courseId);
    }

    public void setStartDate(Date startDate) {
        put(START_DATE_KEY, startDate);
    }

    public void setDueDate(Date dueDate) {
        put(DUE_DATE_KEY, dueDate);
    }

    public void setLink(String link) {
        put(LINK_KEY, link);
    }
}
