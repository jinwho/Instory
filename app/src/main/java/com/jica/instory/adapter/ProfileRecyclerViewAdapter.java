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
import com.jica.instory.database.ProfileMinimal;

import java.util.List;

public class ProfileRecyclerViewAdapter extends RecyclerView.Adapter<ProfileRecyclerViewAdapter.ProfileRecyclerViewHolder> {

    //프로필 목록
    private List<ProfileMinimal> profiles;

    //View Holder
    class ProfileRecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView name, comment;
        public RatingBar rating;

        public ProfileRecyclerViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            rating = view.findViewById(R.id.ratingBar);
            comment = view.findViewById(R.id.comment);
        }
    }

    public ProfileRecyclerViewAdapter(List<ProfileMinimal> profileMinialList) {
        this.profiles = profileMinialList;
    }

    public List<ProfileMinimal> getProfiles() {
        return profiles;
    }
    public void setProfiles(List<ProfileMinimal> profiles) {
        this.profiles = profiles;
    }

    @NonNull
    @Override
    public ProfileRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //뷰 홀더에 레이아웃을 inflate 시킨다.
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_profile, parent, false);
        return new ProfileRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileRecyclerViewHolder holder, int position) {
        final ProfileMinimal profile = profiles.get(position);

        //프로필 목록에서 위치에 해당하는 프로필을 읽어와 값을 할당한다.
        holder.name.setText(profile.getName());
        holder.comment.setText(profile.getComment());
        holder.rating.setRating(profile.getRating());

        //해당 item 클릭시 ViewProfileActivity 에 id를 전달하고 넘어간다.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ViewProfileActivity.class);
                intent.putExtra("id", profile.getPid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }


}
