/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.model;

/**
 *  ONE_MONTH: 0,
 *   THREE_MONTH: 1,
 *   SIX_MONTH: 2,
 *   ONE_YEAR: 3,
 *   THREE_YEAR: 4,
 *   FIVE_YEAR: 5,
 *   TEN_YEAR: 6,
 *   LIFETIME: 7
 */
public class SubscriptionModel {
    String id, time, amount, faceValue;

    public SubscriptionModel(String id, String time, String amount, String faceValue) {
        this.id = id;
        this.time = time;
        this.amount = amount;
        this.faceValue = faceValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(String faceValue) {
        this.faceValue = faceValue;
    }
}
