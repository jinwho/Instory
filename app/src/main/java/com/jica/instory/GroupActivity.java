package com.jica.instory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jica.instory.adapter.BandAdapter;
import com.jica.instory.adapter.ProfileAdapter;
import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.dao.BandDao;
import com.jica.instory.database.entity.Band;

import java.util.List;

public class GroupActivity extends AppCompatActivity {
    BandDao bandDao = AppDatabase.getInstance(this).bandDao();
    RecyclerView rv;
    BandAdapter ba;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        //get db
        List<Band> bands = bandDao.getAll();
        ba = new BandAdapter(this);
        rv.setAdapter(ba);
        ba.setBands(bands);

        /*save.setOnClickListener(v -> {
            Band b = new Band();
            b.setName(contents.getText().toString());
            bandDao.insert(b);
            ba.setBands(bandDao.getAll());
            contents.setText("");
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
