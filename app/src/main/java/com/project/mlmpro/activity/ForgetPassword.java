/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.project.mlmpro.R;
import com.project.mlmpro.component.Loader;
import com.project.mlmpro.utils.Constant;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.Server;
import com.project.mlmpro.utils.StringHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPassword extends AppCompatActivity {
    Toolbar toolbar;
    String mobile;
    EditText mobileNumber;
    Button forgetBtn;
    RequestApi api;

    String TAG = ForgetPassword.class.getSimpleName();
    Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        setTitle("");
        mobileNumber = findViewById(R.id.mobile_input);
        forgetBtn = findViewById(R.id.forget_pwd);
        api = new RequestApi(this);
        loader = new Loader(this);


        forgetBtn.setOnClickListener(v -> {
            mobile = mobileNumber.getText().toString();
            if (StringHandler.isValidMobile(mobile) == false) {
                mobileNumber.setError("Invalid mobile number");
                return;
            }

            JSONObject object = new JSONObject();
            try {

                object.put("phone", mobile);
            } catch (Exception e) {
                e.printStackTrace();
            }

            loader.show("Please wait..");
            api.postRequest(object, response -> {
                loader.dismiss();
                try {
                    int status = response.getInt("status");
                    if (status == 200) {
//                        String otp =
                        JSONObject data = response.getJSONObject("data");
                        String otp = data.getString("otp");
                        Constant.OTP_FORGET = otp;
                        Constant.FORGET_MOBILE = mobile;
                        Intent i= new Intent(getApplicationContext() , NewPassword.class);
                        startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onCreate: " + response);
            }, Server.FORGET_PASSWORD);

//            loader.dismiss();
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}