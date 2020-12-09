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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MlmTrainerAdapter extends RecyclerView.Adapter<MlmTrainerAdapter.ViewHolder> {
    ArrayList<FeaturePost> list;
    Context context;

    public MlmTrainerAdapter(ArrayList<FeaturePost> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_mlm_trainer, null, false);

        return new MlmTrainerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FeaturePost p
                = list.get(position);
        holder.companyName.setText(p.getCompanyName());
        holder.email.setText(p.getEmail());
        holder.whatsApp.setText("+91-" + p.getWhatsappContact());
        holder.mobile.setText("+91-" + p.getPhone());
        holder.webLink.setText(p.getWebsiteLike());
        holder.trainingInst.setText(p.getTrainingInstitue());
        Picasso.get().load(p.getPostImage()).placeholder(R.drawable.placeholder)
                .error(R.drawable.logo_circle)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView companyName, trainingInst, webLink, mobile, whatsApp, email;
        CircleImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.name);
            trainingInst = itemView.findViewById(R.id.training_inst);
            webLink = itemView.findViewById(R.id.website_link);
            mobile = itemView.findViewById(R.id.contact_number);
            whatsApp = itemView.findViewById(R.id.whatsapp_number);
            email = itemView.findViewById(R.id.email);
            imageView = itemView.findViewById(R.id.image) ;
        }
    }
}
