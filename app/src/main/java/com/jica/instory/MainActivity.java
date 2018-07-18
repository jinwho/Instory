package com.jica.instory;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.jica.instory.adapter.ProfileAdapter;
import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.dao.ProfileDao;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private ProfileDao profileDao;
    private ProfileAdapter profileAdapter;

    @BindView(R.id.profile_list) RecyclerView recyclerView;
    @BindView(R.id.main_menu) ImageView main_menu;
    @BindView(R.id.fab) FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //FloatingActionButton 클릭시 프로필 추가 화면으로
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), NewProfileActivity.class);
            startActivity(intent);
        });

        //메뉴버튼 클릭시 어차피 설정화면 없으니까 그룹 화면으로
        main_menu.setOnClickListener(v -> {
            Intent intent = new Intent(this, GroupActivity.class);
            startActivity(intent);
        });

        //DB에 저장 되어있는 프로필 목록을 불러와 어댑터 에서 처리한다.
        profileDao = AppDatabase.getInstance(this).profileDao();
        profileAdapter = new ProfileAdapter(this);
        recyclerView.setAdapter(profileAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerView.setHasFixedSize(true);//only for now
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //when visible -> update view
    @Override
    protected void onStart() {
        super.onStart();
        //다른 액티비티에서 삭제나 추가가 이루어 지기 때문에 이 방법 말고는없다.
        profileAdapter.setProfiles(profileDao.getAllMinimal());
    }


}
