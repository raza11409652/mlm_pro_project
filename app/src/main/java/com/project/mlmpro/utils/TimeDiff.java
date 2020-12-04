/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeDiff {
    final static String TAG = TimeDiff.class.getSimpleName();

    public static String diff(String date) {
        //
        try {
            Date today = new Date();
            Log.e(TAG, "diff: Today " + today.toString());
            String FinalDate = convertMongoDate(date);
            long difference = today.getTime() - new Date(FinalDate).getTime();
            long seconds = difference / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            if (hours > 24) {
                return days + "day ago";
            } else {
                return hours + "hour ago";
            }

//            return String.valueOf(days);

        } catch (Exception exception) {
            Log.e("Exception" + TAG, "exception " + exception);
        }
        return null;
    }

    private static String convertMongoDate(String val) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            String finalStr = outputFormat.format(inputFormat.parse(val));
            System.out.println(finalStr);
            return finalStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
