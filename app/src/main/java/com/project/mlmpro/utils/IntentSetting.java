/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class IntentSetting {
    Context context ;

    public IntentSetting(Context context) {
        this.context = context;
    }
    public  void call(String mobile){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +mobile));
        context.startActivity(intent);
    }



}
