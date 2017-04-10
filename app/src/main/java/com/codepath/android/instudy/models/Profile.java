package com.codepath.android.instudy.models;

import android.net.Uri;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by hkanekal on 4/8/2017.
 */
@ParseClassName("Profile")
public class Profile  extends ParseObject {
    public static String USER_ID_KEY = "userId";
    public static String USER_NAME_KEY = "userName";
    public static String USER_TAGLINE_KEY = "userTagLine";
    public static String USER_PROFILE_IMAGE_KEY = "userProfileImage";
    public static String USER_LOCATION_KEY = "userLocation";
    public static String USER_NOTES_KEY = "userNotes";
    public static String USER_STUDENT_COUNT_KEY = "studentGroupCount";
    public static String USER_TEACHER_COUNT_KEY = "teacherGroupCount";
    public static String USER_PROFILE_PICTURE_KEY = "myProfilePic"; // "myProfilePic.png";

    public String getUserIdKey() {
        return getString(USER_ID_KEY);
    }
    public void setUSER_ID_KEY(String userId) {
        put(USER_ID_KEY, userId);
    }

    public String getUserNameKey() {
        return getString(USER_NAME_KEY);
    }

    public void setUserNameKey(String userNameKey) {
        put(USER_NAME_KEY,userNameKey);
    }

    public String getUserTaglineKey() {
        return getString(USER_TAGLINE_KEY);
    }

    public void setUserTaglineKey(String userTaglineKey) {
        put(USER_TAGLINE_KEY ,userTaglineKey);
    }

    public String getUserProfileImageKey() {
        return getString(USER_PROFILE_IMAGE_KEY);
    }

    public void setUserProfileImageKey(Uri userProfileImageKey) {
        put(USER_PROFILE_IMAGE_KEY,String.valueOf(userProfileImageKey));
    }

    public String getUserLocationKey() {
        return getString(USER_LOCATION_KEY);
    }

    public void setUserLocationKey(String userLocationKey) {
        put(USER_LOCATION_KEY,userLocationKey);
    }

    public String getUserNotesKey() {
        return getString(USER_NOTES_KEY);
    }

    public void setUserNotesKey(String userNotesKey) {
        put(USER_NOTES_KEY,userNotesKey);
    }

    public String getUserStudentCountKey() {
        return getString(USER_STUDENT_COUNT_KEY);
    }

    public void setUserStudentCountKey(String userStudentCountKey) {
        put(USER_STUDENT_COUNT_KEY,userStudentCountKey);
    }

    public String getUserTeacherCountKey() {
        return USER_TEACHER_COUNT_KEY;
    }

    public void setUserTeacherCountKey(String userTeacherCountKey) {
        put(USER_TEACHER_COUNT_KEY,userTeacherCountKey);
    }

    public String getUserProfilePictureKey() {
        return USER_PROFILE_PICTURE_KEY;
    }

    public void setUserProfilePictureKey(String userProfilePictureKey) {
        put(USER_PROFILE_PICTURE_KEY,userProfilePictureKey);
    }
}
