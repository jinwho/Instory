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

import java.util.List;

public class BandAdapter extends RecyclerView.Adapter<BandAdapter.BandViewHolder> {

    public class BandViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private ImageView del;

        public BandViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            del = itemView.findViewById(R.id.del);
        }
    }

    //그룹 목록
    private List<Band> bands;
    private final LayoutInflater mInflater;
    private Context context;
    private BandDao bandDao;

    public BandAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
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

        holder.name.setText(band.getName());
        holder.del.setOnClickListener(v -> {
            bandDao = AppDatabase.getInstance(context).bandDao();
            bandDao.delete(bands.get(position));
            removeAt(position);
        });
    }

    @Override
    public int getItemCount() {
        if (bands != null)
            return bands.size();
        else return 0;
    }

    public void removeAt(int position) {
        bands.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, bands.size());
    }
}
