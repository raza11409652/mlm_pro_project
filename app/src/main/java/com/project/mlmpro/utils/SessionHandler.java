/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionHandler {
    private static String TAG = SessionHandler.class.getSimpleName();
    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Shared preferences file name
    private static final String PREF_NAME = "MLM_PRO_ANDROID";

    private static final String IS_LOGGED_IN = "logged_in_status";
    private static final String LOGGED_IN_MOBILE = "logged_in_mobile";
    private static final String LOGGED_IN_USER = "logged_in_user"; //it will store uid
    private static final String DEVICE_TOKEN = "device_Token"; //it will store uid
    private static final String LOGGED_TOKEN = "login_Token"; //it will store uid
    private static final String USER_NAME = "user_name"; //it will store uid
    private static final String USER_EMAIL = "user_email"; //it will store uid
    private static final String PROFILE_IMAGE = "user_image"; //it will store uid
    private static final String COMPANY_NAME = "user_company"; //it will store uid
    private static final String USER_ADDRESS= "user_address"; //it will store uid

    public SessionHandler(Context context) {
        this._context = context;

        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref
                .edit();
    }

    public void setLogin(boolean status) {
        editor.putBoolean(IS_LOGGED_IN, status);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setLoggedToken(String _token) {
        editor.putString(LOGGED_TOKEN, _token);
        editor.commit();
        Log.d(TAG, "setLoggedToken: Token modified");
    }

    public void setLoggedInMobile(String _mobile) {
        editor.putString(LOGGED_IN_MOBILE, _mobile);
        editor.commit();
        Log.d(TAG, "setLoggedInMobile: Mobile number updated");
    }

    public void setUserName(String name) {
        editor.putString(USER_NAME, name);
        editor.commit();
        Log.d(TAG, "setUserName: name modifed");
    }

    public void setLoggedInUser(String user) {
        editor.putString(LOGGED_IN_USER, user);
        editor.commit();
        Log.d(TAG, "setLoggedInUser: logged is user changed");
    }

    public void setLoggedInEmail(String email) {
        editor.putString(USER_EMAIL, email);
        editor.commit();
        Log.d(TAG, "setLoggedInEmail: Email updated");
    }
    public  void setCompanyName(String com){
        editor.putString(COMPANY_NAME , com);
        editor.commit();
        Log.d(TAG, "setCompanyName: " +com);
    }
    public  String getCompanyName(){
        return  pref.getString(COMPANY_NAME , null) ;
    }

    public String getLoggedInUser() {
        return pref.getString(LOGGED_IN_USER, null);
    }


    public String getLoggedToken() {
        return pref.getString(LOGGED_TOKEN, null);
    }

    public String getLoggedInMobile() {
        return pref
                .getString(LOGGED_IN_MOBILE, null);
    }

    public String getUserName() {
        return pref.getString(USER_NAME, null);
    }

    public boolean getIsLoggedIn() {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public String getUserEmail() {
        return pref.getString(USER_EMAIL, null);
    }

    public void setProfileImage(String image) {
        editor.putString(PROFILE_IMAGE, image);
        editor.commit();
        Log.d(TAG, "setProfileImage: " + image);
    }

    public String getProfileImage() {
        return pref.getString(PROFILE_IMAGE, null);
    }
}
