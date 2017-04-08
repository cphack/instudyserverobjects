package com.codepath.android.instudy.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


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

    public String getCourseId() {
        return getString(COURSE_ID_KEY);
    }

    public Date getStartDate() {
        return getDate(START_DATE_KEY);
    }

    public String getTitle() {
        return getString(TITLE_KEY);
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

    public void setStartDate(Date startDate) {
        put(START_DATE_KEY, startDate);
    }

    public void setLocation(String location) {
        put(LOCATION_KEY, location);
    }

    public void setTopics(ArrayList<String> topics) {
        put(TOPICS_KEY, topics);
    }
}