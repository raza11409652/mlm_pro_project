package com.project.mlmpro;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.project.mlmpro.activity.Home;
import com.project.mlmpro.activity.Login;
import com.project.mlmpro.utils.Constant;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.SessionHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This will work as splash screen
 * and entry point for the App
 */
public class MainActivity extends AppCompatActivity {
    SessionHandler sessionHandler;
    RequestApi requestApi;
    String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionHandler = new SessionHandler(this);
        requestApi = new RequestApi(this);
//        sessionHandler.setUserDeviceToken(Constant.DEVICE_TOKEN );
        if (sessionHandler.getIsLoggedIn()) {
//            Intent home = new Intent(getApplicationContext(), Home.class);
//            updateScreen(home);
            requestApi.validateSession(response -> {
//                Log.d(TAG, "onResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    int status = object.getInt("status");
                    if (status == 200) {
                        JSONObject data = object.getJSONObject("data");
                        String _id = data.getString("id");
                        String _name = data.getString("fullName");
                        String _email = data.getString("email");
                        String _phone = data.getString("phone");
                        String _token = data.getString("accessToken");
                        String _isVerified = data.getString("isVerified");
                        if (_isVerified.equals("0")) {
                            //false
                            Constant.PROCESS = "SIGNUP";
                            Intent verify = new Intent(getApplicationContext(), VerifyOtp.class);
                            updateScreen(verify);
                        } else {
                            Intent home = new Intent(getApplicationContext(), Home.class);
                            updateScreen(home);
                        }
//
                    } else {
                        Intent login = new Intent(getApplicationContext(), Login.class); //login
                        updateScreen(login);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } else {
            Intent login = new Intent(getApplicationContext(), Login.class); //login
//        startActivity(login);
            updateScreen(login);
        }

//        Intent login = new Intent(getApplicationContext(), Login.class); //login
////        startActivity(login);
//        updateScreen(login);

    }


    private void updateScreen(Intent intent) {
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();

    }
}