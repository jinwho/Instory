package com.jica.instory;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.jica.instory.adapter.ProfileRecyclerViewAdapter;
import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.Profile;
import com.jica.instory.database.ProfileDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Profile> profileList;
    private ProfileDao profileDao;
    private RecyclerView rv;
    private ProfileRecyclerViewAdapter rvAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.modify:
                return true;
            case R.id.group:
                return true;
            case R.id.setting:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //refresh recycler view
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
                Intent intent = new Intent(getApplicationContext(), AddOrEditProfileActivity.class);
                startActivity(intent);
            }
        });

        //read DB
        profileDao = AppDatabase.getInstance(this).profileDao();
        profileList = profileDao.getAll();

        // send to ProfileRecyclerViewAdapter
        rvAdapter = new ProfileRecyclerViewAdapter(profileList);
        rv = findViewById(R.id.profile_list);
        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
