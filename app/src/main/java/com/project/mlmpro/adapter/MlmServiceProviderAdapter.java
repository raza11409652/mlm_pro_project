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
import com.project.mlmpro.utils.StringHandler;

import java.util.ArrayList;

public class MlmServiceProviderAdapter extends RecyclerView.Adapter<MlmServiceProviderAdapter.Viewholder> {
    ArrayList<FeaturePost> list;
    Context context;

    public MlmServiceProviderAdapter(ArrayList<FeaturePost> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_service_provider, null, false);

        return new MlmServiceProviderAdapter.Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        FeaturePost post = list.get(position);
        holder.name.setText(post.getCompanyName());
        holder.address.setText(StringHandler.captalize(post.getStreet1()) + " " + post.getStreet2());
        holder.phone.setText("+91-" + post.getPhone());
        holder.whtsapp.setText("+91-" + post.getWhatsappContact());
        holder.email.setText(post.getEmail());
        holder.type.setText(post.getProductType());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView name, address, phone, whtsapp, email, type;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.contact_number);
            whtsapp = itemView.findViewById(R.id.whatsapp_number);
            type = itemView.findViewById(R.id.type);
        }
    }
}
