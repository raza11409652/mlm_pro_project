/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity;

import android.app.AppComponentFactory;
import android.app.Application;

import com.project.mlmpro.R;
import com.sendbird.uikit.SendBirdUIKit;
import com.sendbird.uikit.adapter.SendBirdUIKitAdapter;
import com.sendbird.uikit.interfaces.UserInfo;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SendBirdUIKit.init(new SendBirdUIKitAdapter() {
            @Override
            public String getAppId() {
                return getString(R.string.sendbird_id);
            }

            @Override
            public String getAccessToken() {
                return null;
            }

            @Override
            public UserInfo getUserInfo() {
                return new UserInfo() {
                    @Override
                    public String getUserId() {
                        return null;
                    }

                    @Override
                    public String getNickname() {
                        return null;
                    }

                    @Override
                    public String getProfileUrl() {
                        return null;
                    }
                };
            }
        } , this);
    }
}
