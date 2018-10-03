package com.dvipersquad.githubsearcher.data.source;

import android.support.annotation.NonNull;

import com.dvipersquad.githubsearcher.data.Repository;

import java.util.List;

public interface RepositoriesDataSource {

    interface LoadRepositoriesCallback {

        void onRepositoriesLoaded(List<Repository> repositories, @NonNull String lastElement, boolean hasNextPage);

        void onDataNotAvailable(String message);
    }

    interface GetRepositoryCallback {

        void onRepositoryLoaded(Repository repository);

        void onDataNotAvailable(String message);
    }

    void getRepositories(@NonNull String query, String lastElement, @NonNull LoadRepositoriesCallback callback);

    void getRepository(@NonNull String repositoryId, @NonNull GetRepositoryCallback callback);

    void saveRepository(@NonNull Repository repository);

    void deleteAllRepositories();

    void deleteRepository(@NonNull String repositoryId);
}
