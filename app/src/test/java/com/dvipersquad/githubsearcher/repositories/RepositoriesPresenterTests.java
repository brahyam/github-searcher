package com.dvipersquad.githubsearcher.repositories;

import com.dvipersquad.githubsearcher.data.Repository;
import com.dvipersquad.githubsearcher.data.User;
import com.dvipersquad.githubsearcher.data.source.RepositoriesDataSource;
import com.dvipersquad.githubsearcher.data.source.RepositoriesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RepositoriesPresenterTests {

    private static List<Repository> REPOSITORIES;

    private static final String DEFAULT_QUERY = "a";
    private static final String DEFAULT_LAST_ELEMENT = "testId3";
    private static final String DEFAULT_MESSAGE = "message";

    @Mock
    private RepositoriesRepository repositoriesRepository;

    @Mock
    private RepositoriesContract.View repositoriesView;

    @Captor
    private ArgumentCaptor<RepositoriesDataSource.LoadRepositoriesCallback> loadRepositoriesCallbackCaptor;

    private RepositoriesPresenter repositoriesPresenter;

    @Before
    public void setupRepositoriesPresenter() {
        MockitoAnnotations.initMocks(this);

        repositoriesPresenter = new RepositoriesPresenter(repositoriesRepository);
        repositoriesPresenter.takeView(repositoriesView);

        when(repositoriesView.isActive()).thenReturn(true);

        REPOSITORIES = new ArrayList<Repository>() {{
            add(new Repository(
                    "testId1",
                    "Test Name 1",
                    "Test Description 1",
                    1,
                    new User("uId1", "uName1", "Url1")
            ));
            add(new Repository(
                    "testId2",
                    "Test Name 2",
                    "Test Description 2",
                    2,
                    new User("uId2", "uName2", "Url2")
            ));
            add(new Repository(
                    "testId3",
                    "Test Name 3",
                    "Test Description 3",
                    3,
                    new User("uId3", "uName3", "Url3")
            ));
        }};
    }

    @Test
    public void loadAllRepositoriesFromRepositoryAndLoadIntoView() {
        // Request repositories load
        repositoriesPresenter.loadRepositories(true, DEFAULT_QUERY);

        // Called 2 times. On fragment added/ On load requested
        verify(repositoriesRepository, times(2))
                .getRepositories(any(String.class), any(String.class), loadRepositoriesCallbackCaptor.capture());

        loadRepositoriesCallbackCaptor.getValue().onRepositoriesLoaded(REPOSITORIES, DEFAULT_LAST_ELEMENT, true);

        // Progress indicator is shown
        verify(repositoriesView, times(2)).showLoadingIndicator(true);
        // Progress indicator is hidden and repositories are shown
        verify(repositoriesView, times(1)).showLoadingIndicator(false);
        ArgumentCaptor<List> showRepositoriesArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(repositoriesView).showRepositories(eq(REPOSITORIES), any(Boolean.class), any(Boolean.class));
    }


    @Test
    public void unavailableTasks_ShowsError() {
        // When repositories are loaded
        repositoriesPresenter.loadRepositories(true, DEFAULT_QUERY);

        // And the repositories aren't available in the repository
        verify(repositoriesRepository, times(2)).getRepositories(any(String.class), any(String.class), loadRepositoriesCallbackCaptor.capture());
        loadRepositoriesCallbackCaptor.getValue().onDataNotAvailable(DEFAULT_MESSAGE);

        // Then an error message is shown
        verify(repositoriesView).showErrorMessage(eq(DEFAULT_MESSAGE));
    }

}
