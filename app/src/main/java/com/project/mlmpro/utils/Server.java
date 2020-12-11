/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.utils;

public class Server {
    public static final String ROOT_SERVER = "http://ec2-3-135-185-63.us-east-2.compute.amazonaws.com:1111/";

    //End points

    public static final String LOGIN = ROOT_SERVER + "";
    public static final String REGISTER = ROOT_SERVER + "customer/signup";
    public static final String APPLY_INVITE = ROOT_SERVER + "customer/applyInviteCode";
    public static final String UPDATE_PROFILE = ROOT_SERVER + "customer/updateProfile";
    public static final String UPLOAD_PHOTO = ROOT_SERVER + "upload/photo"; //MULTIPART PHOTO
    public static final String TOKEN_LOGIN = ROOT_SERVER + "customer/accessTokenLogin";
    public static final String USER_LOGIN = ROOT_SERVER + "customer/login";
    public static final String SLIDER_IMAGE = ROOT_SERVER + "getSliderImage";
    public static final String NEW_POST = ROOT_SERVER + "customer/post";
    public static final String GET_POST = ROOT_SERVER + "customer/post";
    public static final String GET_WALLET = ROOT_SERVER + "customer/getWalletDetails";
    public static final String GET_REFERRAL= ROOT_SERVER + "customer/getReferrals";
    public static final String POST_LIKE= ROOT_SERVER + "customer/post/like";
    public static final String POST_DISLIKE= ROOT_SERVER + "customer/post/unlike";
    public static final String GET_FEATURE= ROOT_SERVER + "feature/post";
    public static final String GET_FEATURE_PENDING= ROOT_SERVER + "customer/feature/CGC/pending";
    public static final String CHECKOUT= ROOT_SERVER + "customer/subscription/checkout";
    public static final String PURCHASE= ROOT_SERVER + "customer/subscription/purchase";
    public static final String DEDUCT_WALLET= ROOT_SERVER + "customer/subscription/deductWalletAmount";
    public static final String FORGET_PASSWORD= ROOT_SERVER + "customer/forgotPassword";
    public static final String SET_NEW_PASSWORD= ROOT_SERVER + "customer/setNewPassword";

}

