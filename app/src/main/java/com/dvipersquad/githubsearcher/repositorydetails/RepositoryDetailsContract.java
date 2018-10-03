package com.dvipersquad.githubsearcher.repositorydetails;

import com.dvipersquad.githubsearcher.BasePresenter;
import com.dvipersquad.githubsearcher.BaseView;
import com.dvipersquad.githubsearcher.data.Repository;

public interface RepositoryDetailsContract {

    interface View extends BaseView<Presenter> {

        void showLoadingIndicator(boolean active);

        void showRepository(Repository repository);

        void showMissingRepository();

        boolean isActive();

        void showErrorMessage(String message);
    }

    interface Presenter extends BasePresenter<View> {

        void loadUsers(boolean forceUpdate);

    }
}
