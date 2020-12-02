package com.project.mlmpro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.project.mlmpro.R;
import com.project.mlmpro.component.Loader;
import com.project.mlmpro.utils.StringHandler;

/**
 * Login Screen
 */
public class Login extends AppCompatActivity {
    Button loginBtn, registerBtn;
    String TAG = Login.class.getSimpleName();
    EditText mobileEdt, passwordEdt;
    String mobile, password;
    Loader loader;

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
//            JSONObject object = new JSONObject();
//            object.put("mobile" , )
//            loginInt()

        });

        registerBtn.setOnClickListener(v -> {
            //start Register here
            Log.d(TAG, "onClick: Register btn clicked");
            Intent registerActivity = new Intent(getApplicationContext(), Register.class);

            updateScreen(registerActivity);

        });
    }

    private void updateScreen(Intent intent) {

        startActivity(intent);

    }
}