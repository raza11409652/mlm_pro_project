/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.adapter;

import android.content.Context;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.mlmpro.R;
import com.project.mlmpro.listener.PostListener;
import com.project.mlmpro.model.Post;
import com.project.mlmpro.utils.SessionHandler;
import com.project.mlmpro.utils.TimeDiff;
import com.project.mlmpro.viewholder.PostViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    ArrayList<Post> list;
    Context context;
    PostListener listener;
    SessionHandler sessionHandler;
    FirebaseDatabase database ;
    DatabaseReference databaseReference ;

    public PostAdapter(ArrayList<Post> list, Context context, PostListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
        sessionHandler = new SessionHandler(context);
        database = FirebaseDatabase.getInstance() ;
        databaseReference = database.getReference().child("comment");

    }
    //
//    public PostAdapter(ArrayList<Post> list, Context context) {
//        this.list = list;
//        this.context = context;
//    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_post, null);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {


        holder.commentCount.setText("0 Comments");
        Post post = list.get(position);
        DatabaseReference commentRef = databaseReference.child(post.getId()) ;
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    long commentCount =   snapshot.getChildrenCount() ;
//                    Log.d("TAG", "onDataChange: " + commentCount);
                    holder.commentCount.setText(String.valueOf(commentCount)+"Comments");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String postImage = post.getImage();
        String currentUserID = sessionHandler.getLoggedInUser();
        if (currentUserID.equals(post.getSender())) {
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.delete.setVisibility(View.GONE);
        }

        holder.delete.setOnClickListener(v->{
            listener.onDelete(post);
        });
        if (post.getIsLiked().equals("1")) {
            holder.like_wrapper.setVisibility(View.GONE);
            holder.dislike_wrapper.setVisibility(View.VISIBLE);
        } else {
            holder.like_wrapper.setVisibility(View.VISIBLE);
            holder.dislike_wrapper.setVisibility(View.GONE);

        }
        if (list.get(position).getData().equals("NA")) {
            holder.postData.setVisibility(View.GONE);
        } else {
            holder.postData.setVisibility(View.VISIBLE);
        }
//        holder.commentCount.setText();

        if (postImage.contains("MLM_PRO_VIDEO")||postImage.contains(".mp4")||postImage.contains("videos")) {
            //This is vide
            try {
                SimpleExoPlayer player = new SimpleExoPlayer.Builder(context).build();
                holder.postImage.setVisibility(View.GONE);
                holder.videoView.setVisibility(View.VISIBLE);
                holder.videoView.setPlayer(player);
//                holder.videoView.setCustomErrorMessage("Video Not Found");
//                Log.d("TAG", "onBindViewHolder: " + postImage);
                MediaItem item = MediaItem.fromUri(postImage);
                player.addMediaItem(item);
                player.addListener(new Player.EventListener() {
                    @Override
                    public void onPlaybackStateChanged(int state) {
                        if (state== ExoPlayer.STATE_ENDED){

                        }
                    }
                });
//                holder.videoView.on

//                player.
//                holder.video_wrapper.setVisibility(View.VISIBLE);
//                holder.video_wrapper.setVideoPath(postImage);
//                holder.video_wrapper.start();
//                holder.video_wrapper.setOnCompletionListener(mp -> {
//                    holder.video_wrapper.start();
//                });
//                holder.video_wrapper.
            } catch (Exception e) {
                Log.d("TAG", "onBindViewHolder: " + e.getLocalizedMessage());
            }
        } else {
            if (postImage == null || postImage.equals("NA")) {
                holder.postImage.setVisibility(View.GONE);
            } else {
                holder.postImage.setVisibility(View.VISIBLE);
            }
            //this isimage
//            holder.postImage.setVisibility(View.VISIBLE);
            Picasso.get().load(list.get(position).getImage())
                    .placeholder(R.drawable.placeholder).error(R.drawable.placeholder)
                    .into(holder.postImage);
        }

        holder.postData.setText(post.getData());
//        long sec = TimeDiff.diff(post.getCreatedOn() );
//        int minutes = (int) (sec/60);
//        int hour = minutes/60 ;
//        String time = null ;
//        if (hour>1){
//            time = hour + "hour ago";
//        }
        holder.time.setText(TimeDiff.diff(post.getCreatedOn()));
        holder.name.setText(post.getSenderName());
        holder.likeCount.setText(post.getLikesCount() + " Likes");
        holder.dislikeLikeCount.setText(post.getLikesCount() + " Likes");
        Linkify.addLinks(holder.postData , Linkify.WEB_URLS);

        holder.like_wrapper.setOnClickListener(v -> {
            listener.onLikeClick(post);
            holder.like_wrapper.setVisibility(View.GONE);
            holder.dislike_wrapper.setVisibility(View.VISIBLE);
        });
        holder.dislike_wrapper.setOnClickListener(v -> {
            listener.onDislikeClick(post);
            holder.like_wrapper.setVisibility(View.VISIBLE);
            holder.dislike_wrapper.setVisibility(View.GONE);
        });
        holder.share_wrapper.setOnClickListener(v -> {
            listener.onShareClick(post);

        });
        Picasso.get().load(list.get(position).getSenderImage())
                .error(R.drawable.logo)
                .placeholder(R.drawable.logo_circle)
                .into(holder.profileImage);
        holder.commentWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onComment(post);
            }
        });


    }

    @Override
    public int getItemCount() {
//        Log.d("TAG :: ", "getItemCount: " + list.size());
        return list.size();
    }
}
