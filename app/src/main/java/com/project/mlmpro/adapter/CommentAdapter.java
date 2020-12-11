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
import com.project.mlmpro.model.CommentPost;
import com.project.mlmpro.utils.TimeDiff;

import java.util.ArrayList;

public class CommentAdapter  extends RecyclerView.Adapter<CommentAdapter.Viewholder> {
    ArrayList<CommentPost>list ;
    Context context ;

    public CommentAdapter(ArrayList<CommentPost> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_comment , parent , false);
        return new CommentAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        holder.user.setText(list.get(position).getUser());
        holder.comment.setText(list.get(position).getComment());
        holder.time.setText(TimeDiff.dateNow(list.get(position).getTime()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView comment , time , user ;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            comment  =itemView.findViewById(R.id.comment) ;
            user  =itemView.findViewById(R.id.user) ;
            time = itemView.findViewById(R.id.time);
        }
    }
}
