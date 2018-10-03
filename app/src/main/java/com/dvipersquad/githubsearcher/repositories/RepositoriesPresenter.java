package com.dvipersquad.githubsearcher.repositories;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dvipersquad.githubsearcher.data.Repository;
import com.dvipersquad.githubsearcher.data.source.RepositoriesDataSource;
import com.dvipersquad.githubsearcher.data.source.RepositoriesRepository;
import com.dvipersquad.githubsearcher.di.ActivityScoped;

import java.util.List;

import javax.inject.Inject;

@ActivityScoped
public class RepositoriesPresenter implements RepositoriesContract.Presenter {

    private static final String DEFAULT_QUERY = "a";
    private static final String TAG = RepositoriesPresenter.class.getSimpleName();
    private final RepositoriesRepository repositoriesRepository;

    @Nullable
    private RepositoriesContract.View repositoriesView;

    private boolean firstLoad = true;
    private String lastElementLoaded;
    private String lastQueryLoaded = DEFAULT_QUERY;

    @Inject
    RepositoriesPresenter(RepositoriesRepository repositoriesRepository) {
        this.repositoriesRepository = repositoriesRepository;
    }

    @Override
    public void loadRepositories(boolean forceUpdate, final String query) {
        if (repositoriesView != null) {
            repositoriesView.showLoadingIndicator(true);
        }

        if (query != null && !query.isEmpty() && !lastQueryLoaded.equals(query)) {
            lastQueryLoaded = query;
            lastElementLoaded = null;
            firstLoad = true;
        }

        if (forceUpdate || firstLoad) {
            repositoriesRepository.refreshRepositories();
        }

        repositoriesRepository.getRepositories(lastQueryLoaded, lastElementLoaded, new RepositoriesDataSource.LoadRepositoriesCallback() {

            @Override
            public void onRepositoriesLoaded(List<Repository> repositories, @NonNull String lastElement, boolean hasNextPage) {
                if (repositoriesView == null || !repositoriesView.isActive()) {
                    return;
                }
                lastElementLoaded = lastElement;
                repositoriesView.showLoadingIndicator(false);
                repositoriesView.showRepositories(repositories, hasNextPage, firstLoad);
                firstLoad = false;
            }

            @Override
            public void onDataNotAvailable(String message) {
                if (repositoriesView == null || !repositoriesView.isActive()) {
                    return;
                }
                repositoriesView.showLoadingIndicator(false);
                repositoriesView.showErrorMessage(message);
            }
        });
    }

    @Override
    public void openRepositoryDetailsView(@NonNull Repository repository) {
        if (this.repositoriesView != null) {
            repositoriesView.showRepositoryDetailsUI(repository.getId());
        }
    }

    @Override
    public void takeView(RepositoriesContract.View view) {
        this.repositoriesView = view;
        if (lastQueryLoaded == null || lastQueryLoaded.isEmpty() || lastQueryLoaded.equals(DEFAULT_QUERY)) {
            loadRepositories(false, DEFAULT_QUERY);
        }
    }

    @Override
    public void dropView() {
        this.repositoriesView = null;
    }
}
