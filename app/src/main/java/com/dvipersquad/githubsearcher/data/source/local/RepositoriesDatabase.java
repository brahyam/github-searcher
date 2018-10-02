package com.dvipersquad.githubsearcher.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.dvipersquad.githubsearcher.data.Repository;

@Database(entities = {Repository.class}, version = 1, exportSchema = false)
public abstract class RepositoriesDatabase extends RoomDatabase {
    public abstract RepositoriesDao repositoriesDao();
}
