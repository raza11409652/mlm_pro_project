/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mlmpro.R;
import com.project.mlmpro.model.FeaturePost;
import com.project.mlmpro.utils.IntentSetting;
import com.project.mlmpro.utils.StringHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FastCourierServiceAdapter extends RecyclerView.Adapter<FastCourierServiceAdapter.ViewHolder> {
    ArrayList<FeaturePost> list;
    Context context;
    IntentSetting setting;

    public FastCourierServiceAdapter(ArrayList<FeaturePost> list, Context context) {
        this.list = list;
        this.context = context;
        setting = new IntentSetting(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_fast_courier, null);
        return new FastCourierServiceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.nam
        String[] types = {"State level", "District level", "International level", "Rural level"};

        FeaturePost post = list.get(position);
        holder.name.setText(StringHandler.captalize(post.getCompanyName()));

        holder.phone.setText("+91-" + post.getPhone());
        holder.whatsapp.setText("+91-" + post.getWhatsappContact());
        holder.email.setText(post.getEmail());
        holder.address.setText(StringHandler.captalize(post.getStreet1()) + ", " + post.getStreet2());
        String type = post.getProductType();
        switch (type) {
            case "1":
                holder.type.setText("State level");
                break;
            case "2":
                holder.type.setText("District level");
                break;
            case "3":
                holder.type.setText("International level");
                break;
            case "4":
                holder.type.setText("Rural level");
                break;
        }

        Picasso.get().load(post.getPostImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);
        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.openWhatsappWithMobile(list.get(position).getWhatsappContact());
            }
        });
        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.call(list.get(position).getPhone());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, whatsapp, phone, email, type;
        CircleImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            email = itemView.findViewById(R.id.email);
            whatsapp = itemView.findViewById(R.id.whatsapp_number);
            phone = itemView.findViewById(R.id.contact_number);
            type = itemView.findViewById(R.id.type);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
