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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.project.mlmpro.R;
import com.project.mlmpro.activity.service.Graphics;
import com.project.mlmpro.activity.service.Legal;
import com.project.mlmpro.activity.service.SocialMediaMarketing;
import com.project.mlmpro.activity.service.Software;
import com.project.mlmpro.activity.service.VideoEditing;
import com.project.mlmpro.adapter.HomeMenuAdapter;
import com.project.mlmpro.adapter.NavMenuAdapter;
import com.project.mlmpro.adapter.SliderAdapter;
import com.project.mlmpro.listener.HomeMenuListener;
import com.project.mlmpro.model.HomeMenu;
import com.project.mlmpro.model.ImageGallery;
import com.project.mlmpro.model.MenuModel;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Home extends AppCompatActivity implements HomeMenuListener {
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
    RecyclerView homeMenu;

    RequestApi api;
    ArrayList<HomeMenu> menus = new ArrayList<>();
    HomeMenuAdapter homeMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        menuList = findViewById(R.id.menu_list);
        slider = findViewById(R.id.image_slider);
        indicator = findViewById(R.id.indicator);

        homeMenu = findViewById(R.id.home_menu);
        homeMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        homeMenuAdapter = new HomeMenuAdapter(menus, this, this);
        homeMenu.setAdapter(homeMenuAdapter);
        homeMenu.setHasFixedSize(true);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        adapterSlider = new SliderAdapter(this, list);
//        adapterSlider.notifyDataSetChanged();
        slider.setAdapter(adapterSlider);
        indicator.setupWithViewPager(slider, true);
        api = new RequestApi(this);

//        setTitle("");


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open,
                R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        prepareMenuData();
        populateExpandableList();


        //Populate Gallery Image()

        fetchImageSlider();
//
//        ImageGallery imageGallery = new ImageGallery("https://res.cloudinary.com/swiggy/image/upload/fl_lossy,f_auto,q_auto,w_520,h_520/rng/md/carousel/production/qnb4ihhwxcfwlbbrrhc", "Title");
//        list.add(imageGallery);
//        ImageGallery imageGallery1 = new ImageGallery("https://res.cloudinary.com/swiggy/image/upload/fl_lossy,f_auto,q_auto,w_520,h_520/rng/md/carousel/production/qnb4ihhwxcfwlbbrrhc", "Title");
//        list.add(imageGallery1);
//        adapterSlider.notifyDataSetChanged();

        //Populate Home Menu
        HomeMenu growth_company = new HomeMenu("Current Growth Company", null, R.drawable.currentgrowthcompany);
        menus.add(growth_company);
        HomeMenu top_leaders = new HomeMenu("Company Top Leaders", null, R.drawable.companytopleader);
        menus.add(top_leaders);
        HomeMenu plan_Details = new HomeMenu("Company Plan Detail", null, R.drawable.plandetails);
        menus.add(plan_Details);

        HomeMenu top_mlm_trainer = new HomeMenu("Top MLM Trainer", null, R.drawable.topmlmtrainer);
        menus.add(top_mlm_trainer);


        HomeMenu mlm_company = new HomeMenu("All MLM Company", null, R.drawable.allmlmcompany);
        menus.add(mlm_company);
        HomeMenu top_network = new HomeMenu("Top Network Company", null, R.drawable.topnetwork);
        menus.add(top_network);
        HomeMenu training_seminar_update = new HomeMenu("Training Seminar Update", null, R.drawable.trainingseminar);
        menus.add(training_seminar_update);
        homeMenuAdapter.notifyDataSetChanged();


    }

    private void fetchImageSlider() {

        api.getRequest(Server.SLIDER_IMAGE, response -> {
//            Log.d(TAG, "fetchImageSlider: " + response);
            try {
                JSONObject object = new JSONObject(response);
                int status = object.getInt("status");
                if (status == 200) {
                    JSONArray images = object.getJSONArray("data");
                    for (int i = 0; i < images.length(); i++) {
                        JSONObject single = images.getJSONObject(i);
                        String title = single.getString("name");
                        String url = single.getString("imageStr");
                        ImageGallery imageGallery = new ImageGallery(url, title);
                        list.add(imageGallery);
                    }
                    adapterSlider.notifyDataSetChanged();
                } else {
                    //Status code invalid
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

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


    @Override
    public void onItemClick(HomeMenu menu) {
        Log.d(TAG, "onItemClick: Home menu Item click");

    }
}