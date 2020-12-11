/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mlmpro.R;
import com.project.mlmpro.model.FeaturePost;
import com.project.mlmpro.utils.IntentSetting;
import com.project.mlmpro.utils.StringHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CurrentGrowthAdapter extends RecyclerView.Adapter<CurrentGrowthAdapter.ViewHolder> {
    ArrayList<FeaturePost> list;
    Context context;
    IntentSetting setting ;

    public CurrentGrowthAdapter(ArrayList<FeaturePost> list, Context context) {
        this.list = list;
        this.context = context;
        setting = new IntentSetting(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_growth_company, null, false);
        return new CurrentGrowthAdapter.ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeaturePost post = list.get(position);
        holder.companyName.setText(StringHandler.captalize(post.getCompanyName()));
        holder.address.setText(post.getStreet1() + " " + post.getStreet2());
        holder.mobile.setText("+91-" + post.getPhone());
        holder.whatsapp.setText("+91-" + post.getWhatsappContact());
//        holder.s
        holder.website.setText(post.getWebsiteLike());
        String status = post.getStatus();
        switch (status) {
            case "0":
                holder.status.setText("Verified");
                holder.status.setBackground(context.getDrawable(R.drawable.verified));
                holder.status.setTextColor(context.getColor(R.color.green));

                break;
            case "1":
                holder.status.setText("Pending");
                holder.status.setBackground(context.getDrawable(R.drawable.pending));
                holder.status.setTextColor(context.getColor(R.color.alert_red_primary));
                break;
        }

        Picasso.get().load(post.getPostImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);

        holder.website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.openWeb(list.get(position).getWebsiteLike());
            }
        });
        holder.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setting.openWhatsappWithMobile(list.get(position).getWhatsappContact());
            }
        });
        holder.mobile.setOnClickListener(new View.OnClickListener() {
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
        TextView address, companyName, mobile, whatsapp, website, status;
        CircleImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address);
            companyName = itemView.findViewById(R.id.name);
            mobile = itemView.findViewById(R.id.contact_number);
            whatsapp = itemView.findViewById(R.id.whatsapp_number);
            website = itemView.findViewById(R.id.website_link);
            status = itemView.findViewById(R.id.status);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
