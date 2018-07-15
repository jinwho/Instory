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
import com.jica.instory.database.dao.NoteDao;
import com.jica.instory.database.entity.Note;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    //View Holder
    class NoteViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.contents)
        TextView contents;
        @BindView(R.id.del)
        ImageView del;

        NoteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //프로필 목록
    private List<Note> notes;
    private final LayoutInflater mInflater;
    private NoteDao noteDao;

    public NoteAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        noteDao = AppDatabase.getInstance(context).noteDao();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //뷰 홀더에 레이아웃을 inflate 시킨다.
        View itemView = mInflater.inflate(R.layout.row_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        //위치에 해당하는 프로필
        final Note note = notes.get(position);

        //프로필을 view 의 값으로 할당
        holder.date.setText(note.getDate());
        holder.contents.setText(note.getContent());
        holder.del.setOnClickListener(v -> {
            noteDao.delete(note);
            removeAt(position);
        });
    }

    @Override
    public int getItemCount() {
        if (notes != null)
            return notes.size();
        else return 0;
    }

    private void removeAt(int position) {
        notes.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, notes.size());
    }
}
