/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.project.mlmpro.R;
import com.project.mlmpro.adapter.TransactionAdapter;
import com.project.mlmpro.model.Transaction;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WalletScreen extends AppCompatActivity {
    Toolbar toolbar;
    RequestApi api;
    String TAG = WalletScreen.class.getSimpleName();

    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout dataHolder;
    TextView amountEdt;

    ArrayList<Transaction> list = new ArrayList<>();
    RecyclerView walletHistory;
    TransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_screen);

        toolbar = findViewById(R.id.toolbar);
        shimmerFrameLayout = findViewById(R.id.loader_screen);
        dataHolder = findViewById(R.id.data_holder);
        amountEdt = findViewById(R.id.amount_wallet);
        walletHistory = findViewById(R.id.history);
        walletHistory.setHasFixedSize(true);
        walletHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionAdapter(list, this);
        walletHistory.setAdapter(adapter);


        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);

        setTitle(getString(R.string.wallet));
        api = new RequestApi(this);

        getWalletDetails();
    }

    private void getWalletDetails() {
        api.getRequest(Server.GET_WALLET, response -> {
            Log.d(TAG, "getWalletDetails: " + response);
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            dataHolder.setVisibility(View.VISIBLE);
            try {
                JSONObject jsonObject = new JSONObject(response);
                int status = jsonObject.getInt("status");
                if (status == 200) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    String amount = data.getString("walletAmount");
                    if (amount == null || amount.equals("null")) {
                        amount = String.valueOf(0.00);
                    }
                    JSONArray arrayTransactions = data.getJSONArray("transctions");
                    amountEdt.setText(amount);
                    if (arrayTransactions.length() < 1) {
                        Log.w(TAG, "getWalletDetails: No transaction found ");
                        return;
                    }

                    for (int i = 0; i < arrayTransactions.length(); i++) {
                        JSONObject single = arrayTransactions.getJSONObject(i);
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
                    //Error

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}