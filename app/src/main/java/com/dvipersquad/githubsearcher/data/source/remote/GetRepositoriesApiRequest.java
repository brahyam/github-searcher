package com.dvipersquad.githubsearcher.data.source.remote;

public class GetRepositoriesApiRequest {

    private static final String GET_REPOSITORIES_QUERY = "" +
            "query SearchRepositories($queryString: String!, $number: Int!, $after: String) {" +
            "search(query: $queryString, first: $number, after: $after, type: REPOSITORY) {" +
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
//            "watchers(first: 3) {" +
//            " totalCount " +
//            "watchers: edges {" +
//            " node { " +
//            "... on User { " +
//            "id " +
//            "name " +
//            "avatarUrl } " +
//            "} " +
//            "} " +
//            "}  " +
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

        private Integer number;

        private String after;

        public Variables(String queryString, Integer number, String after) {
            this.queryString = queryString;
            this.number = number;
            this.after = after;
        }

        public String getQueryString() {
            return queryString;
        }

        public void setQueryString(String queryString) {
            this.queryString = queryString;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public String getAfter() {
            return after;
        }

        public void setAfter(String after) {
            this.after = after;
        }
    }
}
