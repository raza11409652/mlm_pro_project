/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.project.mlmpro.R;

public class IntentSetting {
    Context context;

    public IntentSetting(Context context) {
        this.context = context;
    }

    public void call(String mobile) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
        context.startActivity(intent);
    }

    public void openWhatsapp() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        String url = "https://api.whatsapp.com/send?phone=" + context.getString(R.string.whatsapp_to_number);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    public void openShare(String msg) {
//        Log.d(TAG, "shareIntent: " + msg);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "MLM Pro");
        context.startActivity(Intent.createChooser(sharingIntent, "Share using"));

    }


}
