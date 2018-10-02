package com.dvipersquad.githubsearcher.data.source.remote;

import com.dvipersquad.githubsearcher.data.Repository;

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
        Repository node;

        public RepositoryNode(Repository node) {
            this.node = node;
        }

        public Repository getNode() {
            return node;
        }

        public void setNode(Repository node) {
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
