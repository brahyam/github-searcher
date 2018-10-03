package com.dvipersquad.githubsearcher.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private String userId;

    @SerializedName("name")
    private String userName;

    private String avatarUrl;

    @ForeignKey(entity = Repository.class, parentColumns = "id", childColumns = "userId")
    private String repositoryId;

    public User(@NonNull String userId, String userName, String avatarUrl, String repositoryId) {
        this.userId = userId;
        this.userName = userName;
        this.avatarUrl = avatarUrl;
        this.repositoryId = repositoryId;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
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

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }
}
