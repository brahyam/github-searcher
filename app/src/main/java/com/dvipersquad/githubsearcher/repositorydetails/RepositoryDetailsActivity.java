package com.dvipersquad.githubsearcher.repositorydetails;

import android.os.Bundle;

import com.dvipersquad.githubsearcher.R;
import com.dvipersquad.githubsearcher.utils.ActivityUtils;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class RepositoryDetailsActivity extends DaggerAppCompatActivity {
    public static final String EXTRA_REPOSITORY_ID = "REPOSITORY_ID";

    @Inject
    RepositoryDetailsFragment injectedRepositoryDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repositorydetails_act);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        RepositoryDetailsFragment repositoryDetailsFragment =
                (RepositoryDetailsFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.contentFrame);

        if (repositoryDetailsFragment == null) {
            repositoryDetailsFragment = this.injectedRepositoryDetailsFragment;
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    repositoryDetailsFragment, R.id.contentFrame);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
