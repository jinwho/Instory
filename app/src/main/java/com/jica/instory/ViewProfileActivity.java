package com.jica.instory;

import android.app.ActionBar;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.Profile;
import com.jica.instory.database.ProfileDao;

public class ViewProfileActivity extends AppCompatActivity {
    private ProfileDao profileDao;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        //set up back button
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        TextView tvName = findViewById(R.id.name);
        TextView tvComment = findViewById(R.id.comment);
        RatingBar rating = findViewById(R.id.rating);

        // position을 전달받아 해당 id를 가진 프로필을 DB로 검색한 후 뷰를 통해 보여준다.
        Intent intent = getIntent();
        int i = intent.getIntExtra("position",0);
        profileDao = AppDatabase.getInstance(this).profileDao();
        Profile p = profileDao.getByID(i+1);

        tvName.setText(p.getName());
        tvComment.setText(p.getComment());
        rating.setRating(p.getRating());


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
            case R.id.edit:
                return true;
            case R.id.delete:
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
