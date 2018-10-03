package com.dvipersquad.githubsearcher.data;

import android.arch.persistence.room.Ignore;

import com.dvipersquad.githubsearcher.data.source.remote.RepositoriesApiResponse;

import java.util.ArrayList;
import java.util.List;

public class Watchers {

    private int totalCount;

    @Ignore
    private List<User> watchers;

    public Watchers(int totalCount) {
        this.totalCount = totalCount;
    }

    public Watchers(int totalCount, List<User> watchers) {
        this.totalCount = totalCount;
        this.watchers = watchers;
    }

    public Watchers(RepositoriesApiResponse.ResponseWatchers responseWatchers, String repositoryId) {
        this.totalCount = responseWatchers.getTotalCount();
        this.watchers = new ArrayList<>();
        if (responseWatchers.getWatchers() != null) {
            for (RepositoriesApiResponse.UserNode userNode : responseWatchers.getWatchers()) {
                userNode.getNode().setRepositoryId(repositoryId);
                this.watchers.add(userNode.getNode());
            }
        }
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<User> getWatchers() {
        return watchers;
    }

    public void setWatchers(List<User> watchers) {
        this.watchers = watchers;
    }
}
