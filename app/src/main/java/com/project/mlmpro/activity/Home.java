/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.project.mlmpro.R;
import com.project.mlmpro.activity.feature.CompanyPlanDetail;
import com.project.mlmpro.activity.feature.CompanyTopLeaders;
import com.project.mlmpro.activity.feature.CurrentGrowthCompany;
import com.project.mlmpro.activity.feature.FastCourierService;
import com.project.mlmpro.activity.feature.MLMCompanyList;
import com.project.mlmpro.activity.feature.MLMSeminalUpdate;
import com.project.mlmpro.activity.feature.MlmProductManufacturing;
import com.project.mlmpro.activity.feature.MlmProductServiceProvider;
import com.project.mlmpro.activity.feature.TopMLMTrainer;
import com.project.mlmpro.activity.feature.TopNetworkCompany;
import com.project.mlmpro.activity.service.Graphics;
import com.project.mlmpro.activity.service.Legal;
import com.project.mlmpro.activity.service.SocialMediaMarketing;
import com.project.mlmpro.activity.service.Software;
import com.project.mlmpro.activity.service.VideoEditing;
import com.project.mlmpro.adapter.HomeMenuAdapter;
import com.project.mlmpro.adapter.NavMenuAdapter;
import com.project.mlmpro.adapter.PostAdapter;
import com.project.mlmpro.adapter.SliderAdapter;
import com.project.mlmpro.listener.HomeMenuListener;
import com.project.mlmpro.listener.PostListener;
import com.project.mlmpro.model.HomeMenu;
import com.project.mlmpro.model.ImageGallery;
import com.project.mlmpro.model.MenuModel;
import com.project.mlmpro.model.Post;
import com.project.mlmpro.utils.AlertFlash;
import com.project.mlmpro.utils.Constant;
import com.project.mlmpro.utils.IntentSetting;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.Server;
import com.project.mlmpro.utils.SessionHandler;
import com.project.mlmpro.utils.StringHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity implements HomeMenuListener, PostListener {
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
    IntentSetting setting;
    ShimmerFrameLayout loaderScreen;

    //
//    String limit = String.valueOf(20) , skip="0" ;
    AlertFlash alertFlash;
    CircleImageView _profile;

    RequestApi api;
    ArrayList<HomeMenu> menus = new ArrayList<>();
    HomeMenuAdapter homeMenuAdapter;

    RecyclerView postListView;
    ArrayList<Post> posts = new ArrayList<>();
    PostAdapter postAdapter;
    NavigationView navigationView;
    TextView nameUser, companyname, address;
    SessionHandler sessionHandler;

    int visibleItemCount, totalItemCount, pastVisiblesItems;
    LinearLayoutManager postLayoutManager, menuLayoutManager;
    String limit = String.valueOf(0), skip = String.valueOf(0);

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sessionHandler = new SessionHandler(this);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        menuList = findViewById(R.id.menu_list);
        slider = findViewById(R.id.image_slider);
        indicator = findViewById(R.id.indicator);
        postListView = findViewById(R.id.post_list);
        postListView.setHasFixedSize(true);
        loaderScreen = findViewById(R.id.loader);
        loaderScreen.startShimmer();


        postLayoutManager = new LinearLayoutManager(this);


        navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        headerView.setOnClickListener(v -> {
            Intent updatep = new Intent(getApplicationContext(), UpdateProfile.class);
            startActivity(updatep);
        });
        nameUser = headerView.findViewById(R.id.name);
        _profile = headerView.findViewById(R.id.image);
        nameUser.setText(StringHandler.captalize(sessionHandler.getUserName()));
        companyname = headerView.findViewById(R.id.company_name);
        address = headerView.findViewById(R.id.address);
        try {
            companyname.setText(StringHandler.captalize(sessionHandler.getCompanyName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        address.setText(sessionHandler.getLoggedInMobile());

        Picasso.get().load(sessionHandler.getProfileImage()).placeholder(R.drawable.logo_circle)
                .placeholder(R.drawable.logo_circle).into(_profile);

        menuLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        homeMenu = findViewById(R.id.home_menu);
        homeMenu.setLayoutManager(menuLayoutManager);
        homeMenuAdapter = new HomeMenuAdapter(menus, this, this);
        homeMenu.setAdapter(homeMenuAdapter);
        homeMenu.setHasFixedSize(true);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);


//
        api = new RequestApi(this);
        setting = new IntentSetting(this);
        alertFlash = new AlertFlash(this, this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open,
                R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        prepareMenuData();
        populateExpandableList();

        final int duration = 10;
        final int pixelsToMove = 30;
        final Handler mHandler = new Handler(Looper.getMainLooper());
        final Runnable SCROLLING_RUNNABLE = new Runnable() {

            @Override
            public void run() {
                homeMenu.smoothScrollBy(pixelsToMove, 0);
                mHandler.postDelayed(this, duration);
            }
        };

        homeMenu.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = menuLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastItem == menuLayoutManager.getItemCount() - 1) {
                    mHandler.removeCallbacks(SCROLLING_RUNNABLE);
                    Handler postHandler = new Handler();
                    postHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            homeMenu.setAdapter(null);
                            homeMenu.setAdapter(homeMenuAdapter);
                            mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);
                        }
                    }, 2000);
                }

            }
        });


        //Populate Gallery Image()

//        fetchImageSlider();


        fetchPost(limit, skip);
        runOnUiThread(() -> {

            fetchImageSlider();

        });

        //Populate Home Menu
        HomeMenu growth_company = new HomeMenu("Current Growth Company", new Intent(getApplicationContext(), CurrentGrowthCompany.class), R.drawable.currentgrowthcompany);
        menus.add(growth_company);
        HomeMenu top_leaders = new HomeMenu("Company Top Leaders", new Intent(getApplicationContext(), CompanyTopLeaders.class), R.drawable.companytopleader);
        menus.add(top_leaders);
        HomeMenu plan_Details = new HomeMenu("Company Plan Detail", new Intent(getApplicationContext(), CompanyPlanDetail.class), R.drawable.plandetails);
        menus.add(plan_Details);

        HomeMenu top_mlm_trainer = new HomeMenu("Top MLM Trainer", new Intent(getApplicationContext(), TopMLMTrainer.class), R.drawable.topmlmtrainer);
        menus.add(top_mlm_trainer);


        HomeMenu mlm_company = new HomeMenu("All MLM Company", new Intent(getApplicationContext(), MLMCompanyList.class), R.drawable.allmlmcompany);
        menus.add(mlm_company);
        HomeMenu top_network = new HomeMenu("Top Networker", new Intent(getApplicationContext(), TopNetworkCompany.class), R.drawable.topnetwork);
        menus.add(top_network);
        HomeMenu training_seminar_update = new HomeMenu("Training Seminar Update", new Intent(getApplicationContext(), MLMSeminalUpdate.class), R.drawable.trainingseminar);
        menus.add(training_seminar_update);
        HomeMenu service_provider = new HomeMenu("MLM Product Service Provider", new Intent(getApplicationContext(), MlmProductServiceProvider.class), R.drawable.service_provider);
        menus.add(service_provider);
        HomeMenu manufa = new HomeMenu("MLM Product Manufacturing", new Intent(getApplicationContext(), MlmProductManufacturing.class), R.drawable.manufactrintg_ps);
        menus.add(manufa);
        HomeMenu fcs = new HomeMenu("Fast Courier Service", new Intent(getApplicationContext(), FastCourierService.class), R.drawable.fcs);
        menus.add(fcs);
        homeMenuAdapter.notifyDataSetChanged();


        postListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int item = postLayoutManager.findLastVisibleItemPosition();
                if (item == posts.size() - 1) {
                    //last item reach

                    int skipI = Integer.parseInt(skip);
                    skipI = skipI + posts.size();

                    fetchPost(limit, String.valueOf(skipI));


                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: " + postLayoutManager.findLastVisibleItemPosition());

            }

        });

        api.validateSession(res -> {

            Log.d(TAG, "onCreate: " + res);
            try {
                JSONObject response = new JSONObject(res);
                int status = response.getInt("status");
                String message = response.getString("message");
                if (status == 200) {
                    //login success
                    JSONObject data = response.getJSONObject("data");
                    String _id = data.getString("id");
                    String _name = data.getString("fullName");
                    String _email = data.getString("email");
                    String _phone = data.getString("phone");
                    String _token = data.getString("accessToken"); // token
                    sessionHandler.setLoggedInMobile(_phone);
                    sessionHandler.setLoggedInUser(_id);
                    sessionHandler.setLoggedToken(_token);
                    sessionHandler.setUserName(_name);
                    sessionHandler.setLoggedInEmail(_email);
//                    sessionHandler.setLogin(true);
                    String isVerified = data.getString("isVerified");
                    String subscriptionType = data.getString("subscriptionType");
                    String subscriptionExpiryDate = data.getString("subscriptionExpiryDate");
                    Constant.PURCHASED_SUBSCRIPTION_TYPE = subscriptionType;
                    Constant.PURCHASED_SUBSCRIPTION_EXPIRED_ON = subscriptionExpiryDate;


//                    User user = new User(_id ,_name , _email ,_phone, _token  );


                } else {


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });


    }

    private void fetchPost(String limit, String skip) {

        String url = Server.GET_POST + "?limit=" + limit + "&skip=" + skip;
        Log.d(TAG, "fetchPost: " + url);
        runOnUiThread(() -> api.getRequest(url, response -> {
//            Log.d(TAG, "onResponse: " + response);
            try {
                JSONObject object = new JSONObject(response);
//
                int status = object.getInt("status");

                if (status == 200) {
                    JSONArray array = object.getJSONArray("data");
                    if (array.length() < 1) {
                        return;
                    }
                    if (skip.equals("0")) {
                        posts = new ArrayList<>();
                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject single = array.getJSONObject(i);
                        String _id = single.getString("id");
                        String _senderId = single.getString("senderID");
                        String _senderName = single.getString("senderName");
                        String _senderImage = single.getString("senderImage");
                        String _postText = single.getString("postText");
                        String _postImage = single.getString("postImage");
                        String _likesCount = single.getString("noOfLikes");
                        String _sendBirdGroupId = single.getString("sendBirdGroupId");
                        String _isLiked = single.getString("likedByYou");
                        String _createdOn = single.getString("createdAt");
                        String _updatedOn = single.getString("updatedAt");
                        Post post = new Post(_id, _senderId, _senderName, _senderImage, _postText, _postImage, _likesCount, _sendBirdGroupId, _createdOn, _updatedOn, _isLiked);
                        posts.add(post);
//                        Log.d(TAG, "fetchPost: " + _senderImage);

                    }

                   try{
                       loaderScreen.stopShimmer();
                       loaderScreen.setVisibility(View.GONE);
                   }catch (Exception e){

                   }
                    postListView.setVisibility(View.VISIBLE);
                    postAdapter = new PostAdapter(posts, Home.this, Home.this);
                    postListView.setAdapter(postAdapter);
                    postAdapter.notifyDataSetChanged();
                    postListView.setLayoutManager(postLayoutManager);

                } else {
                    //Eroror form server
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }));
    }

    private void fetchImageSlider() {

        api.getRequest(Server.SLIDER_IMAGE, response -> {
//            Log.d(TAG, "fetchImageSlider: " + response);
            try {
                JSONObject object = new JSONObject(response);
                int status = object.getInt("status");
                if (status == 200) {
                    JSONArray images = object.getJSONArray("data");
                    if (images.length() < 1) {
                        slider.setVisibility(View.GONE);
                        indicator.setVisibility(View.GONE);

                    }
                    for (int i = 0; i < images.length(); i++) {
                        JSONObject single = images.getJSONObject(i);
                        String title = single.getString("name");
                        String url = single.getString("imageStr");
                        ImageGallery imageGallery = new ImageGallery(url, title);
                        list.add(imageGallery);
                    }
                    adapterSlider = new SliderAdapter(this, list);
                    slider.setAdapter(adapterSlider);
                    indicator.setupWithViewPager(slider, true);
                    adapterSlider.notifyDataSetChanged();
                    final Handler handler = new Handler();
                    final Runnable update = new Runnable() {
                        public void run() {
                            if (slider.getCurrentItem() == adapterSlider.getCount() - 1) { //adapter is your custom ViewPager's adapter
                                slider.setCurrentItem(0);
                            } else {
                                slider.setCurrentItem(slider.getCurrentItem() + 1, true);
                            }
                        }
                    };

                    timer = new Timer(); // This will create a new Thread
                    timer.schedule(new TimerTask() { // task to be scheduled
                        @Override
                        public void run() {
                            handler.post(update);
                        }
                    }, DELAY_MS, PERIOD_MS);
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
                    Log.d(TAG, "onGroupClick: " + headerList.get(groupPosition).menuName);
                    Intent next = headerList.get(groupPosition).intent;
//                    Log.d(TAG, "populateExpandableList: " + next);
                    if (next != null) {
                        updateUi(next);
                    }
                    if (next == null && headerList.get(groupPosition).menuName.equals("Log Out")) {
                        //Logout function
//                        Log.d(TAG, "populateExpandableList: logout here");
                        closeDrawer();
                        alertFlash.logoutAlert();
                    }

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
        MenuModel feature = new MenuModel("Feature", null, true, true);
        headerList.add(feature);
//        if (!feature.hasChildren) {
//            childList.put(feature, null);
//        }
        List<MenuModel> features = new ArrayList<>();
        MenuModel mlmCompanylist = new MenuModel("MLM Company List", new Intent(getApplicationContext(), MLMCompanyList.class), false, false);
        features.add(mlmCompanylist);
        MenuModel top_network_company = new MenuModel("Top Networker", new Intent(getApplicationContext(), TopNetworkCompany.class), false, false);
        features.add(top_network_company);
        MenuModel top_mlm_trainer = new MenuModel("Top MLM Trainer", new Intent(getApplicationContext(), TopMLMTrainer.class), false, false);
        features.add(top_mlm_trainer);
        MenuModel topLeaders = new MenuModel("Company Top Leaders", new Intent(getApplicationContext(), CompanyTopLeaders.class), false, false);
        features.add(topLeaders);
        MenuModel mlmTrainingSeminarUpdate = new MenuModel("MLM Training Seminar Update", new Intent(getApplicationContext(), MLMSeminalUpdate.class), false, false);
        features.add(mlmTrainingSeminarUpdate);
        MenuModel companyPlanDetails = new MenuModel("Company Plan Details", new Intent(getApplicationContext(), CompanyPlanDetail.class), false, false);
        features.add(companyPlanDetails);
        MenuModel currentGrowthCompany = new MenuModel("Current Growth Company", new Intent(getApplicationContext(), CurrentGrowthCompany.class), false, false);
        features.add(currentGrowthCompany);
        MenuModel fastCourierService = new MenuModel("Fast Courier Service", new Intent(getApplicationContext(), FastCourierService.class), false, false);

        features.add(fastCourierService);
        MenuModel mlm_product_service_provider = new MenuModel("MLM Product Service Provider", new Intent(getApplicationContext(), MlmProductServiceProvider.class), false, false);

        features.add(mlm_product_service_provider);
        MenuModel mlmProductManufacturing = new MenuModel("MLM Product Manufacturing", new Intent(getApplicationContext(), MlmProductManufacturing.class), false, false);

        features.add(mlmProductManufacturing);

        if (feature.hasChildren) {
            childList.put(feature, features);
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
        MenuModel referral = new MenuModel("Referral", new Intent(getApplicationContext(), Referral.class), false, true);
        headerList.add(referral);
        MenuModel walletMenu = new MenuModel(getString(R.string.wallet), new Intent(getApplicationContext(), WalletScreen.class), false, true);
        headerList.add(walletMenu);

        MenuModel help = new MenuModel("Help", new Intent(getApplicationContext() , Help.class), false, true);
        headerList.add(help);
        MenuModel logout = new MenuModel("Log Out", null, false, true);

        headerList.add(logout);


    }

    private void closeDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        switch (item.getItemId()) {
            case R.id.new_post_menu:
                Intent newPost = new Intent(getApplicationContext(), NewPost.class);
                updateUi(newPost);

                break;

            case R.id.notification:
                Intent not = new Intent(getApplicationContext(), Notification.class);
                updateUi(not);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(HomeMenu menu) {
        Log.d(TAG, "onItemClick: Home menu Item click");
        startActivity(menu.getIntent());

    }

    @Override
    public void onLikeClick(Post post) {
        Log.d(TAG, "onLikeClick: " + post.getId());

        JSONObject like = new JSONObject();
        try {
            like.put("postId", post.getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        api.postRequest(like, response -> {
//            Log.d(TAG, "onResponse: " + response);
            fetchPost(limit, skip);
        }, Server.POST_LIKE);

    }

    @Override
    public void onDislikeClick(Post post) {
        Log.d(TAG, "onDislikeClick: " + post.getIsLiked());
        JSONObject like = new JSONObject();
        try {
            like.put("postId", post.getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        api.postRequest(like, response -> {
            Log.d(TAG, "onDislikeClick: dislike");
            fetchPost(limit, skip);

        }, Server.POST_DISLIKE);
    }

    @Override
    public void onShareClick(Post post) {
        String msg = post.getData();
        String id = post.getId();

        JSONObject object1 = new JSONObject();
        try {
            object1.put("postId", post.getId());
            object1.put("type", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        api.postRequest(object1, response -> {

        }, Server.ACTION_POST);
        msg += "\nhttps://mlm.app/" + id;
        msg += "\nDownload app from https://url.com";
        setting.openShare(msg);

    }

    @Override
    public void onComment(Post post) {

        Intent intent = new Intent(getApplicationContext(), Comment.class);
        Constant.CURRENT_POST = post;
        startActivity(intent);
    }

    @Override
    public void onDelete(Post post) {
        JSONObject object = new JSONObject();
        try {

            object.put("id", post.getId());
        } catch (Exception e) {

        }
        api.postRequest(object, response -> {
            Log.d(TAG, "onResponse: " + response);
            fetchPost(limit, skip);
        }, Server.DELETE_POST);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        fetchPost(limit, skip);
        api.validateSession(res -> {
            Log.d(TAG, "onCreate: " + res);

            try {
                JSONObject response = new JSONObject(res);
                int status = response.getInt("status");
                String message = response.getString("message");
                if (status == 200) {
                    //login success
                    JSONObject data = response.getJSONObject("data");
                    String _id = data.getString("id");
                    String _name = data.getString("fullName");
                    String _email = data.getString("email");
                    String _phone = data.getString("phone");
                    String _token = data.getString("accessToken"); // token
                    String _profile = data.getString("imageStr");
                    sessionHandler.setLoggedInMobile(_phone);
                    sessionHandler.setLoggedInUser(_id);
                    sessionHandler.setLoggedToken(_token);
                    sessionHandler.setUserName(_name);
                    sessionHandler.setLoggedInEmail(_email);
//                    sessionHandler.setLogin(true);
                    String isVerified = data.getString("isVerified");
                    String subscriptionType = data.getString("subscriptionType");
                    String subscriptionExpiryDate = data.getString("subscriptionExpiryDate");
                    Constant.PURCHASED_SUBSCRIPTION_TYPE = subscriptionType;
                    Constant.PURCHASED_SUBSCRIPTION_EXPIRED_ON = subscriptionExpiryDate;
                    Constant.LOGEDTOKEN = _token;
                    sessionHandler.setProfileImage(_profile);


//                    User user = new User(_id ,_name , _email ,_phone, _token  );


                } else {


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchPost(limit, skip);
//
        api.validateSession(res -> {
            Log.d(TAG, "onCreate: " + res);

            try {
                JSONObject response = new JSONObject(res);
                int status = response.getInt("status");
                String message = response.getString("message");
                if (status == 200) {
                    //login success
                    JSONObject data = response.getJSONObject("data");
                    String _id = data.getString("id");
                    String _name = data.getString("fullName");
                    String _email = data.getString("email");
                    String _phone = data.getString("phone");
                    String _token = data.getString("accessToken"); // token
                    sessionHandler.setLoggedInMobile(_phone);
                    sessionHandler.setLoggedInUser(_id);
                    sessionHandler.setLoggedToken(_token);
                    sessionHandler.setUserName(_name);
                    sessionHandler.setLoggedInEmail(_email);
//                    sessionHandler.setLogin(true);
                    String isVerified = data.getString("isVerified");
                    String subscriptionType = data.getString("subscriptionType");
                    String subscriptionExpiryDate = data.getString("subscriptionExpiryDate");
                    Constant.PURCHASED_SUBSCRIPTION_TYPE = subscriptionType;
                    Constant.PURCHASED_SUBSCRIPTION_EXPIRED_ON = subscriptionExpiryDate;
                    String _profile = data.getString("imageStr");
                    sessionHandler.setProfileImage(_profile);


//                    User user = new User(_id ,_name , _email ,_phone, _token  );


                } else {


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }
}
