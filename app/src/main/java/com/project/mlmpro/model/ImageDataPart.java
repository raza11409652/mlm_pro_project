/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.model;

public class ImageDataPart {
    private String fileName;
    private byte[] content;
    private String type;

    public ImageDataPart(String fileName, byte[] content) {
        this.fileName = fileName;
        this.content = content;
    }

    public ImageDataPart(String fileName, byte[] content, String type) {
        this.fileName = fileName;
        this.content = content;
        this.type = type;
    }

    public ImageDataPart() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
