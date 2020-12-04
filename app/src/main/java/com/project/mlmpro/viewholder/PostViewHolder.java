/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mlmpro.R;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public TextView postData, name, time;
    public ImageView profileImage, postImage;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        postData = itemView.findViewById(R.id.post_data);
        name = itemView.findViewById(R.id.profile_name);
        time = itemView.findViewById(R.id.time);
        profileImage = itemView.findViewById(R.id.profile_image);
        postImage = itemView.findViewById(R.id.post_image);

    }
}
