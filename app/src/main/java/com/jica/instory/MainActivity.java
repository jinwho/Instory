package com.jica.instory;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jica.instory.adapter.ProfileAdapter;
import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.dao.ProfileDao;
import com.jica.instory.database.entity.ProfileMinimal;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProfileDao profileDao;
    private ProfileAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        RecyclerView rv = findViewById(R.id.profile_list);
        ImageView menu_button = findViewById(R.id.menu_button);

        //FloatingActionButton 클릭시 프로필 추가 화면으로
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddOrEditProfileActivity.class);
            startActivity(intent);
        });

        //메뉴버튼 클릭시 어차피 설정화면 없으니까 그룹 화면으로
        menu_button.setOnClickListener(v -> {
            Intent intent = new Intent(this, GroupActivity.class);
            startActivity(intent);
        });

        //DB에 저장 되어있는 프로필 목록을 불러와 어댑터 에서 처리한다.
        profileDao = AppDatabase.getInstance(this).profileDao();
        rvAdapter = new ProfileAdapter(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(rvAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //프로필 목록을 얻어서 adapter 에 보낸다.
        rvAdapter.setProfiles(profileDao.getAllMinimal());
    }
}
