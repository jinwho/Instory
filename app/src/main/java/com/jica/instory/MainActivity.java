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
    //프로필 목록
    private List<Profile> profileList;
    //DB접근
    private ProfileDao profileDao;
    //View
    private RecyclerView rv;
    //Adapter
    private ProfileRecyclerViewAdapter rvAdapter;

    //오른쪽 상단 메뉴를 생성한다.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //목록 편집 : 프로필 여러개를 선택한다.
    //그룹 : 그룹관리 화면으로 넘어간다.
    //설정 : 설정화면으로 넘어간다.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profileedit:
                return true;
            case R.id.group:
                return true;
            case R.id.setting:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //프로필을 추가하거나
    //다른 액티비티에서 돌아왔을 때 목록을 갱신한다.
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

        //FloatingActionButton 클릭시 프로필 추가 화면으로 넘어간다.
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddOrEditProfileActivity.class);
                startActivity(intent);
            }
        });

        //DB에 저장 되어있는 프로필 목록을 불러온다.
        profileDao = AppDatabase.getInstance(this).profileDao();
        profileList = profileDao.getAll();

        // 어댑터에서 목록을 처리해서 보여준다.
        rvAdapter = new ProfileRecyclerViewAdapter(profileList);
        rv = findViewById(R.id.profile_list);
        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
