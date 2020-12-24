/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.andrognito.flashbar.Flashbar;
import com.project.mlmpro.R;
import com.project.mlmpro.VerifyOtp;
import com.project.mlmpro.component.Loader;
import com.project.mlmpro.utils.AlertFlash;
import com.project.mlmpro.utils.Constant;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.Server;
import com.project.mlmpro.utils.SessionHandler;
import com.project.mlmpro.utils.StringHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    Toolbar toolbar;
    EditText nameEdt, emailEdt, mobileEdt, passwordEdt, confPasswordEdt;
    String TAG = Register.class.getSimpleName();
    String name, email, mobile, password, confPassword;
    Button register;
    RequestApi api;
    Loader loader;
    SessionHandler sessionHandler;
    private Flashbar flashbar = null;

    AlertFlash alertFlash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        /**
         * Init views
         */
        toolbar = findViewById(R.id.toolbar);
        nameEdt = findViewById(R.id.name);
        emailEdt = findViewById(R.id.email);
        mobileEdt = findViewById(R.id.mobile);
        register = findViewById(R.id.register_btn);
        passwordEdt = findViewById(R.id.password_input);
        confPasswordEdt = findViewById(R.id.password_input_confirm);


        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);

        setTitle("");
        api = new RequestApi(this);
        loader = new Loader(this);
        sessionHandler = new SessionHandler(this);
        alertFlash = new AlertFlash(this, this);
//        if (flashbar==null){
//            flashbar = alertFlash.alertError()
//        }


        nameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + s);
                boolean valid = StringHandler.isValidName(s.toString());
                if (!valid) {
                    nameEdt.setError(getString(R.string.invalid_name));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean valid = StringHandler.isEmailValid(s.toString());
                if (!valid) {
                    emailEdt.setError(getString(R.string.invalid_email));
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mobileEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                boolean valid = StringHandler.isValidMobile(s.toString());
                if (!valid) {
                    mobileEdt.setError(getString(R.string.invalid_mobile));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        register.setOnClickListener(v -> {
            name = nameEdt.getText().toString();
            email = emailEdt.getText().toString();
            mobile = mobileEdt.getText().toString();

            password = passwordEdt.getText().toString();
            confPassword = confPasswordEdt.getText().toString();

            boolean valid = StringHandler.isPasswordValid(password, confPassword);

            if (StringHandler.isValidName(name) == false) {
                nameEdt.setError(getString(R.string.invalid_name));
                return;
            }
            if (StringHandler.isEmailValid(email) == false) {
                emailEdt.setError(getString(R.string.invalid_email));
                return;
            }
            if (StringHandler.isValidMobile(mobile) == false) {
                mobileEdt.setError(getString(R.string.invalid_mobile));
                return;
            }
            if (!valid) {
                confPasswordEdt.setError(getString(R.string.invalid_psw));
                return;
            }
            loader.show(getString(R.string.loading));


            //All ok

            JSONObject object = new JSONObject();
            try {
                object.put("fullName", name);
                object.put("email", email);
                object.put("phone", mobile);
                object.put("password", password);
                object.put("deviceToken" , sessionHandler.getUserDeviceToken());


                registerInit(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        });


    }

    private void registerInit(JSONObject object) {
        api.postRequest(object, response -> {
            Log.d(TAG, "registerInit: " + response);
            loader.dismiss();
            try {
                int status = response.getInt("status");
                String message = response.getString("message");
                if (status == 200) {
                    //OK
                    JSONObject data = response.getJSONObject("data");
                    String _id = data.getString("id");
                    String _name = data.getString("fullName");
                    String _email = data.getString("email");
                    String _phone = data.getString("phone");
                    String _token = data.getString("accessToken");
                    sessionHandler.setLogin(true);
                    sessionHandler.setLoggedInMobile(_phone);
                    sessionHandler.setLoggedInUser(_id);
                    sessionHandler.setLoggedToken(_token);
                    sessionHandler.setUserName(_name);
                    sessionHandler.setLoggedInEmail(_email);
                    Constant.PROCESS = "SIGNUP";
                    Intent verification = new Intent(getApplicationContext(), VerifyOtp.class);
                    updateScreen(verification);

                } else {
                    //Error
                    if (flashbar == null) {
                        flashbar = alertFlash.alertError("" + message);

                    } else {
                        flashbar = null;
                        flashbar = alertFlash.alertError("" + message);
                    }
                    flashbar.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, Server.REGISTER);


    }

    private void updateScreen(Intent intent) {
        startActivity(intent);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
}