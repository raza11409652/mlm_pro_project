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
    static  String [] months = {"Jan" ,"Feb" , "March" , "April" ,  "May" , "June" , "July" , "Aug" , "Sep" , "Oct" , "Nov" ,"Dec" };

    public static String diff(String date) {
        //
        try {
            Date today = new Date();
//            Log.e(TAG, "diff: Today " + today.toString());
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            String tod = outputFormat.format(today);
            String FinalDate = convertMongoDateWithS(date);
//            Log.e(TAG, "diff: "  +FinalDate );
            Date postDate = new Date(FinalDate) ;
            Date Now = new Date(tod);
//            Log.d(TAG, "diff: "  +postDate);
//            Log.d(TAG, "diff: "  +Now);

            long difference = postDate.getTime()  - Now.getTime();
            difference = Math.abs(difference);
            long seconds = difference / 1000;
            long minutes = seconds/60  ;
            long hour = minutes /60 ;
            if (hour<24){
                if (hour<1){
                    return  minutes +" minutes ago";
                }else if(minutes<1){
                    return  seconds +" seconds ago";
                }
                return  hour+" hour ago";
            }else{
                return  postDate.getDate() +" " + months[postDate.getMonth()];
            }

        } catch (Exception exception) {
            Log.e("Exception" + TAG, "exception " + exception);
        }
        return null;
    }

    public static String diffO(String date) {
        //
        try {
            Date today = new Date();
//            Log.e(TAG, "diff: Today " + today.toString());
            String FinalDate = convertMongoDate(date);
            long difference = new Date(FinalDate).getTime() - today.getTime();
            long seconds = difference / 1000;
            Log.d(TAG, "diffO: "  +seconds);
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
