package com.dvipersquad.githubsearcher.repositories;

import android.support.annotation.NonNull;

import com.dvipersquad.githubsearcher.data.Repository;
import com.dvipersquad.githubsearcher.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
public class RepositoriesPresenter implements RepositoriesContract.Presenter {

    @Inject
    public RepositoriesPresenter() {
    }

    @Override
    public void loadRepositories(boolean forceUpdate, int page) {

    }

    @Override
    public void openRepositoryDetailsView(@NonNull Repository repository) {

    }

    @Override
    public void takeView(RepositoriesContract.View view) {

    }

    @Override
    public void dropView() {

    }
}
