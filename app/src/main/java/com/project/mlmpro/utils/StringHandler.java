/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class StringHandler {
    public static final boolean isEmpty(String s) {
        if (TextUtils.isEmpty(s)) {
            return true;
        }
        return false;
    }

    /**
     * validate email
     *
     * @param email
     * @return boolean
     */
    public static final boolean isEmailValid(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;

        }
        return false;
    }

    /**
     * validate name
     * 1. length should be greater than 3
     *
     * @param name
     * @return boolean
     */
    public static final boolean isValidName(String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        if (name.length() < 3) {
            return false;
        }

        return true;
    }

    /**
     * Validate mobile
     *
     * @param mobile
     * @return
     */
    public static final boolean isValidMobile(String mobile) {
        String MobilePattern = "[0-9]{10}";
        if (TextUtils.isEmpty(mobile)) return false;

        if (mobile.matches(MobilePattern)) return true;

        return false;
    }

    public static final boolean isPasswordValid(String psw, String confPsw) {
        if (TextUtils.isEmpty(psw) || TextUtils.isEmpty(confPsw)) {
            return false;
        }
        if (psw.equals(confPsw)) return true;

        return false;
    }

    public static String captalize(String st) {

        String firstLetter = st.substring(0, 1);
        String remainingLetters = st.substring(1, st.length());

        // change the first letter to uppercase
        firstLetter = firstLetter.toUpperCase();

        // join the two substrings
        st = firstLetter + remainingLetters;
        return st;
    }

}



