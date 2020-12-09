/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mlmpro.R;
import com.project.mlmpro.listener.PostListener;
import com.project.mlmpro.model.Post;
import com.project.mlmpro.utils.TimeDiff;
import com.project.mlmpro.viewholder.PostViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    ArrayList<Post> list;
    Context context;
    PostListener listener;

    public PostAdapter(ArrayList<Post> list, Context context, PostListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
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
        Post post = list.get(position);
        String postImage = post.getImage();
        if (postImage == null || postImage.equals("NA")) {
            holder.postImage.setVisibility(View.GONE);
        } else {
            holder.postImage.setVisibility(View.VISIBLE);
        }

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

        Picasso.get().load(list.get(position).getImage())
                .placeholder(R.drawable.placeholder).error(R.drawable.placeholder)
                .into(holder.postImage);
        holder.postData.setText(post.getData());
        holder.time.setText(TimeDiff.diff(post.getCreatedOn()));
        holder.name.setText(post.getSenderName());
        holder.likeCount.setText(post.getLikesCount() + " Likes");
        holder.dislikeLikeCount.setText(post.getLikesCount() + " Likes");

        holder.like_wrapper.setOnClickListener(v -> {
            listener.onLikeClick(post);
        });
        holder.dislike_wrapper.setOnClickListener(v -> {
            listener.onDislikeClick(post);
            holder.like_wrapper.setVisibility(View.VISIBLE);
            holder.dislike_wrapper.setVisibility(View.GONE);
        });
        holder.share_wrapper.setOnClickListener(v -> {
            listener.onShareClick(post);
            holder.like_wrapper.setVisibility(View.GONE);
            holder.dislike_wrapper.setVisibility(View.VISIBLE);
        });
        Picasso.get().load(list.get(position).getSenderImage())
                .error(R.drawable.logo)
                .placeholder(R.drawable.logo_circle)
                .into(holder.profileImage);


    }

    @Override
    public int getItemCount() {
        Log.d("TAG :: ", "getItemCount: " + list.size());
        return list.size();
    }
}
