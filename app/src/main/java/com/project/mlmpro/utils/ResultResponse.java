/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */



        package com.project.mlmpro.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class ResultResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private ResultData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }

}