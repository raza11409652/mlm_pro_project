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


public class HomeMenuViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView menu;

    public HomeMenuViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image);
        menu = itemView.findViewById(R.id.menu);
    }
}
