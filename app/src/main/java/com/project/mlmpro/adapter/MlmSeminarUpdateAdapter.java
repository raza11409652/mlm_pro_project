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

public class MlmSeminarUpdateAdapter extends RecyclerView.Adapter<MlmSeminarUpdateAdapter.Viewholder> {

    ArrayList<FeaturePost> list;
    Context context;

    public MlmSeminarUpdateAdapter(ArrayList<FeaturePost> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_seminar_update, null, false);
        return new MlmSeminarUpdateAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        FeaturePost post = list.get(position);
        holder.companyName.setText(StringHandler.captalize(post.getCompanyName()));
        holder.address.setText(StringHandler.captalize(post.getStreet1()) + " " + post.getStreet2());
        holder.whatsapp.setText("+91-" + post.getWhatsappContact());
        holder.mobile.setText("+91-" + post.getPhone());
        holder.time.setText(post.getTime());
        holder.amount.setText("Rs. Amount");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView companyName, address, mobile, whatsapp, time, amount;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            whatsapp = itemView.findViewById(R.id.whatsapp_number);
            mobile = itemView.findViewById(R.id.contact_number);
            time = itemView.findViewById(R.id.time);
            amount = itemView.findViewById(R.id.charges);
        }
    }
}
