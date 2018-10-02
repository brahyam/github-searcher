package com.dvipersquad.githubsearcher.repositories;

import android.support.annotation.NonNull;

import com.dvipersquad.githubsearcher.BasePresenter;
import com.dvipersquad.githubsearcher.BaseView;
import com.dvipersquad.githubsearcher.data.Repository;

import java.util.List;

public class RepositoriesContract {

    interface View extends BaseView<Presenter> {

        void showRepositories(List<Repository> repositories, int page);

        void showLoadingIndicator();

        void showEmptyRepositoryList();

        void showErrorMessage(String message);

        void showRepositoryDetailsUI(@NonNull String repositoryId);

        boolean isActive();
    }

    interface Presenter extends BasePresenter<View> {

        void loadRepositories(boolean forceUpdate, int page);

        void openRepositoryDetailsView(@NonNull Repository repository);

    }
}
