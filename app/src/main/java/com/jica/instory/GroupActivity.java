package com.jica.instory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jica.instory.adapter.BandAdapter;
import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.dao.BandDao;
import com.jica.instory.database.entity.Band;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupActivity extends AppCompatActivity {
    BandDao bandDao = AppDatabase.getInstance(this).bandDao();
    List<Band> bands;
    BandAdapter bandAdapter;

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.add_group)
    Button add_group;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);
        rv.setLayoutManager(new LinearLayoutManager(this));

        //get db
        bands = bandDao.getAll();
        bandAdapter = new BandAdapter(this);
        rv.setAdapter(bandAdapter);
        bandAdapter.setBands(bands);

        /*save.setOnClickListener(v -> {
            Band b = new Band();
            b.setName(contents.getText().toString());
            bandDao.insert(b);
            bandAdapter.setBands(bandDao.getAll());
            contents.setText("");
        });*/
        back.setOnClickListener(v -> finish());

        add_group.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this);
            builder.setTitle(R.string.new_group);
            // Set up the input
            final EditText group_name = new EditText(GroupActivity.this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            group_name.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(group_name);
            builder.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                Band band = new Band();
                band.setName(group_name.getText().toString());
                bandDao.insert(band);
                bands.add(band);
                bandAdapter.notifyItemInserted(bandAdapter.getItemCount());
            });
            builder.setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
            builder.create().show();
        });
    }
}
