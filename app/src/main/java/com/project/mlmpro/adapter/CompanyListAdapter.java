/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mlmpro.R;
import com.project.mlmpro.model.FeaturePost;
import com.project.mlmpro.utils.StringHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.ViewHolder> {
    ArrayList<FeaturePost> list;
    Context context;

    public CompanyListAdapter(ArrayList<FeaturePost> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_comp_list, null, false);

        return new CompanyListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.whatsApp.setText("+91-" + list.get(position).getWhatsappContact());
        holder.name.setText(StringHandler.captalize(list.get(position).getCompanyName()));
        holder.address.setText(list.get(position).getStreet1() + ", " + list.get(position).getStreet2());
        holder.mobile.setText("+91-" + list.get(position).getPhone());
        holder.webLink.setText(list.get(position).getWebsiteLike());
        holder.date.setText(list.get(position).getStartingDate());
        holder.country.setText(list.get(position).getCountry());
        Log.d("TAG", "onBindViewHolder: " + list.get(position).getPostImage());
        Picasso.get().load(list.get(position).getPostImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.logo_circle)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView name, address, whatsApp, mobile, webLink, date, country;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            whatsApp = itemView.findViewById(R.id.whatsapp_number);
            mobile = itemView.findViewById(R.id.contact_number);
            webLink = itemView.findViewById(R.id.web_link);
            date = itemView.findViewById(R.id.date);
            country = itemView.findViewById(R.id.country);
            imageView  =itemView.findViewById(R.id.image);
        }
    }
}
