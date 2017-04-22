package com.codepath.android.instudy.helpers;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alex_ on 4/21/2017.
 */

public class DateTimeHelper {


    public static String getRelativeTimeAgo(String rawJsonDate) {

        String relativeDate = "";
        if (rawJsonDate == null) return relativeDate;
        String format = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(format, Locale.ENGLISH);
        sf.setLenient(true);


        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
            relativeDate = relativeDate.replace(" minutes ago", "m")
                    .replace(" minute ago", "m")
                    .replace(" hours ago", "h")
                    .replace(" hour ago", "h")
                    .replace(" days ago", "d")
                    .replace(" day ago", "d");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }


    public static String getRelativeTimeAgo(Date date) {

        if(date==null) return "now";
        String relativeDate = "";

       /* String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);*/


        long dateMillis = date.getTime();
        relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
        relativeDate = relativeDate.replace(" minutes ago", "m")
                .replace(" minute ago", "m")
                .replace(" hours ago", "h")
                .replace(" hour ago", "h")
                .replace(" days ago", "d")
                .replace(" day ago", "d");

        if(relativeDate.equals("0m")){
            relativeDate ="now";
        }


        return relativeDate;
    }
}
