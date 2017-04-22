package com.codepath.android.instudy.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

@ParseClassName("Chat")
public class Chat extends ParseObject {

    public static final String RECIPIENTS_KEY = "recipients";
    public static final String LAST_MESSAGE_DATE_KEY = "updateAt";
    public static final String LAST_MESSAGE_ID = "lastMessageId";
    public static final String CHAT_NAME_KEY = "chatName";


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



    public void setLastDate(Date date) {
        put(LAST_MESSAGE_DATE_KEY, date);
    }

    public String getLastMessageId() {
        return getString(LAST_MESSAGE_ID);
    }

    public void setLastMessageId(String msgId) {
        put(LAST_MESSAGE_ID, msgId);
    }

    public String getChatName() {
        return getString(CHAT_NAME_KEY);
    }

    public void setChatName(String name) {
        put(CHAT_NAME_KEY, name);
    }

}

