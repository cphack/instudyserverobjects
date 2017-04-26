package com.codepath.android.instudy.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import static com.codepath.android.instudy.models.Assignment_User.ASSIGNMENT_ID_KEY;
import static com.codepath.android.instudy.models.Assignment_User.ISSUBMIT_KEY;

@ParseClassName("Notification")
public class Notification extends ParseObject {
    public static final String TITLE_KEY = "title";
    public static final String LINK_KEY = "link";
    public static final String DESCRIP_KEY = "descrip";
    public static final String TYPE_KEY= "type";
    public static final String IMAGE_URL_KEY= "image";

    public String getTitle() {
        return getString(TITLE_KEY);
    }
    public void setTitle(String val) {
       put(TITLE_KEY,val);
    }

    public String getLink() {
        return getString(LINK_KEY);
    }
    public void setLink(String val) {
        put(LINK_KEY,val);
    }


    public String getDescrip() {
        return getString(DESCRIP_KEY);
    }
    public void setDescrip(String val) {
        put(DESCRIP_KEY,val);
    }


    public String getType() {
        return getString(TYPE_KEY);
    }
    public void setType(String val) {
        put(TYPE_KEY,val);
    }


    public String getImageUrlKey() {
        return getString(IMAGE_URL_KEY);
    }
    public void setImage(String val) {
        put(IMAGE_URL_KEY,val);
    }
}
