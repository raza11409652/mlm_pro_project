/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity.service;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.project.mlmpro.R;
import com.project.mlmpro.utils.IntentSetting;

public class VideoEditing extends AppCompatActivity {

    Toolbar toolbar;
    Button call ,whatsapp ;
    IntentSetting setting  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_editing);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        setting = new IntentSetting(getApplicationContext()) ;

        setTitle(getString(R.string.service));
        call  =findViewById(R.id.call) ;
        whatsapp  =findViewById(R.id.whatsapp) ;
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.call(getString(R.string.call_to_number));
            }
        });
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.openWhatsapp();
            }
        });
    }
}