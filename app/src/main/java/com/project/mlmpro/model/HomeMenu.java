/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.model;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public class HomeMenu {
    String name;
    Intent intent;
    int drawable;

    public HomeMenu(String name, Intent intent, int drawable) {
        this.name = name;
        this.intent = intent;
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
