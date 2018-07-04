package com.jica.instory.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jica.instory.R;
import com.jica.instory.ViewProfileActivity;
import com.jica.instory.database.Profile;

import java.util.List;

public class ProfileRecyclerViewAdapter extends RecyclerView.Adapter<ProfileRecyclerViewAdapter.ProfileRecyclerViewHolder> {

    private List<Profile> profileList;

    class ProfileRecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView name,comment;
        public RatingBar rating;
        public ProfileRecyclerViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            rating = view.findViewById(R.id.ratingBar);
            comment = view.findViewById(R.id.comment);
        }
    }

    public ProfileRecyclerViewAdapter(List<Profile> profileList) {
        this.profileList = profileList;
    }

    public List<Profile> getProfileList() {
        return profileList;
    }

    public void setProfileList(List<Profile> profileList) {
        this.profileList = profileList;
    }

    @NonNull
    @Override
    public ProfileRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_profile, parent, false);
        return new ProfileRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileRecyclerViewHolder holder, final int position) {

        final Profile profile = profileList.get(position);
        holder.name.setText(profile.getName());
        holder.comment.setText(profile.getComment());
        holder.rating.setRating(profile.getRating());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ViewProfileActivity.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }


}
