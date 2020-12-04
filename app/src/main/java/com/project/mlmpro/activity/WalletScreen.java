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

import com.facebook.shimmer.ShimmerFrameLayout;
import com.project.mlmpro.R;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WalletScreen extends AppCompatActivity {
    Toolbar toolbar;
    RequestApi api;
    String TAG = WalletScreen.class.getSimpleName();

    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout dataHolder ;
    TextView amountEdt ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_screen);

        toolbar = findViewById(R.id.toolbar);
        shimmerFrameLayout = findViewById(R.id.loader_screen);
        dataHolder = findViewById(R.id.data_holder);
        amountEdt = findViewById(R.id.amount_wallet );


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
                    JSONArray arrayTransactions = data.getJSONArray("transctions");
                    amountEdt.setText(amount);
                    if (arrayTransactions.length()<1){
                        Log.w(TAG, "getWalletDetails: No transaction found " );
                    }


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