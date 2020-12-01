package com.project.mlmpro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.project.mlmpro.R;

/**
 * Login Screen
 */
public class Login extends AppCompatActivity {
    Button loginBtn, registerBtn;
    String TAG = Login.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**
         * Todo :: init views
         */

        registerBtn = findViewById(R.id.register_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start Register here
                Log.d(TAG, "onClick: Register btn clicked");
                Intent registerActivity = new Intent(getApplicationContext(), Register.class);

                updateScreen(registerActivity);

            }
        });
    }

    private void updateScreen(Intent intent) {

        startActivity(intent);

    }
}