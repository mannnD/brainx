package com.brainx.core.domain;

import lombok.Data;

import java.util.List;
@Data
public class PostWithMedia {
    private String title;
    private String description;
    private byte[] mediaContents;

    public PostWithMedia(String title, String description, byte[] mediaContents) {
        this.title = title;
        this.description = description;
        this.mediaContents = mediaContents;
    }

    // Getters and Setters
}