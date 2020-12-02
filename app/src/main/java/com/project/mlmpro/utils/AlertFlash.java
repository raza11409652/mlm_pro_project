/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.utils;

import android.app.Activity;
import android.content.Context;

import com.andrognito.flashbar.Flashbar;
import com.project.mlmpro.R;

public class AlertFlash {
    Context context;
    Activity activity;

    public AlertFlash(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public Flashbar alertError(String msg) {
        return new Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.TOP)
                .title("Alert")
                .message(msg)
                .duration(2000)
                .backgroundDrawable(R.drawable.alert_gardient).build();
    }
}
