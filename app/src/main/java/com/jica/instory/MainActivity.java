package com.jica.instory;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

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
        rv.setLayoutManager(new LinearLayoutManager(this));

        //FloatingActionButton 클릭시 프로필 추가 화면으로 넘어간다.
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddOrEditProfileActivity.class);
            startActivity(intent);
        });

        //DB에 저장 되어있는 프로필 목록을 불러와 어댑터 에서 처리한다.
        profileDao = AppDatabase.getInstance(this).profileDao();
        rvAdapter = new ProfileAdapter(this);
        rv.setAdapter(rvAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //프로필 목록을 얻어서 adapter 에 보낸다.
        rvAdapter.setProfiles(profileDao.getAllMinimal());
    }

    //오른쪽 상단 메뉴를 생성한다.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //그룹 : 그룹관리 화면으로 넘어간다.
    //설정 : 설정화면으로 넘어간다.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.group:
                Intent intent = new Intent(this, GroupActivity.class);
                startActivity(intent);
                return true;
            case R.id.setting:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
