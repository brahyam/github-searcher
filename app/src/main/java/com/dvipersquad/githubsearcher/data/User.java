package com.dvipersquad.githubsearcher.data;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    private String userId;

    @SerializedName("name")
    private String userName;

    private String avatarUrl;

    public User(String userId, String userName, String avatarUrl) {
        this.userId = userId;
        this.userName = userName;
        this.avatarUrl = avatarUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
