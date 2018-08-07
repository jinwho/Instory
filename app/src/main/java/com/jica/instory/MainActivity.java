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
import com.jica.instory.adapter.SimpleSectionedRecyclerViewAdapter;
import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.dao.BandDao;
import com.jica.instory.database.dao.ProfileDao;
import com.jica.instory.database.entity.Band;
import com.jica.instory.database.entity.ProfileMinimal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private ProfileDao profileDao;
    private BandDao bandDao;
    private ProfileAdapter profileAdapter;

    @BindView(R.id.profile_list)
    RecyclerView recyclerView;
    @BindView(R.id.main_menu)
    ImageView main_menu;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //FloatingActionButton 클릭시 프로필 추가 화면으로
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), NewProfileActivity.class);
            startActivity(intent);
        });

        //메뉴버튼 클릭시 그룹 관리 화면으로
        main_menu.setOnClickListener(v -> {
            Intent intent = new Intent(this, GroupActivity.class);
            startActivity(intent);
        });

        //DB data load
        profileDao = AppDatabase.getInstance(this).profileDao();
        bandDao = AppDatabase.getInstance(this).bandDao();

        profileAdapter = new ProfileAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        //recyclerView.setAdapter(profileAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        UpdateList();
    }

    private void UpdateList() {

        //This is the code to provide a sectioned list
        List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();
        List<ProfileMinimal> profiles = new ArrayList<>();
        int totalSize = 0;

        //그룹이 있는 프로필들
        List<Band> bands = bandDao.getAll();
        bands.add(0, null);
        for (Band band : bands) {

            List<ProfileMinimal> bandprofiles;
            String bandname;

            if (band == null) {
                bandprofiles = profileDao.getAllMinimal(null);
                bandname = "전체";
            } else {
                bandprofiles = profileDao.getAllMinimal(band.getBid());
                bandname = band.getName();
            }

            if (!bandprofiles.isEmpty()) {
                sections.add(new SimpleSectionedRecyclerViewAdapter.Section(totalSize, bandname));
                totalSize += bandprofiles.size();
                profiles.addAll(bandprofiles);
            }
        }
        if (!profiles.isEmpty()) profileAdapter.setProfiles(profiles);

        //Add your adapter to the sectionAdapter
        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter mSectionedAdapter =
                new SimpleSectionedRecyclerViewAdapter(this, R.layout.row_main_group,R.id.name, profileAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        recyclerView.setAdapter(mSectionedAdapter);

    }
}
