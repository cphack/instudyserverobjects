package com.codepath.android.instudy.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;


@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String USER_ID_KEY = "userId";
    public static final String BODY_KEY = "body";
    public static final String CHAT_KEY = "chatid";

    private MapData mapData;



    public ParseFile getFileName() {

       return this.getParseFile("fileName");
    }




    public MapData getMapData() {
        return mapData;
    }

    public void setMapData(MapData mapData) {
        this.mapData = mapData;
    }


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

    public void setChatId(String chatid) {
        put(CHAT_KEY, chatid);
    }

    public String getChatId() {
        return getString(CHAT_KEY);
    }

}
