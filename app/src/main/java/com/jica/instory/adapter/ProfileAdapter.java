package com.jica.instory.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jica.instory.R;
import com.jica.instory.ViewProfileActivity;
import com.jica.instory.database.entity.ProfileMinimal;
import com.jica.instory.manager.MyFileManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {
    //View Holder
    class ProfileViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.profile_pic)
        CircleImageView profile_pic;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.comment)
        TextView comment;
        @BindView(R.id.ratingBar)
        RatingBar rating;

        ProfileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //프로필 목록
    private List<ProfileMinimal> profiles;
    private final LayoutInflater mInflater;
    private Context context;

    public ProfileAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setProfiles(List<ProfileMinimal> profiles) {
        this.profiles = profiles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //뷰 홀더에 레이아웃을 inflate 시킨다.
        View itemView = mInflater.inflate(R.layout.row_main_profile, parent, false);
        return new ProfileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {

        if (profiles != null) {
            //위치에 해당하는 프로필
            final ProfileMinimal profile = profiles.get(position);
            //프로필을 view 의 값으로 할당
            holder.name.setText(profile.getName());
            holder.rating.setRating(profile.getRating());
            String comment = profile.getComment();

            if (comment.isEmpty()) {
                holder.comment.setVisibility(View.INVISIBLE);
            } else {
                holder.comment.setText(comment);
            }
            //사진이 있을 경우에만 보여준다.
            Bitmap bitmap = MyFileManager.getInstance().loadImage(context, profile.getFilename());
            if (bitmap != null) holder.profile_pic.setImageBitmap(bitmap);

            //해당 item 클릭시 ViewProfileActivity 에 id를 전달하고 넘어간다.
            holder.itemView.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, ViewProfileActivity.class);
                intent.putExtra("id", profile.getPid());
                context.startActivity(intent);
            });
        }
        holder.setIsRecyclable(false);
    }


    @Override
    public int getItemCount() {
        if (profiles != null)
            return profiles.size();
        else return 0;
    }

}
