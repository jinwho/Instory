package com.jica.instory;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
    //DB객체
    private ProfileDao profileDao = AppDatabase.getInstance(this).profileDao();
    //프로필
    private Profile profile;
    //프로필 ID
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        //view 가져오기
        TextView tvName = findViewById(R.id.name);
        TextView tvComment = findViewById(R.id.comment);
        RatingBar rating = findViewById(R.id.ratingBar);

        //뒤로가기 버튼 만들기
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // 보내온 id를 가진 프로필을 뷰를 통해 보여준다.
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        //DB id 검색
        profile = profileDao.getById(id);
        //view 값 할당
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
            case R.id.listedit:
                Intent intent = new Intent(this, AddOrEditProfileActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
                return true;
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.delete_message)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                profileDao.deleteAll(profile);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do nothing
                            }
                        }).create().show();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
