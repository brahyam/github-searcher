package com.dvipersquad.githubsearcher.data.source.local;

import android.support.annotation.NonNull;

import com.dvipersquad.githubsearcher.data.Repository;
import com.dvipersquad.githubsearcher.data.User;
import com.dvipersquad.githubsearcher.data.source.RepositoriesDataSource;
import com.dvipersquad.githubsearcher.utils.AppExecutors;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RepositoriesLocalDataSource implements RepositoriesDataSource {

    private static final String NOT_FOUND_MESSAGE = "Not found";
    private final RepositoriesDao repositoriesDao;

    private final AppExecutors appExecutors;

    @Inject
    public RepositoriesLocalDataSource(RepositoriesDao repositoriesDao, AppExecutors appExecutors) {
        this.repositoriesDao = repositoriesDao;
        this.appExecutors = appExecutors;
    }

    @Override
    public void getRepositories(@NonNull final String query, final String lastElement, @NonNull final RepositoriesDataSource.LoadRepositoriesCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Repository> repositories = repositoriesDao.getRepositoriesForQuery(query);
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (repositories.isEmpty()) {
                            callback.onDataNotAvailable(NOT_FOUND_MESSAGE);
                        } else {
                            callback.onRepositoriesLoaded(repositories, lastElement, false);
                        }
                    }
                });
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getRepository(@NonNull final String repositoryId, @NonNull final GetRepositoryCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Repository repository = repositoriesDao.getRepositoryById(repositoryId);
                final List<User> repositoryWatchers = repositoriesDao.getRepositoryUsers(repositoryId);
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (repository == null) {
                            if (repository.getWatchers() != null) {
                                repository.getWatchers().setWatchers(repositoryWatchers);
                            }
                            callback.onDataNotAvailable(NOT_FOUND_MESSAGE);
                        } else {
                            callback.onRepositoryLoaded(repository);
                        }
                    }
                });
            }
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveRepository(@NonNull final Repository repository) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                repositoriesDao.insertRepository(repository);
                repositoriesDao.insertUsers(repository.getWatchers().getWatchers());
            }
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteAllRepositories() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                repositoriesDao.deleteRepositories();
            }
        };
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteRepository(@NonNull final String repositoryId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                repositoriesDao.deleteRepositoryById(repositoryId);
            }
        };
        appExecutors.diskIO().execute(runnable);
    }
}
