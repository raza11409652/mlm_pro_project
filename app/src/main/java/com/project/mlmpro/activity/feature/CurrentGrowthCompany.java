/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity.feature;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.project.mlmpro.R;
import com.project.mlmpro.adapter.ReferralPagerAdapter;
import com.project.mlmpro.fragment.PendingCompany;
import com.project.mlmpro.fragment.VerifiedCompany;
import com.project.mlmpro.utils.RequestApi;

public class CurrentGrowthCompany extends AppCompatActivity {

    Toolbar toolbar;
    RequestApi requestApi;
    ViewPager viewPager;
    ReferralPagerAdapter adapter;

    TabLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_growth_company);

        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);

        setTitle(getString(R.string.current_growth));
        layout = findViewById(R.id.layout_tab);
        viewPager = findViewById(R.id.view_pager);
        adapter = new ReferralPagerAdapter(getSupportFragmentManager());

        requestApi = new RequestApi(this);

        adapter.addFrag(new PendingCompany(), "Pending");
        adapter.addFrag(new VerifiedCompany(), "Verified");
        viewPager.setAdapter(adapter);
        layout.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.post_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        if (item.getItemId() == R.id.new_post_menu) {
//            open Add Men list
            Intent newF = new Intent(getApplicationContext(), NewGrowthCompany.class);
//            newF.putExtra("title", getString(R.string.mlm_company_list));
//            newF.putExtra("type", String.valueOf(0));
            startActivity(newF);

        }
        return super.onOptionsItemSelected(item);

    }
}