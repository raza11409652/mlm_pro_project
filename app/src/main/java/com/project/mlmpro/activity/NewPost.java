/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.project.mlmpro.R;
import com.project.mlmpro.component.Loader;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.Server;
import com.project.mlmpro.utils.SessionHandler;
import com.project.mlmpro.utils.StringHandler;

import org.json.JSONObject;

public class NewPost extends AppCompatActivity {

    SessionHandler sessionHandler;
    Toolbar toolbar;
    String imageUrl = null, postData = null, sender, senderName, senderImage = "N/A";
    EditText postEdt;
    Button post;
    String TAG = NewPost.class.getSimpleName();
    RequestApi api;
    Loader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        toolbar = findViewById(R.id.toolbar);
        post = findViewById(R.id.post_btn);
        postEdt = findViewById(R.id.post_data);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);

        setTitle(getString(R.string.new_post_heading));

        sessionHandler = new SessionHandler(this);
        api = new RequestApi(this);
        sender = sessionHandler.getLoggedInUser();
        senderName = sessionHandler.getUserName();
//        senderImage = null;
        loader = new Loader(this);

        post.setOnClickListener(v -> {
            postData = postEdt.getText().toString().trim();
//            Log.d(TAG, "onCreate: " + postData);
            if (StringHandler.isEmpty(postData)) {
                Toast.makeText(getApplicationContext(), "Please write something", Toast.LENGTH_SHORT).show();
                return;
            }


//            Log.d(TAG, "onCreate: ");
            try {
                JSONObject object = new JSONObject();
                object.put("senderID", sender);
                object.put("senderName", senderName);
                object.put("postText", postData);
                object.put("postImage", imageUrl);
                object.put("senderImage", senderImage);
                Log.d(TAG, "onCreate: " + object);
                postCreate(object);
            } catch (Exception e) {
                e.printStackTrace();
            }


        });


    }

    private void postCreate(JSONObject object) {
        loader.show(getString(R.string.loading));
        api.postRequest(object, response -> {
            Log.d(TAG, "postCreate: " + response);
            loader.dismiss();
        }, Server.NEW_POST);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}