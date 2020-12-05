/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.mlmpro.R;
import com.project.mlmpro.utils.IntentSetting;
import com.project.mlmpro.utils.RequestApi;

import org.json.JSONObject;


public class Refer extends Fragment {
    RequestApi requestApi;
    String TAG = Refer.class.getSimpleName();
    TextView referCode;
    Button invite;
    IntentSetting setting;

    public Refer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestApi = new RequestApi(getContext());
        setting = new IntentSetting(getContext());
        requestApi.validateSession(response -> {
            Log.d(TAG, "onCreate: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");
                if (status == 200) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    String referralCode = data.getString("referralCode");
                    referCode.setText(referralCode);
                } else {
                    //
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_refer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        referCode = view.findViewById(R.id.refer_code);
        invite = view.findViewById(R.id.invite_btn);
        invite.setOnClickListener(v -> {

            String msg = getString(R.string.refer_sub_text);
//            String msg = "https://api.whatsapp.com/send?phone="+getString(R.string.whatsapp_to_number);
            setting.openShare(msg);

        });

    }
}