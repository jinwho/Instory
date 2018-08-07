package com.jica.instory.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jica.instory.R;
import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.dao.BandDao;
import com.jica.instory.database.entity.Band;
import com.jica.instory.listener.OnGroupClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BandAdapter extends RecyclerView.Adapter<BandAdapter.BandViewHolder> {

    public class BandViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.del)
        ImageView del;

        private BandViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //그룹 목록
    private List<Band> bands;
    private final LayoutInflater mInflater;
    private BandDao bandDao;
    private OnGroupClickListener groupClick;

    public BandAdapter(Context context, OnGroupClickListener groupClick) {
        mInflater = LayoutInflater.from(context);
        bandDao = AppDatabase.getInstance(context).bandDao();
        this.groupClick = groupClick;
    }

    public void setBands(List<Band> bands) {
        this.bands = bands;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.row_group, parent, false);
        return new BandViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BandViewHolder holder, int position) {
        final Band band = bands.get(position);

        //TODO 여기 코드 뷰홀더로 옮길 것!!

        holder.name.setText(band.getName());
        holder.del.setOnClickListener(v -> {
            bandDao.delete(bands.get(position));
            removeAt(position);
        });
        if (groupClick != null){
            holder.itemView.setOnClickListener(v -> groupClick.onGroupClick(band.getBid()));
        }

    }

    @Override
    public int getItemCount() {
        if (bands != null)
            return bands.size();
        else return 0;
    }

    private void removeAt(int position) {
        bands.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, bands.size());
    }
}
