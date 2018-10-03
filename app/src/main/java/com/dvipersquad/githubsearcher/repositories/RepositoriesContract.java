package com.dvipersquad.githubsearcher.repositories;

import android.support.annotation.NonNull;

import com.dvipersquad.githubsearcher.BasePresenter;
import com.dvipersquad.githubsearcher.BaseView;
import com.dvipersquad.githubsearcher.data.Repository;

import java.util.List;

public interface RepositoriesContract {

    interface View extends BaseView<Presenter> {

        void showRepositories(List<Repository> repositories, boolean isLastPage, boolean clearAdapter);

        void showLoadingIndicator(boolean active);

        void showErrorMessage(String message);

        void showRepositoryDetailsUI(@NonNull String repositoryId);

        boolean isActive();
    }

    interface Presenter extends BasePresenter<View> {

        void loadRepositories(boolean forceUpdate, String query);

        void openRepositoryDetailsView(@NonNull Repository repository);

    }
}
