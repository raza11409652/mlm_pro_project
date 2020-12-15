/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.model;

public class Notify {
    String id , userId , secondUserId , secondUserName , secondUserImage ,
            referanceId, type ,createdAt ;

    public Notify(String id, String userId, String secondUserId, String secondUserName, String secondUserImage, String referanceId, String type, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.secondUserId = secondUserId;
        this.secondUserName = secondUserName;
        this.secondUserImage = secondUserImage;
        this.referanceId = referanceId;
        this.type = type;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSecondUserId() {
        return secondUserId;
    }

    public void setSecondUserId(String secondUserId) {
        this.secondUserId = secondUserId;
    }

    public String getSecondUserName() {
        return secondUserName;
    }

    public void setSecondUserName(String secondUserName) {
        this.secondUserName = secondUserName;
    }

    public String getSecondUserImage() {
        return secondUserImage;
    }

    public void setSecondUserImage(String secondUserImage) {
        this.secondUserImage = secondUserImage;
    }

    public String getReferanceId() {
        return referanceId;
    }

    public void setReferanceId(String referanceId) {
        this.referanceId = referanceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
