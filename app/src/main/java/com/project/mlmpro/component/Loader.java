/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.project.mlmpro.R;


public class Loader {
    Context context;
    AlertDialog alertDialog;

    public Loader(Context context) {
        this.context = context;
    }

    public void show(String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.customLoader);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.loader_custom, null);
        TextView errorMsg = view.findViewById(R.id.text);
        errorMsg.setText(msg);
        alert.setView(view);
        alert.setCancelable(false);

        alertDialog = alert.create();
        alertDialog.show();

    }

    public void dismiss() {
        if (alertDialog.isShowing()) alertDialog.dismiss();
    }
}
