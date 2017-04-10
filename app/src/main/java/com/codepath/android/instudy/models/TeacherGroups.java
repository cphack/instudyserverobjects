package com.codepath.android.instudy.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by hkanekal on 4/8/2017.
 */
@ParseClassName("TeacherGroups")
public class TeacherGroups extends ParseObject {
    public static String TC_GROUP_NAME = "tcGroupName";
    public static String TC_GROUP_NUMBER = "tcGroupNumber";
    public static String TC_GROUP_COUNT = "tcGroupCount";
    public static String TC_GROUP_DATE = "tcGroupDate";

    public static String getTcGroupName() {
        return TC_GROUP_NAME;
    }

    public static void setTcGroupName(String tcGroupName) {
        TC_GROUP_NAME = tcGroupName;
    }

    public static String getTcGroupNumber() {
        return TC_GROUP_NUMBER;
    }

    public static void setTcGroupNumber(String tcGroupNumber) {
        TC_GROUP_NUMBER = tcGroupNumber;
    }

    public static String getTcGroupCount() {
        return TC_GROUP_COUNT;
    }

    public static void setTcGroupCount(String tcGroupCount) {
        TC_GROUP_COUNT = tcGroupCount;
    }

    public static String getTcGroupDate() {
        return TC_GROUP_DATE;
    }

    public static void setTcGroupDate(String tcGroupDate) {
        TC_GROUP_DATE = tcGroupDate;
    }
}
