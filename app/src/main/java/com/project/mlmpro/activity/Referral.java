/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.project.mlmpro.R;
import com.project.mlmpro.Rewards;
import com.project.mlmpro.adapter.ReferralPagerAdapter;
import com.project.mlmpro.fragment.Refer;

public class Referral extends AppCompatActivity {
    TabLayout layout;
    String TAG = Referral.class.getSimpleName();
    ViewPager viewPager;
    ReferralPagerAdapter referralPagerAdapter;
    Toolbar toolbar;
//Refer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layout = findViewById(R.id.layout_tab);
        viewPager = findViewById(R.id.view_pager);
        referralPagerAdapter = new ReferralPagerAdapter(getSupportFragmentManager());
        referralPagerAdapter.addFrag(new Refer(), getString(R.string.refer));
        referralPagerAdapter.addFrag(new Rewards(), getString(R.string.reward));

        viewPager.setAdapter(referralPagerAdapter);
        layout.setupWithViewPager(viewPager);


        setTitle("Referrals");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}