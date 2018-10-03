package com.dvipersquad.githubsearcher.repositorydetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dvipersquad.githubsearcher.R;
import com.dvipersquad.githubsearcher.data.Repository;
import com.dvipersquad.githubsearcher.data.User;
import com.dvipersquad.githubsearcher.di.ActivityScoped;
import com.dvipersquad.githubsearcher.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

@ActivityScoped
public class RepositoryDetailsFragment extends DaggerFragment implements RepositoryDetailsContract.View {

    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

    private UsersAdapter adapter;

    TextView txtRepositoryDetailsName;
    TextView txtRepositorySubscribersCount;
    private ProgressBar progressBarUsersLoading;

    private boolean isLoading;

    private boolean isLastPage = false;

    private EndlessRecyclerViewScrollListener paginationListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
        @Override
        public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//            if (!isLoading) {
//                presenter.loadUsers(false);
//            }
        }
    };

    /**
     * Handles clicks on users
     */
    private UsersAdapter.UserListener listener = new UsersAdapter.UserListener() {
        @Override
        public void onItemClickListener(User user) {
            // TODO: implement user details view
        }
    };

    @NonNull
    private static final String ARGUMENT_REPOSITORY_ID = "REPOSITORY_ID";

    @Inject
    String repositoryId;

    @Inject
    RepositoryDetailsContract.Presenter presenter;

    @Inject
    public RepositoryDetailsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new UsersAdapter(new ArrayList<User>(), listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.dropView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.repositorydetails_frag, container, false);
        progressBarUsersLoading = rootView.findViewById(R.id.progressBarUsersLoading);
        txtRepositoryDetailsName = rootView.findViewById(R.id.txtRepositoryDetailsName);
        txtRepositorySubscribersCount = rootView.findViewById(R.id.txtRepositorySubscribersCount);
        RecyclerView recyclerRepositorySubscribers = rootView.findViewById(R.id.recyclerRepositorySubscribers);
        recyclerRepositorySubscribers.setHasFixedSize(true);
        recyclerRepositorySubscribers.setLayoutManager(linearLayoutManager);
        recyclerRepositorySubscribers.addOnScrollListener(paginationListener);
        recyclerRepositorySubscribers.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void showLoadingIndicator(boolean active) {
        if (active) {
            progressBarUsersLoading.setVisibility(View.VISIBLE);
        } else {
            progressBarUsersLoading.setVisibility(View.GONE);
        }

    }

    @Override
    public void showRepository(Repository repository) {
        txtRepositoryDetailsName.setText(repository.getName());
        String subscriberCount;
        if (repository.getForkCount() > 999) {
            subscriberCount = String.format(Locale.getDefault(), "%dK", repository.getWatchers().getTotalCount() / 1000);
        } else {
            subscriberCount = String.format(Locale.getDefault(), "%d", repository.getWatchers().getTotalCount());
        }
        txtRepositorySubscribersCount.setText(subscriberCount);
        adapter.replaceData(repository.getWatchers().getWatchers());
    }

    @Override
    public void showMissingRepository() {
        txtRepositoryDetailsName.setText(getString(R.string.repository_not_found));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showErrorMessage(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }
}
