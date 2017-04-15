package com.codepath.android.instudy.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;



/*
Title,
Description
teachers[]
students[]
maxnumstudent
startdate[]
enddate[]
lections[]
assignments[]
surveys[]  ????
nextlectionid
 */

@ParseClassName("Course")
public class Course extends ParseObject {
    public static final String TITLE_KEY = "title";
    public static final String DESCRIPTION_KEY = "description";
    public static final String TEACHERS_KEY = "teachers";
    public static final String STUDENTS_KEY = "students";
    public static final String MAX_STUDENTS_KEY = "maxStudents";
    public static final String START_DATE_KEY = "startDate";
    public static final String END_DATE_KEY = "endDate";
    public static final String LECTIONS_KEY = "lections";
    public static final String ASSIGNMENTS_KEY = "assignemtns";
    public static final String LECTION_KEY = "lection";
    public static final String ASSIGNMENT_KEY = "assignement";

    public String getTitle() {
        return getString(TITLE_KEY);
    }
    public String getDescription() {
        return getString(DESCRIPTION_KEY);
    }
    public String getTeachers() { return getString(TEACHERS_KEY);}
    public ArrayList<String> getStudents() {
        ArrayList<String> result = new ArrayList<>();
        JSONArray sts = getJSONArray(STUDENTS_KEY);
        if(sts!=null) {
            for (int i = 0; i < sts.length(); i++) {
                try {
                    result.add((String) sts.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
        return result;
    }
    public Integer getMaxStudentNum() {
        return getInt(MAX_STUDENTS_KEY);
    }
    public Date getStartDate() {
        return getDate(START_DATE_KEY);
    }
    public Date getEndDate() {
        return getDate(END_DATE_KEY);
    }
    public ArrayList<String> getLections() {
        ArrayList<String> result = new ArrayList<>();
        JSONArray lcs = getJSONArray(LECTIONS_KEY);

        for (int i = 0; i < lcs.length(); i++) {
            try {
                result.add((String) lcs.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return result;
    }
    public ArrayList<String> getAssignements() {
        ArrayList<String> result = new ArrayList<>();
        JSONArray asms = getJSONArray(ASSIGNMENTS_KEY);

        for (int i = 0; i < asms.length(); i++) {
            try {
                result.add((String) asms.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return result;
    }
    public String getLection() {
        return getString(LECTION_KEY);
    }
    public String getAssignment() {
        return getString(ASSIGNMENT_KEY);
    }
    public String getSubTitle(){return "14 April 2017 Campus C, aud.244";}


    public void setTitle(String title) {
        put(TITLE_KEY, title);
    }
    public void setDescription(String description) {
        put(DESCRIPTION_KEY, description);
    }
    public void setTeachers(String teacher) {
        put(TEACHERS_KEY, teacher);
    }
    public void setStudents(ArrayList<String> students) {
        put(STUDENTS_KEY, students);
    }
    public void setMaxStudetnNum(Integer maxStudentNum ) {
        put(MAX_STUDENTS_KEY, maxStudentNum);
    }
    public void setStartDate(Date startDate) {
        put(START_DATE_KEY, startDate);
    }
    public void setEndDate(Date endDate) {
        put(END_DATE_KEY, endDate);
    }
    public void setLections(ArrayList<String> lections) {
        put(LECTIONS_KEY, lections);
    }
    public void setAssignemtns(ArrayList<String> assignments) {
        put(ASSIGNMENTS_KEY, assignments);
    }
    public void setLection(String lection) {
        put(LECTION_KEY, lection);
    }
    public void setAssignment(String assignment) {
        put(ASSIGNMENT_KEY, assignment);
    }



}
