/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.model;

import com.sendbird.uikit.interfaces.UserInfo;

class UserChatProfile implements UserInfo {
    private String YOUR_USER_ID;
    private String YOUR_USER_NICKNAME;
    private String YOUR_USER_PROFILE_URL;

    public UserChatProfile(String YOUR_USER_ID, String YOUR_USER_NICKNAME, String YOUR_USER_PROFILE_URL) {
        this.YOUR_USER_ID = YOUR_USER_ID;
        this.YOUR_USER_NICKNAME = YOUR_USER_NICKNAME;
        this.YOUR_USER_PROFILE_URL = YOUR_USER_PROFILE_URL;
    }

    @Override
    public String getUserId() {
        return YOUR_USER_ID;
    }

    @Override
    public String getNickname() {
        return YOUR_USER_NICKNAME;
    }

    @Override
    public String getProfileUrl() {
        return YOUR_USER_PROFILE_URL;
    }
}