package com.codepath.android.instudy.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


/**
 * Created by alex_ on 4/6/2017.
 */


/*

LECTION
title
topics
startdate
location
 */




@ParseClassName("Lection")
public class Lection extends ParseObject {
    public static final String COURSE_ID_KEY = "courseId";
    public static final String TITLE_KEY = "title";
    public static final String TOPICS_KEY = "topics";
    public static final String LOCATION_KEY = "location";
    public static final String START_DATE_KEY = "startDate";
    public static final String START_TIME_KEY = "starTime";

    public String getCourseId() {
        return getString(COURSE_ID_KEY);
    }

    public String getStartDate() {
        return getString(START_DATE_KEY);
    }

    public String getStartTime() {
        return getString(START_TIME_KEY);
    }

    public String getTitle() {
        return getString(TITLE_KEY);
    }
    public void setTitle(String  title) {
        put(TITLE_KEY,title);
    }

    public ArrayList<String> getTopics() {
        ArrayList<String> result = new ArrayList<>();
        JSONArray topics = getJSONArray(TOPICS_KEY);

        for (int i = 0; i < topics.length(); i++) {
            try {
                result.add((String) topics.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return result;
    }

    public String getLocation() {
        return getString(LOCATION_KEY);
    }

    public void setCourseId(String courseId) {
        put(COURSE_ID_KEY, courseId);
    }

    public void setStartDate(String startDate) {
        if(startDate == null) { startDate="00-00"; }
        put(START_DATE_KEY, startDate);
    }

    public void setStartTime(String startTime) {
        if(startTime == null) {startTime = "00:00";}
        put(START_TIME_KEY, startTime);
    }

    public void setLocation(String location) {
        put(LOCATION_KEY, location);
    }

    public void setTopics(ArrayList<String> topics) {
        put(TOPICS_KEY, topics);
    }


}