package com.codepath.android.instudy.models;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import java.util.Date;


/*
userid
title
link
type
startdate
enddate

 */


@ParseClassName("Event")
public class Event extends ParseObject {

    public static final String USER_ID_KEY = "userid";
    public static final String TITLE_KEY = "title";
    public static final String LINK_KEY = "link";
    public static final String TYPE_KEY = "type";
    public static final String START_DATE_KEY = "startDate";
    public static final String END_DATE_KEY = "endDate";

    public String getUserId() {
        return getString(USER_ID_KEY);
    }
    public void setUserId(String userid) {
        put(USER_ID_KEY, userid);
    }

    public String getTitle() {
        return getString(TITLE_KEY);
    }
    public void setTitle(String title) {
        put(TITLE_KEY, title);
    }


    public String getLink() {
        return getString(LINK_KEY);
    }
    public void setLink(String link) {
        put(LINK_KEY, link);
    }


    public String getType() {
        return getString(TYPE_KEY);
    }
    public void setType(String type) {
        put(TYPE_KEY, type);
    }

    public Date getStartDate() {
        return getDate(START_DATE_KEY);
    }
    public void setStartDate(Date date) {
        put(START_DATE_KEY, date);
    }

    public Date getEndDate() {
        return getDate (END_DATE_KEY);
    }
    public void setEndDate(Date date) {
        put(END_DATE_KEY, date);
    }





}


