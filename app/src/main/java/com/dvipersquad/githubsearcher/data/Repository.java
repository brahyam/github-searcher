package com.dvipersquad.githubsearcher.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "repositories")
public class Repository {

    @PrimaryKey
    @NonNull
    private String id;

    private String name;

    private String description;

    private int forkCount;

    @Embedded
    private User owner;

    public Repository(String id, String name, String description, int forkCount, User owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.forkCount = forkCount;
        this.owner = owner;
    }

    @NonNull
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getForkCount() {
        return forkCount;
    }

    public void setForkCount(int forkCount) {
        this.forkCount = forkCount;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
