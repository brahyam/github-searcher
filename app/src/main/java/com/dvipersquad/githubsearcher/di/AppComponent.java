package com.dvipersquad.githubsearcher.di;

import android.app.Application;

import com.dvipersquad.githubsearcher.SearchApp;
import com.dvipersquad.githubsearcher.data.source.RepositoriesRepository;
import com.dvipersquad.githubsearcher.data.source.RepositoriesRepositoryModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        RepositoriesRepositoryModule.class,
        AppModule.class,
        ActivityBindingModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<SearchApp> {

    RepositoriesRepository getRepositoriesRepository();

    // Enable us to doDaggerAppComponent.builder().application(this).build().inject(this);
    // Includes application in graph
    @Component.Builder
    interface Builder {
        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}
