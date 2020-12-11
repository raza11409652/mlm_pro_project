package com.project.mlmpro.activity.service;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.project.mlmpro.R;
import com.project.mlmpro.utils.IntentSetting;

public class Graphics extends AppCompatActivity {
    Toolbar toolbar;
    Button whatsapp , call ;
    IntentSetting setting ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grpahics);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);

        setTitle(getString(R.string.service));
        setting  =new IntentSetting(this) ;
        whatsapp  =findViewById(R.id.whatsapp) ;
        call  =findViewById(R.id.call) ;
        whatsapp.setOnClickListener(v->{
            setting.openWhatsapp();
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.call(getString(R.string.call_to_number));
            }
        });

    }
}