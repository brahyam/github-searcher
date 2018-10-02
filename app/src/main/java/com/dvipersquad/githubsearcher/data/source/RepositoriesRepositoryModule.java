package com.dvipersquad.githubsearcher.data.source;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.dvipersquad.githubsearcher.data.source.local.RepositoriesDao;
import com.dvipersquad.githubsearcher.data.source.local.RepositoriesDatabase;
import com.dvipersquad.githubsearcher.data.source.local.RepositoriesLocalDataSource;
import com.dvipersquad.githubsearcher.data.source.remote.RepositoriesRemoteDataSource;
import com.dvipersquad.githubsearcher.utils.AppExecutors;
import com.dvipersquad.githubsearcher.utils.DiskIOThreadExecutor;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoriesRepositoryModule {

    private static final int THREAD_COUNT = 3;

    @Singleton
    @Provides
    @Local
    RepositoriesDataSource provideRepositoriesLocalDataSource(RepositoriesDao RepositoriesDao, AppExecutors executors) {
        return new RepositoriesLocalDataSource(RepositoriesDao, executors);
    }

    @Singleton
    @Provides
    @Remote
    RepositoriesDataSource provideRepositoriesRemoteDataSource() {
        return new RepositoriesRemoteDataSource();
    }

    @Singleton
    @Provides
    RepositoriesDatabase provideDB(Application context) {
        return Room.databaseBuilder(
                context.getApplicationContext(),
                RepositoriesDatabase.class, "Repositories.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    RepositoriesDao provideRepositoriesDao(RepositoriesDatabase database) {
        return database.repositoriesDao();
    }

    @Singleton
    @Provides
    AppExecutors provideAppExecutors() {
        return new AppExecutors(
                new DiskIOThreadExecutor(),
                Executors.newFixedThreadPool(THREAD_COUNT),
                new AppExecutors.MainThreadExecutor());
    }
}
