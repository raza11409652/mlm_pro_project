package com.project.mlmpro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

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

/**
 * Login Screen
 */
public class Login extends AppCompatActivity {
    Button loginBtn, registerBtn;
    String TAG = Login.class.getSimpleName();
    EditText mobileEdt, passwordEdt;
    String mobile, password;
    Loader loader;
    RequestApi api;
    Flashbar flashbar = null;
    AlertFlash alertFlash;
    SessionHandler sessionHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**
         * Todo :: init views
         */

        registerBtn = findViewById(R.id.register_btn);
        mobileEdt = findViewById(R.id.mobile_input);
        passwordEdt = findViewById(R.id.password_input);
        loginBtn = findViewById(R.id.login_btn);
        loader = new Loader(this);
        api = new RequestApi(this);
        sessionHandler = new SessionHandler(this);
        alertFlash = new AlertFlash(this, this);

        mobileEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean valid = StringHandler.isValidMobile(s.toString());
                if (!valid) {
                    mobileEdt.setError("Mobile invalid");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loginBtn.setOnClickListener(v -> {
            mobile = mobileEdt.getText().toString();
            password = passwordEdt.getText().toString();
            if (!StringHandler.isValidMobile(mobile)) {
                return;
            }
            if (StringHandler.isEmpty(password)) {
                return;
            }
            JSONObject object = new JSONObject();

            try {
                object.put("phone", mobile);
                object.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            runOnUiThread(() -> {
                loginInit(object);
            });

        });

        registerBtn.setOnClickListener(v -> {
            //start Register here
            Log.d(TAG, "onClick: Register btn clicked");
            Intent registerActivity = new Intent(getApplicationContext(), Register.class);

            updateScreen(registerActivity);

        });
    }

    private void loginInit(JSONObject object) {
        loader.show(getString(R.string.loading));
        api.postRequest(object, response -> {
            Log.d(TAG, "loginInit: " + response);
            loader.dismiss();
            try {
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
                    String _profile = data.getString("imageStr");
                    sessionHandler.setLoggedInMobile(_phone);
                    sessionHandler.setLoggedInUser(_id);
                    sessionHandler.setLoggedToken(_token);
                    sessionHandler.setUserName(_name);
                    sessionHandler.setLoggedInEmail(_email);
                    sessionHandler.setProfileImage(_profile);

//                    sessionHandler.setLogin(true);
                    String isVerified = data.getString("isVerified");
                    String subscriptionType = data.getString("subscriptionType");
                    String subscriptionExpiryDate = data.getString("subscriptionExpiryDate");
                    Constant.PURCHASED_SUBSCRIPTION_TYPE =subscriptionType ;
                    Constant.PURCHASED_SUBSCRIPTION_EXPIRED_ON =subscriptionExpiryDate ;
                    Constant.LOGEDTOKEN = _token ;
                    if (isVerified.equals("0")) {
                        //Not verified User

                        Constant.PROCESS = "1";
                        Intent intent = new Intent(getApplicationContext()  , VerifyOtp.class);
                        startActivity(intent);
//                        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                    }else{
                        Intent home = new Intent(getApplicationContext(), Home.class);
                        startActivity(home);
                        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                    }


//                    User user = new User(_id ,_name , _email ,_phone, _token  );


                } else {
                    if (flashbar != null) {
                        flashbar = null;
                    }
                    flashbar = alertFlash.alertError("" + message);
                    flashbar.show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, Server.USER_LOGIN);


    }

    private void updateScreen(Intent intent) {

        startActivity(intent);

    }
}