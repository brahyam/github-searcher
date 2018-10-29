package com.dvipersquad.githubsearcher.repositorydetails;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dvipersquad.githubsearcher.R;
import com.dvipersquad.githubsearcher.data.User;
import com.dvipersquad.githubsearcher.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private List<User> users;
    private UsersAdapter.UserListener userListener;

    UsersAdapter(List<User> users, UsersAdapter.UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.repositorydetails_item, parent, false);
        return new UsersAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {
        final User user = users.get(position);

        if (!TextUtils.isEmpty(user.getAvatarUrl())) {
            Picasso.get()
                    .load(user.getAvatarUrl())
                    .transform(new CircleTransform())
                    .into(holder.imgUserAvatar);
        }

        holder.txtUserName.setText(user.getUserName());

        if (userListener != null) {
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userListener.onItemClickListener(user);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    void replaceData(List<User> users) {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        this.users.clear();
        this.users.addAll(users);
        notifyDataSetChanged();
    }

    void addData(List<User> users) {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        this.users.addAll(users);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View layout;
        ImageView imgUserAvatar;
        TextView txtUserName;

        ViewHolder(View itemView) {
            super(itemView);
            layout = itemView;
            imgUserAvatar = itemView.findViewById(R.id.imgUserAvatar);
            txtUserName = itemView.findViewById(R.id.txtRepositoryName);
        }
    }

    public interface UserListener {
        void onItemClickListener(User user);
    }
}
