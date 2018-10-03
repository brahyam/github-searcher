package com.dvipersquad.githubsearcher.repositories;

import com.dvipersquad.githubsearcher.di.ActivityScoped;
import com.dvipersquad.githubsearcher.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RepositoriesModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract RepositoriesFragment repositoriesFragment();

    @ActivityScoped
    @Binds
    abstract RepositoriesContract.Presenter repositoriesPresenter(RepositoriesPresenter presenter);

}
