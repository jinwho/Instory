package com.jica.instory.adapter;

import android.graphics.Movie;
import android.media.Rating;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jica.instory.R;
import com.jica.instory.database.Profile;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private List<Profile> profileList;

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView name,comment;
        public RatingBar rating;
        public RecyclerViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            rating = view.findViewById(R.id.rating);
            comment = view.findViewById(R.id.comment);
        }
    }

    public RecyclerViewAdapter(List<Profile> profileList) {
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
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_profile, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Profile profile = profileList.get(position);
        holder.name.setText(profile.getName());
        holder.comment.setText(profile.getComment());
        holder.rating.setRating(profile.getRating());
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }


}
