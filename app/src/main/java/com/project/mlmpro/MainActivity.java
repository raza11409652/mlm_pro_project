package com.project.mlmpro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.project.mlmpro.activity.Home;
import com.project.mlmpro.activity.Login;
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
        if (sessionHandler.getIsLoggedIn()) {
//            Intent home = new Intent(getApplicationContext(), Home.class);
//            updateScreen(home);
            requestApi.validateSession(response -> {
                Log.d(TAG, "onResponse: " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    int status = object.getInt("status");
                    if (status == 200) {
                        Intent home = new Intent(getApplicationContext(), Home.class);
                        updateScreen(home);
                    } else {
                        Intent login = new Intent(getApplicationContext(), Login.class);
                        updateScreen(login);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } else {
            Intent login = new Intent(getApplicationContext(), Login.class);
//        startActivity(login);
            updateScreen(login);
        }


    }


    private void updateScreen(Intent intent) {
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();

    }
}