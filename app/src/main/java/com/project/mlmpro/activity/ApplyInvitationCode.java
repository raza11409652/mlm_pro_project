/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.andrognito.flashbar.Flashbar;
import com.project.mlmpro.R;
import com.project.mlmpro.component.Loader;
import com.project.mlmpro.utils.AlertFlash;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.Server;
import com.project.mlmpro.utils.SessionHandler;
import com.project.mlmpro.utils.StringHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class ApplyInvitationCode extends AppCompatActivity {
    Toolbar toolbar;
    SessionHandler sessionHandler;
    AlertFlash alertFlash;
    Flashbar flashbar = null;
    EditText inviteEdt;
    String inviteCode = null;
    Button confirm, skip;
    RequestApi requestApi;
    Loader loader;

    String TAG = ApplyInvitationCode.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_invitation_code);

        toolbar = findViewById(R.id.toolbar);
        skip = findViewById(R.id.skip_button);
        confirm = findViewById(R.id.confirm_button);
        inviteEdt = findViewById(R.id.invite_code);


        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);

        setTitle("");

        sessionHandler = new SessionHandler(this);
        alertFlash = new AlertFlash(this, this);
        requestApi = new RequestApi(this);
        loader = new Loader(this);

//        alertFlash.


        skip.setOnClickListener(v -> {
            //Skip to Profile complete
            Intent intent = new Intent(getApplicationContext(), CompleteProfile.class);
            updateScreen(intent);
        });
        confirm.setOnClickListener(v -> {
            inviteCode = inviteEdt.getText().toString();
            if (StringHandler.isEmpty(inviteCode)) {
                if (flashbar != null) {
                    flashbar = null;
                }
                flashbar = alertFlash.alertError(getString(R.string.required_error_invite));
                flashbar.show();
                return;
            }
            loader.show(getString(R.string.loading));
            JSONObject object = new JSONObject();
            try {
                object.put("inviteCode", inviteCode);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> saveInvite(object));


        });

    }

    private void saveInvite(JSONObject object) {

        requestApi.postRequest(object, response -> {
            loader.dismiss();
            Log.d(TAG, "onResponse: " + response);

            try {
                int status = response.getInt("status");
                String message = response.getString("message");
                if (status == 200) {
                    //UPDATE Screen forward to Profile complete
                    Intent intent = new Intent(getApplicationContext(), CompleteProfile.class);
                    updateScreen(intent);

                } else {
                    if (flashbar != null) {
                        flashbar = null;
                    }
                    flashbar = alertFlash.alertError(message + "");
                    flashbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, Server.APPLY_INVITE);
    }

    private void updateScreen(Intent intent) {
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }


}