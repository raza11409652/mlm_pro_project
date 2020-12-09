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
import com.project.mlmpro.model.Transaction;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    ArrayList<Transaction> list;
    Context context;

    public TransactionAdapter(ArrayList<Transaction> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_transactions, null);
        return new TransactionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Transaction transaction = list.get(position);
        String type = transaction.getType();
        String msg = null;
        if (type.equals("INVITE CODE AMOUNT")) {
            msg = "You just received " + transaction.getAmount() + " from invite code of " + transaction.getSecondUserName();

        } else if (type.equals("REFERRAL AMOUNT")) {
            msg = "You  received Rs." + transaction.getAmount() + " from Referral code of " + transaction.getSecondUserName();

        } else if (type.equals("SUBSCRIPTION PURCHASED")) {
            msg = "You have used Rs. " + (-Double.valueOf(transaction.getAmount())) + " for Subscription Purchased";
        }
        holder.amount.setText(transaction.getAmount());
        holder.ref.setText(msg);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ref, amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            ref
                    = itemView.findViewById(R.id.ref);

        }
    }


}
