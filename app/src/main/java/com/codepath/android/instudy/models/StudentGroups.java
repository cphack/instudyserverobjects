package com.codepath.android.instudy.models;

/**
 * Created by hkanekal on 4/8/2017.
 */

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("StudentGroups")
public class StudentGroups extends ParseObject {
    public static String ST_GROUP_NAME = "stGroupName";
    public static String ST_GROUP_NUMBER = "stGroupNumber";
    public static String ST_GROUP_COUNT = "stGroupCount";
    public static String ST_GROUP_DATE = "stGroupDate";

    public static String getStGroupName() {
        return ST_GROUP_NAME;
    }

    public static void setStGroupName(String stGroupName) {
        ST_GROUP_NAME = stGroupName;
    }

    public static String getStGroupNumber() {
        return ST_GROUP_NUMBER;
    }

    public static void setStGroupNumber(String stGroupNumber) {
        ST_GROUP_NUMBER = stGroupNumber;
    }

    public static String getStGroupCount() {
        return ST_GROUP_COUNT;
    }

    public static void setStGroupCount(String stGroupCount) {
        ST_GROUP_COUNT = stGroupCount;
    }

    public static String getStGroupDate() {
        return ST_GROUP_DATE;
    }

    public static void setStGroupDate(String stGroupDate) {
        ST_GROUP_DATE = stGroupDate;
    }
}
