package com.dvipersquad.githubsearcher.data.source;

import android.support.annotation.NonNull;

import com.dvipersquad.githubsearcher.data.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RepositoriesRepository implements RepositoriesDataSource {

    private static final String TAG = RepositoriesRepository.class.getSimpleName();
    private static final String EMPTY_CURSOR = "";
    private final RepositoriesDataSource repositoriesRemoteDataSource;
    private final RepositoriesDataSource repositoriesLocalDataSource;

    Map<String, Repository> cachedRepositories;

    private boolean cacheIsDirty = false;

    @Inject
    RepositoriesRepository(@Remote RepositoriesDataSource repositoriesRemoteDataSource, @Local RepositoriesDataSource repositoriesLocalDataSource) {
        this.repositoriesRemoteDataSource = repositoriesRemoteDataSource;
        this.repositoriesLocalDataSource = repositoriesLocalDataSource;
    }

    @Override
    public void getRepositories(@NonNull final String query, final String lastElement, @NonNull final LoadRepositoriesCallback callback) {
        if (cachedRepositories != null && !cacheIsDirty && (lastElement == null || lastElement.isEmpty())) {
            callback.onRepositoriesLoaded(new ArrayList<>(cachedRepositories.values()), EMPTY_CURSOR, true);
        }

        if (cacheIsDirty || (lastElement != null && !lastElement.isEmpty())) {
            getRepositoriesFromRemoteDataSource(query, lastElement, callback);
        } else {
            repositoriesLocalDataSource.getRepositories(query, lastElement, new LoadRepositoriesCallback() {

                @Override
                public void onRepositoriesLoaded(List<Repository> repositories, @NonNull String lastElement, boolean hasNextPage) {
                    refreshCache(repositories);
                    callback.onRepositoriesLoaded(repositories, lastElement, hasNextPage);
                }

                @Override
                public void onDataNotAvailable(String message) {
                    getRepositoriesFromRemoteDataSource(query, lastElement, callback);
                }
            });
        }
    }

    private void getRepositoriesFromRemoteDataSource(final String query, String lastElement, final LoadRepositoriesCallback callback) {
        repositoriesRemoteDataSource.getRepositories(query, lastElement, new LoadRepositoriesCallback() {

            @Override
            public void onRepositoriesLoaded(List<Repository> repositories, @NonNull String lastElement, boolean hasNextPage) {
                refreshCache(repositories);
                refreshLocalDataSource(repositories);
                callback.onRepositoriesLoaded(repositories, lastElement, hasNextPage);
            }

            @Override
            public void onDataNotAvailable(String message) {
                callback.onDataNotAvailable(message);
            }
        });
    }

    private void refreshCache(List<Repository> repositories) {
        if (cachedRepositories == null) {
            cachedRepositories = new LinkedHashMap<>();
        }
        if (cacheIsDirty) {
            cachedRepositories.clear();
        }
        for (Repository repository : repositories) {
            cachedRepositories.put(repository.getId(), repository);
        }
        cacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Repository> repositories) {
        if (cacheIsDirty) {
            repositoriesLocalDataSource.deleteAllRepositories();
        }
        for (Repository repository : repositories) {
            repositoriesLocalDataSource.saveRepository(repository);
        }
    }

    @Override
    public void getRepository(@NonNull final String repositoryId, @NonNull final GetRepositoryCallback callback) {
        // Try cache
        if (cachedRepositories != null && !cachedRepositories.isEmpty()) {
            final Repository repository = cachedRepositories.get(repositoryId);
            if (repository != null) {
                callback.onRepositoryLoaded(repository);
                return;
            }
        }

        // Try local data
        repositoriesLocalDataSource.getRepository(repositoryId, new GetRepositoryCallback() {
            @Override
            public void onRepositoryLoaded(Repository repository) {
                if (cachedRepositories == null) {
                    cachedRepositories = new LinkedHashMap<>();
                }
                cachedRepositories.put(repository.getId(), repository);
                callback.onRepositoryLoaded(repository);
            }

            @Override
            public void onDataNotAvailable(String message) {
                // Try remote
                repositoriesRemoteDataSource.getRepository(repositoryId, new GetRepositoryCallback() {
                    @Override
                    public void onRepositoryLoaded(Repository repository) {
                        if (cachedRepositories == null) {
                            cachedRepositories = new LinkedHashMap<>();
                        }
                        cachedRepositories.put(repository.getId(), repository);
                        callback.onRepositoryLoaded(repository);
                    }

                    @Override
                    public void onDataNotAvailable(String message) {
                        callback.onDataNotAvailable(message);
                    }
                });
            }
        });
    }

    @Override
    public void saveRepository(@NonNull Repository repository) {
        repositoriesLocalDataSource.saveRepository(repository);
        if (cachedRepositories == null) {
            cachedRepositories = new LinkedHashMap<>();
        }
        cachedRepositories.put(repository.getId(), repository);
    }

    public void refreshRepositories() {
        cacheIsDirty = true;
    }

    @Override
    public void deleteAllRepositories() {
        repositoriesLocalDataSource.deleteAllRepositories();
        if (cachedRepositories == null) {
            cachedRepositories = new LinkedHashMap<>();
        }
        cachedRepositories.clear();
    }

    @Override
    public void deleteRepository(@NonNull String repositoryId) {
        repositoriesLocalDataSource.deleteRepository(repositoryId);
        if (cachedRepositories == null) {
            cachedRepositories = new LinkedHashMap<>();
        }
        cachedRepositories.remove(repositoryId);
    }

}
