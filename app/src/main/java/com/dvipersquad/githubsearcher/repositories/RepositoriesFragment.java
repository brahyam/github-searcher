package com.dvipersquad.githubsearcher.repositories;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dvipersquad.githubsearcher.R;
import com.dvipersquad.githubsearcher.data.Repository;
import com.dvipersquad.githubsearcher.di.ActivityScoped;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

@ActivityScoped
public class RepositoriesFragment extends DaggerFragment implements RepositoriesContract.View {

    @Inject
    RepositoriesContract.Presenter presenter;

    @Inject
    public RepositoriesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.repositories_frag, container, false);
        if (getActivity() != null && getActivity().getActionBar() != null) {
            getActivity().getActionBar().setElevation(0);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.dropView();
    }

    @Override
    public void showRepositories(List<Repository> repositories, int page) {

    }

    @Override
    public void showLoadingIndicator() {

    }

    @Override
    public void showEmptyRepositoryList() {

    }

    @Override
    public void showErrorMessage(String message) {

    }

    @Override
    public void showRepositoryDetailsUI(@NonNull String repositoryId) {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
