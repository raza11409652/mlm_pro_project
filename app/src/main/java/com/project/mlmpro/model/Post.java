/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.model;

public class Post {
    String id , sender , senderName , senderImage , data , image , likesCount  ,
            sendBirdGroupId  , createdOn , updatedOn ;
    String isLiked ;

    public Post(String id, String sender, String senderName, String senderImage, String data, String image, String likesCount, String sendBirdGroupId, String createdOn, String updatedOn, String isLiked) {
        this.id = id;
        this.sender = sender;
        this.senderName = senderName;
        this.senderImage = senderImage;
        this.data = data;
        this.image = image;
        this.likesCount = likesCount;
        this.sendBirdGroupId = sendBirdGroupId;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.isLiked = isLiked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(String likesCount) {
        this.likesCount = likesCount;
    }

    public String getSendBirdGroupId() {
        return sendBirdGroupId;
    }

    public void setSendBirdGroupId(String sendBirdGroupId) {
        this.sendBirdGroupId = sendBirdGroupId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }
}
