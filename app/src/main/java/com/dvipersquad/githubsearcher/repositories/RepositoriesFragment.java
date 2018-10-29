package com.dvipersquad.githubsearcher.repositories;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dvipersquad.githubsearcher.R;
import com.dvipersquad.githubsearcher.data.Repository;
import com.dvipersquad.githubsearcher.di.ActivityScoped;
import com.dvipersquad.githubsearcher.repositorydetails.RepositoryDetailsActivity;
import com.dvipersquad.githubsearcher.utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

@ActivityScoped
public class RepositoriesFragment extends DaggerFragment implements RepositoriesContract.View {

    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

    private RepositoriesAdapter adapter;

    private ProgressBar progressBarRepositoriesLoading;

    private TextView txtStartInstructions;

    private RecyclerView recyclerRepositories;

    private String query;

    private boolean isLoading;

    private boolean isLastPage = false;

    private EndlessRecyclerViewScrollListener paginationListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
        @Override
        public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
            if (!isLoading && !isLastPage) {
                presenter.loadRepositories(false, query);
            }
        }
    };

    /**
     * Handles clicks on repositories
     */
    private RepositoriesAdapter.RepositoryListener listener = new RepositoriesAdapter.RepositoryListener() {
        @Override
        public void onItemClickListener(Repository repository) {
            presenter.openRepositoryDetailsView(repository);
        }
    };

    @Inject
    RepositoriesContract.Presenter presenter;

    @Inject
    public RepositoriesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new RepositoriesAdapter(new ArrayList<Repository>(), listener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.repositories_frag, container, false);
        progressBarRepositoriesLoading = rootView.findViewById(R.id.progressBarRepositoriesLoading);
        txtStartInstructions = rootView.findViewById(R.id.txtStartInstructions);
        recyclerRepositories = rootView.findViewById(R.id.recyclerRepositories);
        recyclerRepositories.setHasFixedSize(true);
        recyclerRepositories.setLayoutManager(linearLayoutManager);
        recyclerRepositories.addOnScrollListener(paginationListener);
        recyclerRepositories.setAdapter(adapter);

        final EditText editTextSearchRepositories = rootView.findViewById(R.id.editTextSearchRepositories);
        editTextSearchRepositories.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                query = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editTextSearchRepositories.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (!TextUtils.isEmpty(textView.getText().toString())) {
                    presenter.loadRepositories(true, query);
                    linearLayoutManager.smoothScrollToPosition(recyclerRepositories, null, 0);
                }

                return false;
            }
        });

        Button btnSearchRepositories = rootView.findViewById(R.id.btnSearchRepositories);
        btnSearchRepositories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(editTextSearchRepositories.getText().toString())) {
                    presenter.loadRepositories(true, query);
                    linearLayoutManager.smoothScrollToPosition(recyclerRepositories, null, 0);
                }
                if (getActivity() != null && getActivity().getSystemService(Context.INPUT_METHOD_SERVICE) != null && getView() != null && getView().getWindowToken() != null) {
                    final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
        isLoading = true; // taking the view causes the presenter to load repositories
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            presenter.loadState(savedInstanceState);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.dropView();
    }

    @Override
    public void showRepositories(List<Repository> repositories, boolean hasNextPage, boolean clearAdapter) {
        if (clearAdapter) {
            adapter.replaceData(repositories);
        } else {
            adapter.addData(repositories);
        }
        recyclerRepositories.setVisibility(View.VISIBLE);
        isLoading = false;
        isLastPage = !hasNextPage;
    }

    @Override
    public void toggleLoadingIndicator(boolean active) {
        if (active) {
            progressBarRepositoriesLoading.setVisibility(View.VISIBLE);
        } else {
            progressBarRepositoriesLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public void showErrorMessage(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        presenter.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showRepositoryDetailsUI(@NonNull String repositoryId) {
        Intent intent = new Intent(getContext(), RepositoryDetailsActivity.class);
        intent.putExtra(RepositoryDetailsActivity.EXTRA_REPOSITORY_ID, repositoryId);
        startActivity(intent);
    }

    @Override
    public void hideStartInstructionsText() {
        txtStartInstructions.setVisibility(View.GONE);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
