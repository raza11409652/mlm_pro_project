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

import java.util.ArrayList;

public class TopNetworkAdapter extends RecyclerView.Adapter<TopNetworkAdapter.ViewHolder> {
    ArrayList<FeaturePost> list;
    Context context;

    public TopNetworkAdapter(ArrayList<FeaturePost> list, Context context) {
        this.list = list;
        this.context = context;
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

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, companyName, address, whatsapp, mobile, email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            companyName = itemView.findViewById(R.id.company_name);
            address = itemView.findViewById(R.id.address);
            whatsapp = itemView.findViewById(R.id.whatsapp_number);
            mobile = itemView.findViewById(R.id.contact_number);
            email = itemView.findViewById(R.id.email);
        }
    }
}
