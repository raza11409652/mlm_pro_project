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

public class CompanyTopLeaderAdapter extends RecyclerView.Adapter<CompanyTopLeaderAdapter.ViewHolder> {
    ArrayList<FeaturePost> list;
    Context context;
IntentSetting setting ;
    public CompanyTopLeaderAdapter(ArrayList<FeaturePost> list, Context context) {
        this.list = list;
        this.context = context;
        setting  =new IntentSetting(context) ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_top_leader, null, false);
        return new CompanyTopLeaderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        View view =
        FeaturePost post = list.get(position);

        holder.country.setText(post.getCountry());
        holder.name.setText(StringHandler.captalize(post.getName()));
        holder.time.setText(post.getTime());
        holder.whatsApp.setText("+91-" + post.getWhatsappContact());
        holder.mobile.setText("+91-" + post.getPhone());
        holder.email.setText(post.getEmail());
        holder.rank.setText(StringHandler.captalize(post.getRank()));
        Picasso.get().load(post.getPostImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.logo_circle)
                .into(holder.imageView);

        holder.mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.call(list.get(position).getPhone());
            }
        });
        holder.whatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.openWhatsappWithMobile(list.get(position).getWhatsappContact());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, rank, whatsApp, mobile, email, time, country;
        CircleImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            rank = itemView.findViewById(R.id.rank);
            whatsApp = itemView.findViewById(R.id.whatsapp_number);
            mobile = itemView.findViewById(R.id.contact_number);
            email = itemView.findViewById(R.id.email);
            time = itemView.findViewById(R.id.time);
            country = itemView.findViewById(R.id.country);
            imageView = itemView.findViewById(R.id.image);


        }
    }
}
