package com.dvipersquad.githubsearcher.repositorydetails;

import com.dvipersquad.githubsearcher.di.ActivityScoped;
import com.dvipersquad.githubsearcher.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RepositoryDetailsPresenterModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract RepositoryDetailsFragment hotelDetailsFragment();

    @ActivityScoped
    @Binds
    abstract RepositoryDetailsContract.Presenter detailsPresenter(RepositoryDetailsPresenter presenter);

    @Provides
    @ActivityScoped
    static String provideRepositoryId(RepositoryDetailsActivity activity) {
        return activity.getIntent().getStringExtra(RepositoryDetailsActivity.EXTRA_REPOSITORY_ID);
    }

}
