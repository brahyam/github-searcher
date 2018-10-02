package com.dvipersquad.githubsearcher.data;

import java.util.List;

class Watchers {

    private int totalCount;

    private List<User> watchers;

    public Watchers(int totalCount, List<User> watchers) {
        this.totalCount = totalCount;
        this.watchers = watchers;
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
