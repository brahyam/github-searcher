package com.dvipersquad.githubsearcher.di;


import com.dvipersquad.githubsearcher.repositories.RepositoriesActivity;
import com.dvipersquad.githubsearcher.repositories.RepositoriesModule;
import com.dvipersquad.githubsearcher.repositorydetails.RepositoryDetailsActivity;
import com.dvipersquad.githubsearcher.repositorydetails.RepositoryDetailsPresenterModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Holds references to the activities that are going to be injected for each module
 */
@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = RepositoriesModule.class)
    abstract RepositoriesActivity repositoriesActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = RepositoryDetailsPresenterModule.class)
    abstract RepositoryDetailsActivity repositoryDetailsActivity();

}
