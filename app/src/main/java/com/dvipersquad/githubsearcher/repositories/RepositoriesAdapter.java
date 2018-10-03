package com.dvipersquad.githubsearcher.repositories;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dvipersquad.githubsearcher.R;
import com.dvipersquad.githubsearcher.data.Repository;
import com.dvipersquad.githubsearcher.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RepositoriesAdapter extends RecyclerView.Adapter<RepositoriesAdapter.ViewHolder> {

    private List<Repository> repositories;
    private RepositoryListener repositoryListener;

    RepositoriesAdapter(List<Repository> repositories, RepositoryListener repositoryListener) {
        this.repositories = repositories;
        this.repositoryListener = repositoryListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.repositories_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Repository repository = repositories.get(position);

        if (repository.getOwner() != null && !TextUtils.isEmpty(repository.getOwner().getAvatarUrl())) {
            Picasso.get()
                    .load(repository.getOwner().getAvatarUrl())
                    .transform(new CircleTransform())
                    .into(holder.imgRepositoryOwnerAvatar);
        }

        holder.txtRepositoryName.setText(repository.getName());

        if (!TextUtils.isEmpty(repository.getDescription())) {
            holder.txtRepositoryDescription.setText(repository.getDescription());
        }

        String forkCount;
        if (repository.getForkCount() > 999) {
            forkCount = String.format(Locale.getDefault(), "%dK", repository.getForkCount() / 1000);
        } else {
            forkCount = String.format(Locale.getDefault(), "%d", repository.getForkCount());
        }
        holder.txtRepositoryForkCount.setText(forkCount);

        if (repositoryListener != null) {
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    repositoryListener.onItemClickListener(repository);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    void replaceData(List<Repository> repositories) {
        if (this.repositories == null) {
            this.repositories = new ArrayList<>();
        }
        this.repositories.clear();
        this.repositories.addAll(repositories);
        notifyDataSetChanged();
    }

    void addData(List<Repository> repositories) {
        if (this.repositories == null) {
            this.repositories = new ArrayList<>();
        }
        this.repositories.addAll(repositories);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View layout;
        ImageView imgRepositoryOwnerAvatar;
        TextView txtRepositoryName;
        TextView txtRepositoryDescription;
        TextView txtRepositoryForkCount;

        ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            imgRepositoryOwnerAvatar = itemView.findViewById(R.id.imgUserAvatar);
            txtRepositoryName = itemView.findViewById(R.id.txtUserName);
            txtRepositoryDescription = itemView.findViewById(R.id.txtRepositoryDescription);
            txtRepositoryForkCount = itemView.findViewById(R.id.txtRepositoryForkCount);
        }
    }

    public interface RepositoryListener {
        void onItemClickListener(Repository repository);
    }
}
