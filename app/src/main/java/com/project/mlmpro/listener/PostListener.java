/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.listener;

import com.project.mlmpro.model.Post;

public interface PostListener {
    void onLikeClick(Post post);
    void onDislikeClick(Post post);
    void onShareClick(Post post);


}
