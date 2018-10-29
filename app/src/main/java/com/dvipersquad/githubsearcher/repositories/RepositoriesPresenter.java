package com.dvipersquad.githubsearcher.repositories;

import android.os.Bundle;
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

    private static final String TAG = RepositoriesPresenter.class.getSimpleName();
    private static final String STATE_FIRST_LOAD = "firstLoad";
    private static final String STATE_LAST_ELEMENT = "lastElement";
    private static final String STATE_LAST_QUERY = "lastQuery";

    private final RepositoriesRepository repositoriesRepository;

    @Nullable
    private RepositoriesContract.View repositoriesView;

    private boolean firstLoad = true;
    private String firstElementLoaded;
    private String lastElementLoaded;
    private String lastQueryLoaded;

    @Inject
    RepositoriesPresenter(RepositoriesRepository repositoriesRepository) {
        this.repositoriesRepository = repositoriesRepository;
    }

    @Override
    public void saveState(Bundle state) {
        state.putBoolean(STATE_FIRST_LOAD, firstLoad);
        state.putString(STATE_LAST_ELEMENT, firstElementLoaded);
        state.putString(STATE_LAST_QUERY, lastQueryLoaded);
    }

    @Override
    public void loadState(Bundle state) {
        firstLoad = state.getBoolean(STATE_FIRST_LOAD);
        lastElementLoaded = state.getString(STATE_LAST_ELEMENT);
        lastQueryLoaded = state.getString(STATE_LAST_QUERY);
        loadRepositories(false, lastQueryLoaded);
    }

    @Override
    public void loadRepositories(boolean forceUpdate, final String query) {
        if (query == null || query.isEmpty()) {
            return;
        }

        if (repositoriesView != null) {
            repositoriesView.toggleLoadingIndicator(true);
        }

        if (lastQueryLoaded == null || !lastQueryLoaded.equals(query)) {
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
                firstElementLoaded = lastElementLoaded;
                lastElementLoaded = lastElement;
                repositoriesView.toggleLoadingIndicator(false);
                repositoriesView.hideStartInstructionsText();
                repositoriesView.showRepositories(repositories, hasNextPage, firstLoad);
                firstLoad = false;
            }

            @Override
            public void onDataNotAvailable(String message) {
                if (repositoriesView == null || !repositoriesView.isActive()) {
                    return;
                }
                repositoriesView.toggleLoadingIndicator(false);
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
    }

    @Override
    public void dropView() {
        this.repositoriesView = null;
    }
}
