/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.andrognito.flashbar.Flashbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.project.mlmpro.MainActivity;
import com.project.mlmpro.R;

public class AlertFlash {
    Context context;
    Activity activity;
    SessionHandler sessionHandler;

    public AlertFlash(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        sessionHandler = new SessionHandler(context);
    }

    public Flashbar alertError(String msg) {
        return new Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.TOP)
                .title("Alert")
                .message(msg)
                .duration(2000)
                .backgroundDrawable(R.drawable.alert_gardient).build();
    }

    public void logoutAlert() {
        new MaterialAlertDialogBuilder(context).setMessage(context.getString(R.string.logout_message))
                .setPositiveButton("OK", (dialog, which) -> {
                    sessionHandler.setLogin(false);
                    sessionHandler.setLoggedInEmail(null);
//                    sessionHandler.setLoggedToken(null);
                    Intent main = new Intent(context, MainActivity.class);
                    context.startActivity(main);
                    main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.finish();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                })
                .show();
    }

    public void alertHome(String msg) {
        new MaterialAlertDialogBuilder(context).setMessage(msg)
                .setPositiveButton("OK", (dialog, which) -> activity.finish())
                .show();
    }
}
