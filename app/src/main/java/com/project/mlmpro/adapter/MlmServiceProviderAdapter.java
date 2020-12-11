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

public class MlmServiceProviderAdapter extends RecyclerView.Adapter<MlmServiceProviderAdapter.Viewholder> {
    ArrayList<FeaturePost> list;
    Context context;
    IntentSetting setting ;

    public MlmServiceProviderAdapter(ArrayList<FeaturePost> list, Context context) {
        this.list = list;
        this.context = context;
        setting = new IntentSetting(context);
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
        holder.name.setText(StringHandler.captalize(post.getCompanyName()));
        holder.address.setText(StringHandler.captalize(post.getStreet1()) + " " + post.getStreet2());
        holder.phone.setText("+91-" + post.getPhone());
        holder.whtsapp.setText("+91-" + post.getWhatsappContact());
        holder.email.setText(post.getEmail());
        holder.type.setText(StringHandler.captalize(post.getProductType()));
        Picasso.get()
                .load(post.getPostImage())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);

        holder.whtsapp.setOnClickListener(new View.OnClickListener() {
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

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView name, address, phone, whtsapp, email, type;
        CircleImageView imageView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.contact_number);
            whtsapp = itemView.findViewById(R.id.whatsapp_number);
            type = itemView.findViewById(R.id.type);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
