package com.dvipersquad.githubsearcher.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dvipersquad.githubsearcher.data.Repository;
import com.dvipersquad.githubsearcher.data.User;

import java.util.List;

@Dao
public interface RepositoriesDao {

    /**
     * Select all repositories that matches query
     *
     * @return all repositories where name is like query, null if not found
     */
    @Query("SELECT * FROM Repositories WHERE name LIKE :query")
    List<Repository> getRepositoriesByQuery(String query);

    /**
     * Select all repositories
     *
     * @return all repositories
     */
    @Query("SELECT * FROM Repositories")
    List<Repository> getRepositories();

    /**
     * Select all repositories that matches query
     *
     * @param query repository name
     * @return all repositories that matches query
     */
    @Query("SELECT * FROM Repositories WHERE name LIKE :query ORDER BY id")
    List<Repository> getRepositoriesForQuery(String query);

    /**
     * Select a repository by id
     *
     * @param repositoryId the repository id
     * @return repository with repositoryId if found, null otherwise.
     */
    @Query("SELECT * FROM Repositories WHERE id = :repositoryId")
    Repository getRepositoryById(String repositoryId);

    /**
     * Store repository in DB (Replace if exist previously)
     *
     * @param repository the repository to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRepository(Repository repository);

    /**
     * Updates repository in DB
     *
     * @param repository the repository to be updated
     */
    @Update
    void updateRepository(Repository repository);

    /**
     * Deletes a repository by id
     *
     * @param repositoryId the repository id to be deleted
     * @return number of repositories deleted. 0 if not found / 1 if found.
     */
    @Query("DELETE FROM Repositories WHERE id = :repositoryId")
    int deleteRepositoryById(String repositoryId);

    /**
     * Deletes all repositories
     */
    @Query("DELETE FROM Repositories")
    void deleteRepositories();

    /**
     * Select all users that waatch a repository
     *
     * @param repositoryId the repository id
     * @return all users that are watching a repository
     */
    @Query("SELECT * FROM Users WHERE repositoryId = :repositoryId")
    List<User> getRepositoryUsers(String repositoryId);

    /**
     * Inserts a list of users into the db
     *
     * @param users
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(List<User> users);
}
