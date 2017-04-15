package com.codepath.android.instudy.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

import static com.codepath.android.instudy.models.Message.BODY_KEY;
import static com.codepath.android.instudy.models.Message.DATE_KEY;


@ParseClassName("Chat")
public class Chat extends ParseObject {

    public static final String RECIPIENTS_KEY = "recipients";
    public static final String LAST_MESSAGE_DATE_KEY = "date";
    public static final String MESSAGES_KEY = "messages";


    public ArrayList<String> getMessages() {
        ArrayList<String> result = new ArrayList<>();
        JSONArray messages = getJSONArray(MESSAGES_KEY);
        if (messages != null) {
            for (int i = 0; i < messages.length(); i++) {
                try {
                    result.add((String) messages.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
        return result;
    }


    public ArrayList<String> getRecipients() {
        ArrayList<String> result = new ArrayList<>();
        JSONArray recipients = getJSONArray(RECIPIENTS_KEY);
        if (recipients != null) {
            for (int i = 0; i < recipients.length(); i++) {
                try {
                    result.add((String) recipients.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
        return result;
    }

    public void setRecipients(ArrayList<String> recipients) {
        put(RECIPIENTS_KEY, recipients);
    }

    public void setMessages(ArrayList<String> messages) {
        put(MESSAGES_KEY, messages);
    }

    public Date getLastDate() {
        return getDate(LAST_MESSAGE_DATE_KEY);
    }

    public void setLastDate(Date date) {
        put(LAST_MESSAGE_DATE_KEY, date);
    }

}
