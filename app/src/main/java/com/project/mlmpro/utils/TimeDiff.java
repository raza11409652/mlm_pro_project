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
            String FinalDate = convertMongoDateWithS(date);
            Date postDate = new Date(FinalDate) ;
            Log.d(TAG, "diff: "  +postDate);
            long difference = today.getTime() - new Date(FinalDate).getTime();
            Log.d(TAG, "diff: "  +difference);
            long seconds = difference / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
//            Log.d(TAG, "diff: " + minutes);
//            if (hours > 24) {
//                return days + " day ago";
//            } else if(hours>1) {
//                return hours + " hour ago";
//            }
            if (minutes < 60) {
                return minutes + "Minutes ago";
            } else if (hours > 24) {
                return days
                        + "days ago ";
            } else {
                return hours + " hour ago";
            }

//            return String.valueOf(days);

        } catch (Exception exception) {
            Log.e("Exception" + TAG, "exception " + exception);
        }
        return null;
    }

    public static String diffO(String date) {
        //
        try {
            Date today = new Date();
            Log.e(TAG, "diff: Today " + today.toString());
            String FinalDate = convertMongoDate(date);
            long difference = new Date(FinalDate).getTime() - today.getTime();
            long seconds = difference / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            if (hours > 24) {
                return String.valueOf(days);
            } else {
                return String.valueOf(hours);
            }

//            return String.valueOf(days);

        } catch (Exception exception) {
            Log.e("Exception" + TAG, "exception " + exception);
        }
        return null;
    }

    public static String convertMongoDate(String val) {
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
    public  static  String dateNow (String time){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(Long.parseLong(time)));
        return  dateString ;
    }

    public static String convertMongoDateWithS(String val) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        try {
            String finalStr = outputFormat.format(inputFormat.parse(val));
//            System.out.println(finalStr);
            return finalStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
