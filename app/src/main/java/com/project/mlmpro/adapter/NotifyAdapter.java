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
import com.project.mlmpro.listener.NotifyListener;
import com.project.mlmpro.model.Notify;
import com.project.mlmpro.utils.StringHandler;
import com.project.mlmpro.utils.TimeDiff;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolder> {
    ArrayList<Notify> list;
    Context context;
    NotifyListener notifyListener;

    public NotifyAdapter(ArrayList<Notify> list, Context context, NotifyListener notifyListener) {
        this.list = list;
        this.context = context;
        this.notifyListener = notifyListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_notification, parent, false);
        return new NotifyAdapter.ViewHolder(view);
    }

    /**
     * LIKE: 0,
     * SHARE: 1,
     * COMMENT: 2,
     * SUBSCRIPTION_PURCHASE: 3,
     * SUBSCRIPTION_RENEWAL: 4,
     * SUBSCRIPTION_EXPIRRY: 5
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notify single = list.get(position);
        String types = single.getType();
//        single.
        String testMsg = null;
        switch (types) {
            case "0":
                testMsg =StringHandler.captalize( single.getSecondUserName()) + " likes your post ";
                break;
            case "1":
                testMsg = StringHandler.captalize(single.getSecondUserName()) + " Shared on your post ";

                break;
            case "2":
                testMsg = StringHandler.captalize(single.getSecondUserName()) + " Commented on your post";
                break;

            case "3":
                testMsg = "You have purchased subscription";
                break;
            case "4":
                testMsg = "Your subscription has been renewed";
                break;
            case "5":
                testMsg = "Your subscription is going to expired";
                break;

            default:
                testMsg = "Notification";
                break;
        }
        Picasso.get().load(single.getSecondUserImage()).
                placeholder(R.drawable.logo_circle)
                .error(R.drawable.placeholder).into(holder.profile);
        holder.msg.setText(testMsg);
        holder.date.setText(TimeDiff.convertMongoDate(single.getCreatedAt()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyListener.onItemClick(single);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile;
        TextView date, msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile_image);
            date = itemView.findViewById(R.id.date);
            msg = itemView.findViewById(R.id.msg);
        }
    }
}
