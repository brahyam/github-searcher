package com.dvipersquad.githubsearcher.repositories;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dvipersquad.githubsearcher.R;
import com.dvipersquad.githubsearcher.utils.ActivityUtils;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class RepositoriesActivity extends DaggerAppCompatActivity {

    @Inject
    RepositoriesPresenter repositoriesPresenter;

    @Inject
    Lazy<RepositoriesFragment> repositoriesFragmentProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repositories_act);

        RepositoriesFragment repositoriesFragment = (RepositoriesFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (repositoriesFragment == null) {
            repositoriesFragment = repositoriesFragmentProvider.get();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    repositoriesFragment,
                    R.id.contentFrame);
        }
    }
}
