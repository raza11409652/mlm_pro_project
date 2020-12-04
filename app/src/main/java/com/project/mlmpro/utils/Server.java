/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.utils;

public class Server {
    public static final String ROOT_SERVER = "http://ec2-3-137-211-102.us-east-2.compute.amazonaws.com:1111/";

    //End points

    public static final String LOGIN = ROOT_SERVER + "";
    public static final String REGISTER = ROOT_SERVER + "customer/signup";
    public static final String APPLY_INVITE = ROOT_SERVER + "customer/applyInviteCode";
    public static final String UPDATE_PROFILE = ROOT_SERVER + "customer/updateProfile";
    public static final String UPLOAD_PHOTO = ROOT_SERVER + "upload/photo"; //MULTIPART PHOTO
    public static final String TOKEN_LOGIN = ROOT_SERVER + "customer/accessTokenLogin";
    public static final String USER_LOGIN = ROOT_SERVER + "customer/login";
    public static final String SLIDER_IMAGE = ROOT_SERVER + "getSliderImage";

}

