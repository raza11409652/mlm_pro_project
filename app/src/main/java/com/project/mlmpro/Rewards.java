/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mlmpro.adapter.TransactionAdapter;
import com.project.mlmpro.model.Transaction;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Rewards extends Fragment {
    String TAG = Rewards.class.getSimpleName();

    RecyclerView rewardView;
    RequestApi requestApi;

    ArrayList<Transaction> list = new ArrayList<>();

    TransactionAdapter adapter;

    public Rewards() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestApi = new RequestApi(getContext());
        requestApi.getRequest(Server.GET_REFERRAL, response -> {
            Log.d(TAG, "onCreate: " + response);
            try {
                JSONObject object = new JSONObject(response);
                int status = object.getInt("status");
                if (status == 200) {
                    JSONArray array = object.getJSONArray("data");
                    if (array.length() < 1) {
                        Toast.makeText(getContext(), "No reward found", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject single = array.getJSONObject(i);
                        String id = single.getString("id");
                        String type = single.getString("type");
                        String amountR = single.getString("amount");
                        String secondUserId = single.getString("secondUserId");
                        String name = single.getString("secondUserName");
                        String date = single.getString("createdAt");
                        Transaction transaction = new Transaction(id, type, amountR, secondUserId, name, date);
                        list.add(transaction);
                    }
                    adapter.notifyDataSetChanged();


                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_rewards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rewardView = view.findViewById(R.id.reward_view);
        rewardView.setHasFixedSize(true);
        adapter = new TransactionAdapter(list, getContext());
        rewardView.setAdapter(adapter);
        rewardView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}