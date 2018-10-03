package com.dvipersquad.githubsearcher.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.dvipersquad.githubsearcher.data.source.remote.RepositoriesApiResponse;

@Entity(tableName = "repositories")
public class Repository {

    @PrimaryKey
    @NonNull
    private String id;

    private String name;

    private String description;

    private int forkCount;

    @Embedded
    private Watchers watchers;

    @Embedded
    private User owner;

    public Repository(@NonNull String id, String name, String description, int forkCount, Watchers watchers, User owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.forkCount = forkCount;
        this.watchers = watchers;
        this.owner = owner;
    }

    public Repository(RepositoriesApiResponse.ResponseRepository responseRepository) {
        this.id = responseRepository.getId();
        this.name = responseRepository.getName();
        this.description = responseRepository.getDescription();
        this.forkCount = responseRepository.getForkCount();
        this.watchers = new Watchers(responseRepository.getWatchers(), this.id);
        this.owner = responseRepository.getOwner();
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
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

    public Watchers getWatchers() {
        return watchers;
    }

    public void setWatchers(Watchers watchers) {
        this.watchers = watchers;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
