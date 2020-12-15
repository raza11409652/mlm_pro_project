/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Response;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.project.mlmpro.R;
import com.project.mlmpro.model.Post;
import com.project.mlmpro.utils.Constant;
import com.project.mlmpro.utils.IntentSetting;
import com.project.mlmpro.utils.RequestApi;
import com.project.mlmpro.utils.Server;
import com.project.mlmpro.utils.TimeDiff;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class SinglePost extends AppCompatActivity {
    Toolbar toolbar;
    Post post = Constant.CURRENT_POST;
    String postId;
    RequestApi requestApi;
    TextView profile_name , time , post_data  , like_count,comment_count;
    CircleImageView profile_image ;
    ImageView post_image ;
    StyledPlayerView video_wrapper ;
    RelativeLayout share_wrapper , comment_wrapper ;
    IntentSetting setting ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        setTitle("");
        requestApi = new RequestApi(this);
        try {
            postId = post.getId();
            Log.d("TAG", "onCreate: " + postId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        profile_image = findViewById(R.id.profile_image);
        profile_name = findViewById(R.id.profile_name );
        time = findViewById(R.id.time);
        post_data  =findViewById(R.id.post_data) ;
        like_count = findViewById(R.id.like_count) ;
        comment_count = findViewById(R.id.comment_count );
        comment_wrapper =findViewById(R.id.comment_wrapper) ;
        share_wrapper = findViewById(R.id.share_wrapper) ;
        video_wrapper = findViewById(R.id.video_wrapper) ;
        post_image = findViewById(R.id.post_image);
        String url = Server.SINGLE_POST + postId;
        Log.d("TAG", "onCreate: " + url);
        setting = new IntentSetting(SinglePost.this);
        requestApi.getRequest(url, response -> {
            Log.d("TAG", "onCreate: " + response);
//            int status =
            try {
                JSONObject object = new JSONObject(response);
                int status = object.getInt("status");
                if (status == 200) {

                    JSONArray Array = object.getJSONArray("data");
                    JSONObject data = Array.getJSONObject(0);
                    String id = data.getString("id");
                    String senderID = data.getString("senderID");
                    String senderName = data.getString("senderName");
                    String senderImage = data.getString("senderImage");
                    String postText = data.getString("postText");
                    String postImage = data.getString("postImage");
                    String noOfLikes = data.getString("noOfLikes");
                    String likedByYou = data.getString("likedByYou");
                    String createdAt = data.getString("likedByYou");
                    if (postImage.equals("NA")){
                        //no image not vide
                    }else if(postImage.contains(".mp4")||postImage.contains("MLM_PRO_VIDEO")){
                        //vide
                        SimpleExoPlayer player = new SimpleExoPlayer.Builder(getApplicationContext()).build();
                        video_wrapper.setPlayer(player);
                        MediaItem item = MediaItem.fromUri(postImage);
                        player.addMediaItem(item);
                        video_wrapper.setVisibility(View.VISIBLE);
                    }else {
                        //image
                        Picasso.get().load(postImage)
                                .error(R.drawable.placeholder).error(R.drawable.placeholder)
                                .into(post_image);
                        post_image.setVisibility(View.VISIBLE);
                    }
                    if (postText.equals("NA")){
                        post_data.setVisibility(View.GONE);

                    }else{
                        post_data.setText(postText);
                        post_data.setVisibility(View.VISIBLE);
                    }
                    time.setText(TimeDiff.diff(createdAt));
                    profile_name.setText(senderName);
                    Picasso.get()
                            .load(senderImage)
                            .error(R.drawable.logo_circle)
                            .placeholder(R.drawable.placeholder)
                            .into(profile_image);


                    like_count.setText(noOfLikes + " Likes");

                    share_wrapper.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            JSONObject object1=new JSONObject();
                            try {
                                object1.put("postId" , postId);
                                object1.put("type" , "1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            requestApi.postRequest(object1, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                }
                            } , Server.ACTION_POST);
                            String msg = postText +"\n";
                            msg+= "by "+senderName ;
                            msg +="\nhttps://mlm.app/"+postId;
                            msg += "\nDownload app from https://url.com";
                            setting.openShare(msg);
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Content you are looking for , is not available", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        comment_wrapper.setOnClickListener(v->{
            Constant.CURRENT_POST = new Post(postId);
            Intent intent = new Intent(getApplicationContext()  , Comment.class);
            startActivity(intent);
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}