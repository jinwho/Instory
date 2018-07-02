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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.edit:
                //프로필들을 편집 한다.
                return true;
            case R.id.group:
                Intent intentGroup = new Intent(getApplicationContext(),GroupActivity.class);
                startActivity(intentGroup);
                return true;
            case R.id.setting:
                Intent intentSetting = new Intent(getApplicationContext(),SettingActivity.class);
                startActivity(intentSetting);
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
                Intent intent = new Intent(getApplicationContext(), AddProfileActivity.class);
                startActivity(intent);
            }
        });

        //read DB
        profileDao = AppDatabase.getInstance(this).profileDao();
        profileList = profileDao.getAll();

        // send to RecyclerViewAdapter
        rvAdapter = new RecyclerViewAdapter(profileList);
        rv = findViewById(R.id.profile_list);
        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
