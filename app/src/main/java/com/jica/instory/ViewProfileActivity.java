package com.jica.instory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.Profile;
import com.jica.instory.database.ProfileDao;

public class ViewProfileActivity extends AppCompatActivity {
    private ProfileDao profileDao = AppDatabase.getInstance(this).profileDao();
    private Profile profile;
    private int position;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        TextView tvName = findViewById(R.id.name);
        TextView tvComment = findViewById(R.id.comment);
        RatingBar rating = findViewById(R.id.ratingBar);

        //set up back button
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // position을 전달받아 해당 id를 가진 프로필을 DB로 검색한 후 뷰를 통해 보여준다.
        Intent intent = getIntent();
        position = intent.getIntExtra("position",0);
        profile = profileDao.getProfileById(position);

        tvName.setText(profile.getName());
        tvComment.setText(profile.getComment());
        rating.setRating(profile.getRating());


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
            case R.id.modify:
                Intent intent = new Intent(this,AddOrEditProfileActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
                finish();
                return true;
            case R.id.delete:
                profileDao.delete(profile);
                finish();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
