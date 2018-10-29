package com.dvipersquad.githubsearcher.data.source;

import com.dvipersquad.githubsearcher.data.Repository;
import com.dvipersquad.githubsearcher.data.User;
import com.dvipersquad.githubsearcher.data.Watchers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RepositoriesRepositoryTest {

    private static final String DEFAULT_QUERY = "a";
    private static final String DEFAULT_LAST_ELEMENT = "testId3";
    private static final String DEFAULT_MESSAGE = "message";
    private static List<Repository> REPOSITORIES = new ArrayList<Repository>() {{
        add(new Repository(
                "testId1",
                "Test Name 1",
                "Test Description 1",
                1,
                new Watchers(1),
                new User("uId1", "uName1", "Url1", "testId1")
        ));
        add(new Repository(
                "testId2",
                "Test Name 2",
                "Test Description 2",
                2,
                new Watchers(1),
                new User("uId2", "uName2", "Url2", "testId2")
        ));
        add(new Repository(
                "testId3",
                "Test Name 3",
                "Test Description 3",
                3,
                new Watchers(1),
                new User("uId3", "uName3", "Url3", "testId3")
        ));
    }};

    private RepositoriesRepository repositoriesRepository;

    @Mock
    private RepositoriesDataSource repositoriesRemoteDataSource;

    @Mock
    private RepositoriesDataSource repositoriesLocalDataSource;

    @Mock
    private RepositoriesDataSource.GetRepositoryCallback getRepositoryCallback;

    @Mock
    private RepositoriesDataSource.LoadRepositoriesCallback loadRepositoriesCallback;

    @Captor
    private ArgumentCaptor<RepositoriesDataSource.LoadRepositoriesCallback> loadRepositoriesCallbackCaptor;

    @Captor
    private ArgumentCaptor<RepositoriesDataSource.GetRepositoryCallback> getRepositoryCallbackCaptor;

    @Before
    public void setupRepositoriesRepository() {
        MockitoAnnotations.initMocks(this);
        repositoriesRepository = new RepositoriesRepository(repositoriesRemoteDataSource, repositoriesLocalDataSource);
    }

    @Test
    public void getRepositories_repositoryCachesAfterFirstApiCall() {
        // send 2 calls to repo
        twoRepositoriesLoadCallsToRepository(loadRepositoriesCallback);

        // Then repositories were only requested once from Service API
        verify(repositoriesLocalDataSource).getRepositoriesNextPage(any(String.class), any(String.class), any(RepositoriesDataSource.LoadRepositoriesCallback.class));
    }

    @Test
    public void getRepositoriesNextPage_repositoriesAreRetrievedFromRemote() {
        repositoriesRepository.getRepositoriesNextPage(DEFAULT_QUERY, DEFAULT_LAST_ELEMENT, loadRepositoriesCallback);

        verify(repositoriesRemoteDataSource).getRepositoriesNextPage(any(String.class), any(String.class), any(RepositoriesDataSource.LoadRepositoriesCallback.class));
    }

    @Test
    public void getRepositories_requestsAllRepositoriesFromLocalDataSource() {
        // When repositories are requested from the repositories repository
        repositoriesRepository.getRepositories(DEFAULT_QUERY, loadRepositoriesCallback);

        // Then repositories are loaded from the local data source
        verify(repositoriesLocalDataSource).getRepositoriesNextPage(any(String.class), any(String.class), any(RepositoriesDataSource.LoadRepositoriesCallback.class));
    }

    @Test
    public void saveRepository_savesRepositoryToLocalDB() {
        // When a repository is saved
        repositoriesRepository.saveRepository(REPOSITORIES.get(0));

        // Then repository is saved locally and cache is updated
        verify(repositoriesLocalDataSource).saveRepository(eq(REPOSITORIES.get(0)));
        assertThat(repositoriesRepository.cachedRepositories.size(), is(1));
    }

    @Test
    public void getRepository_requestsSingleRepositoryFromLocalDataSource() {
        // When a repository is requested from the repositories repository
        repositoriesRepository.getRepository(REPOSITORIES.get(0).getId(), getRepositoryCallback);

        // Then the repository is loaded from the database
        verify(repositoriesLocalDataSource).getRepository(eq(REPOSITORIES.get(0).getId()), any(
                RepositoriesDataSource.GetRepositoryCallback.class));
    }

    @Test
    public void deleteAllRepositories_deleteRepositoriesFromLocalDBAndUpdatesCache() {

        repositoriesRepository.saveRepository(REPOSITORIES.get(0));
        repositoriesRepository.saveRepository(REPOSITORIES.get(1));
        repositoriesRepository.saveRepository(REPOSITORIES.get(2));

        // When all repositories are deleted to the repositories repository
        repositoriesRepository.deleteAllRepositories();

        // Verify the data sources were called
        verify(repositoriesLocalDataSource).deleteAllRepositories();
        // And cache is erased
        assertThat(repositoriesRepository.cachedRepositories.size(), is(0));
    }

    @Test
    public void deleteRepository_deleteRepositoryFromLocalDBAndRemovedFromCache() {
        // Given a repository in the repository
        repositoriesRepository.saveRepository(REPOSITORIES.get(0));
        assertThat(repositoriesRepository.cachedRepositories.containsKey(REPOSITORIES.get(0).getId()), is(true));

        // When deleted
        repositoriesRepository.deleteRepository(REPOSITORIES.get(0).getId());

        // Verify the local data source is called
        verify(repositoriesLocalDataSource).deleteRepository(eq(REPOSITORIES.get(0).getId()));

        // Verify it's removed from cache
        assertThat(repositoriesRepository.cachedRepositories.containsKey(REPOSITORIES.get(0).getId()), is(false));
    }

    @Test
    public void getRepositoriesWithDirtyCache_RepositoriesAreRetrievedFromRemote() {
        // Request repositories with dirty cache
        repositoriesRepository.refreshRepositories();
        repositoriesRepository.getRepositoriesNextPage(DEFAULT_QUERY, DEFAULT_LAST_ELEMENT, loadRepositoriesCallback);

        // And the remote data source has data available
        setRepositoriesAvailable(repositoriesRemoteDataSource, REPOSITORIES);

        // Verify the repositories from the remote data source are returned, not the local
        verify(repositoriesLocalDataSource, never()).getRepositoriesNextPage(any(String.class), any(String.class), eq(loadRepositoriesCallback));
        verify(loadRepositoriesCallback).onRepositoriesLoaded(eq(REPOSITORIES), any(String.class), any(Boolean.class));
    }

    @Test
    public void getRepositoriesWithLocalDataSourceUnavailable_repositoriesAreRetrievedFromRemote() {
        // Request repositories
        repositoriesRepository.getRepositories(DEFAULT_QUERY, loadRepositoriesCallback);

        // And the local data source has no data available
        setRepositoriesNotAvailable(repositoriesLocalDataSource);

        // And the remote data source has data available
        setRepositoriesAvailable(repositoriesRemoteDataSource, REPOSITORIES);

        // Verify the repositories from the local data source are returned
        verify(loadRepositoriesCallback).onRepositoriesLoaded(eq(REPOSITORIES), any(String.class), any(Boolean.class));
    }

    @Test
    public void getRepositoriesWithBothDataSourcesUnavailable_firesOnDataUnavailable() {
        // Request repositories
        repositoriesRepository.getRepositories(DEFAULT_QUERY, loadRepositoriesCallback);

        // data not available locally
        setRepositoriesNotAvailable(repositoriesLocalDataSource);

        // data not available remotely
        setRepositoriesNotAvailable(repositoriesRemoteDataSource);

        // Verify no data is returned
        verify(loadRepositoriesCallback).onDataNotAvailable(any(String.class));
    }

    @Test
    public void getRepositories_refreshesLocalDataSource() {
        // Force remote reload
        repositoriesRepository.refreshRepositories();

        // Request repositories
        repositoriesRepository.getRepositoriesNextPage(DEFAULT_QUERY, DEFAULT_LAST_ELEMENT, loadRepositoriesCallback);

        // Fake remote data source response
        setRepositoriesAvailable(repositoriesRemoteDataSource, REPOSITORIES);

        // Verify data fetched was saved in DB.
        verify(repositoriesLocalDataSource, times(REPOSITORIES.size())).saveRepository(any(Repository.class));
    }

    /**
     * Sends two calls to the repositories repository
     */
    private void twoRepositoriesLoadCallsToRepository(RepositoriesDataSource.LoadRepositoriesCallback callback) {
        // repositories are requested
        repositoriesRepository.getRepositories(DEFAULT_QUERY, callback); // First call to API

        // capture the callback
        verify(repositoriesLocalDataSource).getRepositoriesNextPage(any(String.class), any(String.class), loadRepositoriesCallbackCaptor.capture());

        // Local data source doesn't have data yet
        loadRepositoriesCallbackCaptor.getValue().onDataNotAvailable(null);

        // Verify the remote data source is queried
        verify(repositoriesRemoteDataSource).getRepositoriesNextPage(any(String.class), any(String.class), loadRepositoriesCallbackCaptor.capture());

        // Trigger callback so repositories are cached
        loadRepositoriesCallbackCaptor.getValue().onRepositoriesLoaded(REPOSITORIES, DEFAULT_LAST_ELEMENT, false);

        repositoriesRepository.getRepositories(DEFAULT_QUERY, callback); // Second call to API
    }

    private void setRepositoriesNotAvailable(RepositoriesDataSource dataSource) {
        verify(dataSource).getRepositoriesNextPage(any(String.class), any(String.class), loadRepositoriesCallbackCaptor.capture());
        loadRepositoriesCallbackCaptor.getValue().onDataNotAvailable(DEFAULT_MESSAGE);
    }

    private void setRepositoriesAvailable(RepositoriesDataSource dataSource, List<Repository> repositories) {
        verify(dataSource).getRepositoriesNextPage(any(String.class), any(String.class), loadRepositoriesCallbackCaptor.capture());
        loadRepositoriesCallbackCaptor.getValue().onRepositoriesLoaded(repositories, DEFAULT_LAST_ELEMENT, true);
    }
}
