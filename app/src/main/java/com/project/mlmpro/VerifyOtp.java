/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.project.mlmpro.activity.ApplyInvitationCode;
import com.project.mlmpro.activity.Home;
import com.project.mlmpro.activity.Login;
import com.project.mlmpro.component.OtpEditText;
import com.project.mlmpro.utils.Constant;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.Server;
import com.project.mlmpro.utils.SessionHandler;
import com.project.mlmpro.utils.StringHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class VerifyOtp extends AppCompatActivity {
    SessionHandler sessionHandler;
    String mobile;
    OtpEditText otp;
    Button verify;
    RequestApi api;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        sessionHandler = new SessionHandler(this);
        mobile = sessionHandler.getLoggedInMobile();
        otp = findViewById(R.id.otp_box);
        verify = findViewById(R.id.verify);
        api = new RequestApi(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);

        setTitle("");
//        getOtp();

        verify.setOnClickListener(v -> {
            String otpStr = otp.getText().toString();
            if (StringHandler.isEmpty(otpStr)) {
                Toast.makeText(getApplicationContext(), "OTP is required", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            Log.d("TAG", "onCreate: " + otpStr);
            JSONObject object = new JSONObject();
            try {
                object.put("otp", otpStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            api.postRequest(object, response -> {
                Log.d("TAG", "onCreate: " + response);
                try {
                    int status = response.getInt("status");
                    if (status != 200) {
                        Toast.makeText(getApplicationContext(), "OTP verification failed", Toast.LENGTH_SHORT).show();

                        return;
                    }
                    if (Constant.PROCESS.equals("SIGNUP")) {
                        Intent home = new Intent(getApplicationContext(), ApplyInvitationCode.class);
                        startActivity(home);
//                        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        finish();
                    } else {
                        sessionHandler.setLogin(true);
                        Intent home = new Intent(getApplicationContext(), Home.class);
                        startActivity(home);
                        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, Server.VERIFY_OTP);

//            if (otpStr.equals(Constant.OTP_SERVER)) {
//
//            } else {
//                Toast.makeText(getApplicationContext(), "OTP verification failed", Toast.LENGTH_SHORT).show();
//            }

        });


    }

    private void getOtp() {
        api.validateSession(res -> {
            Log.d("TAG", "onResponse: " + res);
            try {
                JSONObject response = new JSONObject(res);
                int status = response.getInt("status");
                String message = response.getString("message");
                if (status == 200) {
                    //login success
                    JSONObject data = response.getJSONObject("data");
                    String _id = data.getString("id");
                    String _name = data.getString("fullName");
                    String _email = data.getString("email");
                    String _phone = data.getString("phone");
                    String _token = data.getString("accessToken"); // token
                    sessionHandler.setLoggedInMobile(_phone);
                    sessionHandler.setLoggedInUser(_id);
                    sessionHandler.setLoggedToken(_token);
                    sessionHandler.setUserName(_name);
                    sessionHandler.setLoggedInEmail(_email);

                    String isVerified = data.getString("isVerified");
                    String verificationCode = data.getString("verificationCode");
                    String subscriptionType = data.getString("subscriptionType");
                    String subscriptionExpiryDate = data.getString("subscriptionExpiryDate");
                    Constant.PURCHASED_SUBSCRIPTION_TYPE = subscriptionType;
                    Constant.PURCHASED_SUBSCRIPTION_EXPIRED_ON = subscriptionExpiryDate;

                    Constant.OTP_SERVER = verificationCode;
//                    Toast.makeText(getApplicationContext()  , ""+verificationCode , Toast.LENGTH_SHORT).show();


                } else {


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent login = new Intent(getApplicationContext(), Login.class);
        startActivity(login);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            sessionHandler.setLogin(false);
            sessionHandler.setLoggedToken(null);
            Intent login = new Intent(getApplicationContext(), Login.class);
            login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}