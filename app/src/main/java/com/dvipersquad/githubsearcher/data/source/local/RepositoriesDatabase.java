package com.dvipersquad.githubsearcher.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.dvipersquad.githubsearcher.data.Repository;
import com.dvipersquad.githubsearcher.data.User;

@Database(entities = {Repository.class, User.class}, version = 2, exportSchema = false)
public abstract class RepositoriesDatabase extends RoomDatabase {
    public abstract RepositoriesDao repositoriesDao();
}
