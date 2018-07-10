package com.jica.instory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.entity.Profile;
import com.jica.instory.database.dao.ProfileDao;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ViewProfileActivity extends AppCompatActivity {
    //DB객체
    private ProfileDao profileDao = AppDatabase.getInstance(this).profileDao();
    //프로필
    private Profile profile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        //view 가져오기
        TextView tvName = findViewById(R.id.name);
        TextView tvComment = findViewById(R.id.comment);
        RatingBar rating = findViewById(R.id.ratingBar);
        ImageView profile_pic = findViewById(R.id.profile_pic);

        //뒤로가기 버튼 만들기
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // 보내온 id를 가진 프로필을 뷰를 통해 보여준다.
        Intent intent = getIntent();
        Integer id = intent.getIntExtra("id", -1);
        //DB id 검색
        profile = profileDao.get(id);
        //view 값 할당
        tvName.setText(profile.getName());
        tvComment.setText(profile.getComment());
        rating.setRating(profile.getRating());

        //check if it has profile photo
        String filename = String.valueOf(profile.getPid());
        FileInputStream inputStream;
        try {
            inputStream = openFileInput(filename);
            profile_pic.setImageBitmap(BitmapFactory.decodeStream(inputStream));
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_edit:
                Intent intent = new Intent(this, AddOrEditProfileActivity.class);
                intent.putExtra("id", profile.getPid());
                startActivity(intent);
                finish();
                return true;
            case R.id.profile_delete:
                profileDao.delete(profile);
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
