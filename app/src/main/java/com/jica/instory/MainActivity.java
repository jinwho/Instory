package com.jica.instory;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.graphics.Movie;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jica.instory.adapter.RecyclerViewAdapter;
import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.Profile;
import com.jica.instory.database.ProfileDao;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Profile> profileList = new ArrayList<>();
    private ProfileDao profileDao;
    private RecyclerView rv;
    private RecyclerViewAdapter rvAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        profileList = profileDao.getAll();
        rvAdapter.setProfileList(profileList);
        rvAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //handling fab
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddProfileActivity.class);
                startActivity(intent);
            }
        });

        //read DB
        profileDao = AppDatabase.getInstance(this).profileDao();
        profileList = profileDao.getAll();

        // show to RecyclerView
        rvAdapter = new RecyclerViewAdapter(profileList);
        rv = findViewById(R.id.profile_list);
        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
