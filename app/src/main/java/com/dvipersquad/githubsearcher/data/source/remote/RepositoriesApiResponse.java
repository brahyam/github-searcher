package com.dvipersquad.githubsearcher.data.source.remote;

import android.arch.persistence.room.Ignore;

import com.dvipersquad.githubsearcher.data.User;

import java.util.List;

public class RepositoriesApiResponse {

    private Data data;

    public RepositoriesApiResponse(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private Search search;

        public Data(Search search) {
            this.search = search;
        }

        public Search getSearch() {
            return search;
        }

        public void setSearch(Search search) {
            this.search = search;
        }
    }

    public class Search {

        int repositoryCount;

        PageInfo pageInfo;

        List<RepositoryNode> repositories;

        public Search(int repositoryCount, PageInfo pageInfo, List<RepositoryNode> repositories) {
            this.repositoryCount = repositoryCount;
            this.pageInfo = pageInfo;
            this.repositories = repositories;
        }

        public int getRepositoryCount() {
            return repositoryCount;
        }

        public void setRepositoryCount(int repositoryCount) {
            this.repositoryCount = repositoryCount;
        }

        public PageInfo getPageInfo() {
            return pageInfo;
        }

        public void setPageInfo(PageInfo pageInfo) {
            this.pageInfo = pageInfo;
        }

        public List<RepositoryNode> getRepositories() {
            return repositories;
        }

        public void setRepositories(List<RepositoryNode> repositories) {
            this.repositories = repositories;
        }
    }

    public class RepositoryNode {
        ResponseRepository node;

        public RepositoryNode(ResponseRepository node) {
            this.node = node;
        }

        public ResponseRepository getNode() {
            return node;
        }

        public void setNode(ResponseRepository node) {
            this.node = node;
        }
    }

    public class ResponseRepository {
        private String id;

        private String name;

        private String description;

        private int forkCount;

        private ResponseWatchers watchers;

        private User owner;

        public ResponseRepository(String id, String name, String description, int forkCount, ResponseWatchers watchers, User owner) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.forkCount = forkCount;
            this.watchers = watchers;
            this.owner = owner;
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

        public ResponseWatchers getWatchers() {
            return watchers;
        }

        public void setWatchers(ResponseWatchers watchers) {
            this.watchers = watchers;
        }

        public User getOwner() {
            return owner;
        }

        public void setOwner(User owner) {
            this.owner = owner;
        }
    }

    public class ResponseWatchers {
        private int totalCount;

        @Ignore
        private List<UserNode> watchers;

        public ResponseWatchers(int totalCount) {
            this.totalCount = totalCount;
        }

        public ResponseWatchers(int totalCount, List<UserNode> watchers) {
            this.totalCount = totalCount;
            this.watchers = watchers;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<UserNode> getWatchers() {
            return watchers;
        }

        public void setWatchers(List<UserNode> watchers) {
            this.watchers = watchers;
        }
    }

    public class UserNode {

        User node;

        public UserNode(User node) {
            this.node = node;
        }

        public User getNode() {
            return node;
        }

        public void setNode(User node) {
            this.node = node;
        }
    }

    public class PageInfo {

        String endCursor;

        boolean hasNextPage;

        public PageInfo(String endCursor, boolean hasNextPage) {
            this.endCursor = endCursor;
            this.hasNextPage = hasNextPage;
        }

        public String getEndCursor() {
            return endCursor;
        }

        public void setEndCursor(String endCursor) {
            this.endCursor = endCursor;
        }

        public boolean hasNextPage() {
            return hasNextPage;
        }

        public void setHasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
        }
    }

}
