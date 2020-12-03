/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.project.mlmpro.R;
import com.project.mlmpro.model.ImageGallery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<ImageGallery> galleries;

    public SliderAdapter(Context context, ArrayList<ImageGallery> galleries) {
        this.context = context;
        this.galleries = galleries;
    }

    @Override
    public int getCount() {
        return galleries.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_image_gallery, null);
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        ImageView imageView = view.findViewById(R.id.image);
        Picasso.get().load(galleries.get(position).getImageUrl()).into(imageView);
        return view ;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
