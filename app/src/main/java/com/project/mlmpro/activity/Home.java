/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.project.mlmpro.R;
import com.project.mlmpro.activity.service.Graphics;
import com.project.mlmpro.activity.service.Legal;
import com.project.mlmpro.activity.service.SocialMediaMarketing;
import com.project.mlmpro.activity.service.Software;
import com.project.mlmpro.activity.service.VideoEditing;
import com.project.mlmpro.adapter.NavMenuAdapter;
import com.project.mlmpro.adapter.SliderAdapter;
import com.project.mlmpro.model.ImageGallery;
import com.project.mlmpro.model.MenuModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Home extends AppCompatActivity {
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    ExpandableListView menuList;
    NavMenuAdapter adapter;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    String TAG = Home.class.getSimpleName();
    ArrayList<ImageGallery> list = new ArrayList<>();
    ViewPager slider;
    SliderAdapter adapterSlider;
    TabLayout indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        menuList = findViewById(R.id.menu_list);
        slider = findViewById(R.id.image_slider);
        indicator = findViewById(R.id.indicator);


        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        adapterSlider = new SliderAdapter(this, list);
//        adapterSlider.notifyDataSetChanged();
        slider.setAdapter(adapterSlider);
        indicator.setupWithViewPager(slider, true);

//        setTitle("");


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open,
                R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        prepareMenuData();
        populateExpandableList();


        //Populate Gallery Image()

        ImageGallery imageGallery = new ImageGallery("https://res.cloudinary.com/swiggy/image/upload/fl_lossy,f_auto,q_auto,w_520,h_520/rng/md/carousel/production/qnb4ihhwxcfwlbbrrhc", "Title");
        list.add(imageGallery);
        ImageGallery imageGallery1 = new ImageGallery("https://res.cloudinary.com/swiggy/image/upload/fl_lossy,f_auto,q_auto,w_520,h_520/rng/md/carousel/production/qnb4ihhwxcfwlbbrrhc", "Title");
        list.add(imageGallery1);
        adapterSlider.notifyDataSetChanged();
    }

    private void populateExpandableList() {
        adapter = new NavMenuAdapter(this, headerList, childList);
        menuList.setAdapter(adapter);
        menuList.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            if (headerList.get(groupPosition).isGroup) {
                if (!headerList.get(groupPosition).hasChildren) {
                    Log.d(TAG, "onGroupClick: " + headerList.get(groupPosition));

                }
            }
            return false;
        });

        menuList.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            if (childList.get(headerList.get(groupPosition)) != null) {
                MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                if (model.intent != null) {
                    updateUi(model.intent);
                } else {
                    //no intent found
                }
            }
            return false;
        });


    }

    private void updateUi(Intent intent) {
        //check if drawer is open close first
        closeDrawer();
        startActivity(intent);
    }

    private void prepareMenuData() {
        MenuModel menuModel = new MenuModel("Home", null, false, true);
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

        /**
         * Feature
         */
        MenuModel feature = new MenuModel("Feature", null, false, true);
        headerList.add(feature);
        if (!feature.hasChildren) {
            childList.put(feature, null);
        }
        /**
         * Services
         */
        MenuModel service = new MenuModel("Service", null, true, true);
        headerList.add(service);
        List<MenuModel> services = new ArrayList<>();
        MenuModel graphics = new MenuModel(getString(R.string.graphics), new Intent(getApplicationContext(), Graphics.class), false, false);
        services.add(graphics);
        MenuModel software = new MenuModel(getString(R.string.software), new Intent(getApplicationContext(), Software.class), false, false);
        services.add(software);
        MenuModel video = new MenuModel(getString(R.string.video), new Intent(getApplicationContext(), VideoEditing.class), false, false);
        services.add(video);
        MenuModel legal = new MenuModel(getString(R.string.legal), new Intent(getApplicationContext(), Legal.class), false, false);
        services.add(legal);
        MenuModel socialMediaMarket = new MenuModel(getString(R.string.social_media_marketing), new Intent(getApplicationContext(), SocialMediaMarketing.class), false, false);
        services.add(socialMediaMarket);
        if (service.hasChildren) {
            childList.put(service, services);
        }


    }

    private void closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.post_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }


}