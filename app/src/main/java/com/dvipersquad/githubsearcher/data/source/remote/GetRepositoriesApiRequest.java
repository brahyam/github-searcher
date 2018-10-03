package com.dvipersquad.githubsearcher.data.source.remote;

public class GetRepositoriesApiRequest {

    private static final String GET_REPOSITORIES_QUERY = "" +
            "query SearchRepositories(" +
            "$queryString: String!, $repositoryNumber: Int!, $after: String, $watcherNumber: Int!) {" +
            "search(query: $queryString, first: $repositoryNumber, after: $after, type: REPOSITORY) {" +
            "repositoryCount pageInfo { " +
            "endCursor " +
            "hasNextPage } " +
            "repositories: edges {" +
            " node { " +
            "... on Repository {" +
            " id " +
            "name " +
            "description " +
            "forkCount " +
            "watchers(first: $watcherNumber) {" +
            " totalCount " +
            "watchers: edges {" +
            " node { " +
            "... on User { " +
            "id " +
            "name " +
            "avatarUrl } " +
            "} " +
            "} " +
            "}  " +
            "owner { " +
            "... on User { " +
            "id " +
            "name " +
            "avatarUrl " +
            "} " +
            "} " +
            "} " +
            "} " +
            "} " +
            "} " +
            "}";

    private String query;
    private Variables variables;

    public GetRepositoriesApiRequest(Variables variables) {
        this.query = GET_REPOSITORIES_QUERY;
        this.variables = variables;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Variables getVariables() {
        return variables;
    }

    public void setVariables(Variables variables) {
        this.variables = variables;
    }

    public static class Variables {

        private String queryString;

        private Integer repositoryNumber;

        private String after;

        private Integer watcherNumber;

        public Variables(String queryString, Integer repositoryNumber, String after, Integer watcherNumber) {
            this.queryString = queryString;
            this.repositoryNumber = repositoryNumber;
            this.after = after;
            this.watcherNumber = watcherNumber;
        }

        public String getQueryString() {
            return queryString;
        }

        public void setQueryString(String queryString) {
            this.queryString = queryString;
        }

        public Integer getRepositoryNumber() {
            return repositoryNumber;
        }

        public void setRepositoryNumber(Integer repositoryNumber) {
            this.repositoryNumber = repositoryNumber;
        }

        public String getAfter() {
            return after;
        }

        public void setAfter(String after) {
            this.after = after;
        }

        public Integer getWatcherNumber() {
            return watcherNumber;
        }

        public void setWatcherNumber(Integer watcherNumber) {
            this.watcherNumber = watcherNumber;
        }
    }
}
