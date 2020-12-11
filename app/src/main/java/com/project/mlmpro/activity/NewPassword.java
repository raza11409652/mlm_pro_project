/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.project.mlmpro.R;
import com.project.mlmpro.utils.Constant;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.Server;
import com.project.mlmpro.utils.StringHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class NewPassword extends AppCompatActivity {

    Button resend, reset;
    EditText otpEdt, pswEdt, pswConfEdt;
    String otp, psw, confPsw;

    RequestApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        resend = findViewById(R.id.resend_btn);
        reset = findViewById(R.id.reset_btn);

        otpEdt = findViewById(R.id.otp_input);
        pswEdt = findViewById(R.id.password);
        pswConfEdt = findViewById(R.id.conf_password);
        api = new RequestApi(this);
        Toast.makeText(getApplicationContext(), "" + Constant.OTP_FORGET, Toast.LENGTH_SHORT).show();

        resend.setOnClickListener(v -> {
            JSONObject object = new JSONObject();
            try {
                object.put("phone", Constant.FORGET_MOBILE);

            } catch (Exception e) {
                e.printStackTrace();
            }
            api.postRequest(object, response -> {
                try {
                    int status = response.getInt("status");
                    if (status == 200) {
//                        String otp =
                        JSONObject data = response.getJSONObject("data");
                        String otp = data.getString("otp");
                        Constant.OTP_FORGET = otp;
                        Toast.makeText(getApplicationContext(), "NEW OTP " + otp, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, Server.FORGET_PASSWORD);
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psw = pswEdt.getText().toString();
                confPsw = pswConfEdt.getText().toString();
                otp = otpEdt.getText().toString();
                if (StringHandler.isEmpty(otp)) {
                    otpEdt.setError("OTP required");
                    return;
                }
                if (!psw.equals(confPsw)) {
                    //
                    Toast.makeText(getApplicationContext(), "Password mismatch", Toast.LENGTH_SHORT)
                            .show();
                    return;

                }

                //Berify
                JSONObject verifyObj = new JSONObject();
                try {
                    verifyObj.put("otp" , otp);
                    verifyObj.put("phone" , Constant.FORGET_MOBILE);
                    verifyObj.put("password" , psw);
                }catch (Exception e){
                    e.printStackTrace();
                }
                api.postRequest(verifyObj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", "onResponse: " + response);
//                        Log.d(TAG, "onResponse: " + response);
                        try {
                            int status = response.getInt("status");
                            String msg = response.getString("message");

                            if (status == 200) {
//
                               resetNow();
                            } else {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } , Server.SET_NEW_PASSWORD);




            }
        });


    }

    public  void resetNow(){
        JSONObject object = new JSONObject();
        try {
            object.put("phone", Constant.FORGET_MOBILE);
            object.put("password", psw);

        } catch (Exception e) {
            e.printStackTrace();
        }

        api.postRequest(object, response -> {

            try {
                int status = response.getInt("status");
                String msg = response.getString("message");

                if (status == 200) {
//
                    Toast.makeText(getApplicationContext(), "Password updated", Toast.LENGTH_SHORT)
                            .show();

                    Intent login = new Intent(getApplicationContext(), Login.class);
                    login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(login);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Server.SET_NEW_PASSWORD);
    }
}