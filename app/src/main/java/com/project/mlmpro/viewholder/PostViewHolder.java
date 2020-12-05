/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mlmpro.R;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public TextView postData, name, time, likeCount, dislikeLikeCount;
    public ImageView profileImage, postImage;
    public ImageButton like, comment, share;
    public RelativeLayout like_wrapper, dislike_wrapper, share_wrapper;


    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        postData = itemView.findViewById(R.id.post_data);
        name = itemView.findViewById(R.id.profile_name);
        time = itemView.findViewById(R.id.time);
        profileImage = itemView.findViewById(R.id.profile_image);
        postImage = itemView.findViewById(R.id.post_image);
        like = itemView.findViewById(R.id.likes);
        like_wrapper = itemView.findViewById(R.id.like_wrapper);
        dislike_wrapper = itemView.findViewById(R.id.dislike_wrapper);
        likeCount = itemView.findViewById(R.id.like_count);
        dislikeLikeCount = itemView.findViewById(R.id.dislike_like_Count);
        share_wrapper = itemView.findViewById(R.id.share_wrapper);

    }
}
