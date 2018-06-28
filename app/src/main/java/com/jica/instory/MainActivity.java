package com.jica.instory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.Profile;
import com.jica.instory.database.ProfileDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ProfileDao profileDao;

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Profile> profiles;
        profileDao = AppDatabase.getInstance(getApplicationContext()).profileDao();
        rv = findViewById(R.id.profile_list);
    }

    public void addProfile(View v) {
        Intent intent = new Intent(getApplicationContext(), AddProfileActivity.class);
        startActivity(intent);
    }
}
