/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.project.mlmpro.R;
import com.project.mlmpro.adapter.NotifyAdapter;
import com.project.mlmpro.listener.NotifyListener;
import com.project.mlmpro.model.CommentPost;
import com.project.mlmpro.model.Notify;
import com.project.mlmpro.model.Post;
import com.project.mlmpro.utils.Constant;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Notification extends AppCompatActivity implements NotifyListener {

    Toolbar toolbar ;
    ShimmerFrameLayout shimmerFrameLayout  ;
    String limit= String.valueOf(20) , skip = String.valueOf(0);
    RequestApi api ;
    LinearLayout noNotification ;
    RecyclerView list ;
    ArrayList<Notify>notifies = new ArrayList<>( );
    LinearLayoutManager linearLayoutManager ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);

        setTitle(getString(R.string.notification));
        shimmerFrameLayout = findViewById(R.id.loader) ;
        shimmerFrameLayout.startShimmer();
        api = new RequestApi(this) ;
        linearLayoutManager = new LinearLayoutManager(this);
        list = findViewById(R.id.list);
        list.setLayoutManager(linearLayoutManager);
        noNotification = findViewById(R.id.no_notification);
        getNotification(limit , skip);

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastItem = linearLayoutManager.findLastVisibleItemPosition() ;
                if (lastItem==notifies.size()-1){
                    //reached last element
                    int skipInt = Integer.parseInt(skip);
                    skipInt =skipInt +notifies.size() ;
                    skip = String.valueOf(skipInt);
                    getNotification(limit , skip);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//
            }
        });


    }

    private void getNotification(String limit, String skip) {
        String url = Server.NOTIFICATION + "?limit="+limit+"&skip="+skip;
        Log.d("TAG", "getNotification: " + url);
        api.getRequest(url, response -> {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            try {
                JSONObject object = new JSONObject(response);
                int status = object.getInt("status" ) ;
                if (status==200){
                    JSONArray records =object.getJSONArray("data");
                    list.setVisibility(View.VISIBLE);
                    ///valid
                    if (skip.equals("0")){
                        notifies = new ArrayList<>();
                        if (records.length()<1){
                            noNotification.setVisibility(View.VISIBLE);
                            list.setVisibility(View.GONE);
                        }
                    }


                    for (int i=0;i<records.length();i++){
                        JSONObject single  =records.getJSONObject(i);
//                        Log.d("TAG", "getNotification: " + single);
                        String id = single.getString("id") ;
                        String userId=single.getString("userId") ;
                        String secondUserId = single.getString("secondUserId") ;
                        String secondUserName = single.getString("secondUserName") ;
                        String secondUserImage = single.getString("secondUserImage");
                        String referanceId = single.getString("referanceId");
                        String type = single.getString("type");

                        String createdAt = single.getString("createdAt");
                        Notify notify = new Notify(id , userId , secondUserId , secondUserName ,secondUserImage , referanceId , type , createdAt);
                        notifies.add(notify);
                    }
                    NotifyAdapter adapter =new NotifyAdapter(notifies , getApplicationContext()  ,this);

                    adapter.notifyDataSetChanged();
                    list.setAdapter(adapter);


                }else{

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("TAG", "getNotification: " + response);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() ==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * LIKE: 0,
     *      * SHARE: 1,
     *      * COMMENT: 2,
     *      * SUBSCRIPTION_PURCHASE: 3,
     *      * SUBSCRIPTION_RENEWAL: 4,
     *      * SUBSCRIPTION_EXPIRRY: 5
     * @param v
     */
    @Override
    public void onItemClick(Notify v) {
        String type = v.getType() ;
        Intent intent =null;
        switch (type){
            //0 post like
            case "0":
                Constant.CURRENT_POST = new Post(v.getReferanceId());
               intent =new Intent(getApplicationContext() ,SinglePost.class );
                break;
            //1 Post comments
            case "1":
                intent =new Intent(getApplicationContext() ,SinglePost.class );
                break;
            case "2":
                Constant.CURRENT_POST = new Post(v.getReferanceId());
                intent = new Intent(getApplicationContext()  , Comment.class);
            default:
                break;
        }
        if (intent!=null){
            startActivity(intent);
        }

    }
}