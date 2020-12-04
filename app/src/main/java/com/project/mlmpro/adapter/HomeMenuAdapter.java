/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mlmpro.R;
import com.project.mlmpro.listener.HomeMenuListener;
import com.project.mlmpro.model.HomeMenu;
import com.project.mlmpro.viewholder.HomeMenuViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeMenuAdapter extends RecyclerView.Adapter<HomeMenuViewHolder> {
    ArrayList<HomeMenu> menus;
    Context context;
    HomeMenuListener listener;

    public HomeMenuAdapter(ArrayList<HomeMenu> menus, Context context, HomeMenuListener listener) {
        this.menus = menus;
        this.context = context;
        this.listener = listener;
    }

//    public HomeMenuAdapter(ArrayList<HomeMenu> menus, Context context) {
//        this.menus = menus;
//        this.context = context;
//    }

    @NonNull
    @Override
    public HomeMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_home_menu, null);

        return new HomeMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeMenuViewHolder holder, int position) {
        Picasso.get().load(menus.get(position).getDrawable()).into(holder.imageView);
        holder.menu.setText(menus.get(position).getName());
        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(menus.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return menus.size();
    }
}
