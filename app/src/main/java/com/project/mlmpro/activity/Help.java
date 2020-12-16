/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project.mlmpro.R;
import com.project.mlmpro.utils.IntentSetting;

public class Help extends AppCompatActivity {
    LinearLayout parent ;
    TextView emailInfo , emailSupport  , emailEnquiry , emailAd ;
    Button whatsapp , call ;
    IntentSetting setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        parent = findViewById(R.id.parent);

        emailInfo  =findViewById(R.id.info_email);
        emailSupport  =findViewById(R.id.support_email);
        emailEnquiry  =findViewById(R.id.enq_email);
        emailAd =findViewById(R.id.ad_email);
        Linkify.addLinks(emailInfo ,Linkify.EMAIL_ADDRESSES);
        Linkify.addLinks(emailEnquiry ,Linkify.EMAIL_ADDRESSES);
        Linkify.addLinks(emailSupport ,Linkify.EMAIL_ADDRESSES);
        Linkify.addLinks(emailAd ,Linkify.EMAIL_ADDRESSES);
        setting = new IntentSetting(this);
        whatsapp = findViewById(R.id.whatsapp_support);
        call = findViewById(R.id.call_support);

        whatsapp.setOnClickListener(v->{
            setting.openWhatsappWithMobile("9672995845");
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.call("9672995845");
            }
        });

    }
}