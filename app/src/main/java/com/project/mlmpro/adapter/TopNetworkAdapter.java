/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mlmpro.R;
import com.project.mlmpro.model.FeaturePost;
import com.project.mlmpro.utils.IntentSetting;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopNetworkAdapter extends RecyclerView.Adapter<TopNetworkAdapter.ViewHolder> {
    ArrayList<FeaturePost> list;
    Context context;

    IntentSetting setting ;
    public TopNetworkAdapter(ArrayList<FeaturePost> list, Context context) {
        this.list = list;
        this.context = context;
        setting = new IntentSetting(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_top_network_company, null, false);

        return new TopNetworkAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.email.setText(list.get(position).getEmail());
        holder.name.setText(list.get(position).getName());
        holder.companyName.setText(list.get(position).getCompanyName());
        holder.mobile.setText(list.get(position).getPhone());
        holder.whatsapp.setText(list.get(position).getWhatsappContact());
        holder.address.setText(list.get(position).getState());
        Picasso.get().load(list.get(position).getPostImage()).placeholder(R.drawable.placeholder)
                .error(R.drawable.logo_circle)
                .into(holder.imageView);

        holder.mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.call(list.get(position).getPhone());
            }
        });
        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.openWhatsappWithMobile(list.get(position).getWhatsappContact());
            }
        });
//        holder.email.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setting.openShare();
//            }
//        });
//
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, companyName, address, whatsapp, mobile, email;
        CircleImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            companyName = itemView.findViewById(R.id.company_name);
            address = itemView.findViewById(R.id.address);
            whatsapp = itemView.findViewById(R.id.whatsapp_number);
            mobile = itemView.findViewById(R.id.contact_number);
            email = itemView.findViewById(R.id.email);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
