/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.model;

public class User {
    String id, name, email, phone, token, role, status, referralCode, imageUrl,
            whatsappContact, companyName, companyWebsite, roleInCompany,
            createdAt, updatedAt;

    public User(String id, String name, String email, String phone, String token, String role, String status, String referralCode, String imageUrl, String whatsappContact, String companyName, String companyWebsite, String roleInCompany, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.token = token;
        this.role = role;
        this.status = status;
        this.referralCode = referralCode;
        this.imageUrl = imageUrl;
        this.whatsappContact = whatsappContact;
        this.companyName = companyName;
        this.companyWebsite = companyWebsite;
        this.roleInCompany = roleInCompany;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User(String id, String name, String email, String phone, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWhatsappContact() {
        return whatsappContact;
    }

    public void setWhatsappContact(String whatsappContact) {
        this.whatsappContact = whatsappContact;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public String getRoleInCompany() {
        return roleInCompany;
    }

    public void setRoleInCompany(String roleInCompany) {
        this.roleInCompany = roleInCompany;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}