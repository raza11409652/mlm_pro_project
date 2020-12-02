/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.project.mlmpro.R;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.Server;
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


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


                //All ok
                Toast.makeText(getApplicationContext(), "All okay", Toast.LENGTH_SHORT).show();
                JSONObject object = new JSONObject();
                try {
                    object.put("fullName", name);
                    object.put("email", email);
                    object.put("phone", mobile);
                    object.put("password", password);

                    registerInit(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }

    private void registerInit(JSONObject object) {
        api.postRequest(object, response -> Log.d(TAG, "onResponse: " + response), Server.REGISTER);


    }
}