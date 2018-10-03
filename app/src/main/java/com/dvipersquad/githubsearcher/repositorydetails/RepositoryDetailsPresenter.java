package com.dvipersquad.githubsearcher.repositorydetails;

import android.support.annotation.Nullable;

import com.dvipersquad.githubsearcher.data.Repository;
import com.dvipersquad.githubsearcher.data.source.RepositoriesDataSource;
import com.dvipersquad.githubsearcher.data.source.RepositoriesRepository;
import com.dvipersquad.githubsearcher.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
public class RepositoryDetailsPresenter implements RepositoryDetailsContract.Presenter {

    private RepositoriesRepository repositoriesRepository;

    @Nullable
    private RepositoryDetailsContract.View repositoryDetailsView;

    @Nullable
    private String repositoryId;

    // Required when loading users
    private boolean firstLoad = true;
    private String lastElementLoaded;

    @Inject
    RepositoryDetailsPresenter(@Nullable String repositoryId, RepositoriesRepository repositoriesRepository) {
        this.repositoriesRepository = repositoriesRepository;
        this.repositoryId = repositoryId;
    }

    @Override
    public void takeView(RepositoryDetailsContract.View view) {
        repositoryDetailsView = view;
        loadRepository();
    }

    private void loadRepository() {
        if (repositoryId == null) {
            if (repositoryDetailsView != null) {
                repositoryDetailsView.showMissingRepository();
            }
            return;
        }

        if (repositoryDetailsView != null) {
            repositoryDetailsView.showLoadingIndicator(true);
        }

        repositoriesRepository.getRepository(repositoryId, new RepositoriesDataSource.GetRepositoryCallback() {
            @Override
            public void onRepositoryLoaded(final Repository repository) {
                if (repositoryDetailsView == null || !repositoryDetailsView.isActive()) {
                    return;
                }
                repositoryDetailsView.showLoadingIndicator(false);
                if (repository != null) {
                    repositoryDetailsView.showRepository(repository);
                    firstLoad = false;
                }
            }

            @Override
            public void onDataNotAvailable(String message) {
                if (repositoryDetailsView == null || !repositoryDetailsView.isActive()) {
                    return;
                }
                repositoryDetailsView.showErrorMessage(message);
                repositoryDetailsView.showMissingRepository();
            }
        });

    }

    @Override
    public void dropView() {
        repositoryDetailsView = null;
    }

    @Override
    public void loadUsers(boolean forceUpdate) {
        // TODO: implement for endless scroll
    }
}
