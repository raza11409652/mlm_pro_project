/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mlmpro.R;
import com.project.mlmpro.listener.SubscriptionListner;
import com.project.mlmpro.model.SubscriptionModel;

import java.util.ArrayList;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ViewHolder> {
    ArrayList<SubscriptionModel> models;
    Context context;
    SubscriptionListner listner;

    public SubscriptionAdapter(ArrayList<SubscriptionModel> models, Context context, SubscriptionListner listner) {
        this.models = models;
        this.context = context;
        this.listner = listner;
    }

//    public SubscriptionAdapter(ArrayList<SubscriptionModel> models, Context context) {
//        this.models = models;
//        this.context = context;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_subscription, parent, false);
        return new SubscriptionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubscriptionModel subscriptionModel = models.get(position);

        holder.subscribe.setOnClickListener(v -> listner.onClick(subscriptionModel));
        holder.time.setText(subscriptionModel.getTime());
        holder.amount.setText(subscriptionModel.getAmount());

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time, amount;
        Button subscribe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subscribe = itemView.findViewById(R.id.subscribe);
            amount = itemView.findViewById(R.id.amount);
            time = itemView.findViewById(R.id.time);
        }
    }
}
