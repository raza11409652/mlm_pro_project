/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.mlmpro.R;
import com.project.mlmpro.listener.PlanListListner;
import com.project.mlmpro.model.FeaturePost;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlansAdapter  extends RecyclerView.Adapter<PlansAdapter.VieHolder> {
    ArrayList<FeaturePost>list ;
    Context context ;
    PlanListListner listListner ;

    public PlansAdapter(ArrayList<FeaturePost> list, Context context, PlanListListner listListner) {
        this.list = list;
        this.context = context;
        this.listListner = listListner;
    }

    public PlansAdapter(ArrayList<FeaturePost> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public VieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_plan , parent , false);
        return new PlansAdapter.VieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VieHolder holder, int position) {
//        holder.imageProfile.

        Picasso.get()
                .load(list.get(position).getPostImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.imageProfile);

        holder.open.setOnClickListener(v -> listListner.onclick(
                list.get(position).getPlanFile()
        ));
        holder.download.setOnClickListener(v -> listListner.onclick(
                list.get(position).getCompanyName()
        ));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VieHolder  extends RecyclerView.ViewHolder{
        CircleImageView imageProfile  ;
        Button open , download ;
        public VieHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.image_profile);
            open = itemView.findViewById(R.id.open) ;
            download  =itemView.findViewById(R.id.download);

        }
    }
}
