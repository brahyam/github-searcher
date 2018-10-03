package com.dvipersquad.githubsearcher.repositorydetails;

import com.dvipersquad.githubsearcher.data.Repository;
import com.dvipersquad.githubsearcher.data.User;
import com.dvipersquad.githubsearcher.data.Watchers;
import com.dvipersquad.githubsearcher.data.source.RepositoriesDataSource;
import com.dvipersquad.githubsearcher.data.source.RepositoriesRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RepositoryDetailsPresenterTests {

    public static final Repository REPOSITORY = new Repository(
            "testId1",
            "Test Name 1",
            "Test Description 1",
            1,
            new Watchers(1),
            new User("uId1", "uName1", "Url1", "testId1")
    );

    @Mock
    private RepositoriesRepository repositoriesRepository;

    @Mock
    private RepositoryDetailsContract.View repositoryDetailsView;

    @Captor
    private ArgumentCaptor<RepositoriesDataSource.GetRepositoryCallback> getRepositoryCallbackCaptor;

    private RepositoryDetailsPresenter repositoryDetailsPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // Presenter needs active view
        when(repositoryDetailsView.isActive()).thenReturn(true);
    }

    @Test
    public void getRepositoryFromRepositoryAndLoadIntoView() {

        repositoryDetailsPresenter = new RepositoryDetailsPresenter(
                REPOSITORY.getId(), repositoriesRepository);
        repositoryDetailsPresenter.takeView(repositoryDetailsView);

        // Verify loading indicator is activated
        verify(repositoriesRepository).getRepository(eq(REPOSITORY.getId()), getRepositoryCallbackCaptor.capture());
        InOrder inOrder = inOrder(repositoryDetailsView);
        inOrder.verify(repositoryDetailsView).showLoadingIndicator(true);

        getRepositoryCallbackCaptor.getValue().onRepositoryLoaded(REPOSITORY);

        // Verify loading indicator is disabled
        inOrder.verify(repositoryDetailsView).showLoadingIndicator(false);
        // Repository is shown
        verify(repositoryDetailsView).showRepository(REPOSITORY);
    }

    @Test
    public void getInvalidRepositoryFromRepositoryAndLoadIntoView() {
        // When loading of a repository is requested with an invalid repository ID.
        repositoryDetailsPresenter = new RepositoryDetailsPresenter(
                null, repositoriesRepository);
        repositoryDetailsPresenter.takeView(repositoryDetailsView);
        verify(repositoryDetailsView).showMissingRepository();
    }

}
