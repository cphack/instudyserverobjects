package com.codepath.android.instudy.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;




@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String USER_ID_KEY = "userId";
    public static final String BODY_KEY = "body";
    public static final String RECIPIENTS_KEY = "recipients";
    public static final String DATE_KEY = "date";

    public String getUserId() {
        return getString(USER_ID_KEY);
    }

    public String getBody() {
        return getString(BODY_KEY);
    }

    public void setUserId(String userId) {
        put(USER_ID_KEY, userId);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }

    public ArrayList<String> getRecipients() {
        ArrayList<String> result = new ArrayList<>();
        JSONArray recipients = getJSONArray(RECIPIENTS_KEY);

        for (int i = 0; i < recipients.length(); i++) {
            try {
                result.add((String) recipients.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return result;
    }

    public void setRecipients(ArrayList<String> recipients) {
        put(RECIPIENTS_KEY, recipients);
    }

    public Date getDate() {
        return getDate(DATE_KEY);
    }

    public void setDate(Date date) {
        put(DATE_KEY, date);
    }

}
