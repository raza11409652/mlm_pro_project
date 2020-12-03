/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.model;

import android.content.Intent;

public class MenuModel {
    public String menuName;
    public Intent intent;
    public boolean hasChildren, isGroup;

    public MenuModel(String menuName, Intent intent, boolean hasChildren, boolean isGroup) {
        this.menuName = menuName;
        this.intent = intent;
        this.hasChildren = hasChildren;
        this.isGroup = isGroup;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }
}
