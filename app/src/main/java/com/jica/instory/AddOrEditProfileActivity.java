package com.jica.instory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.Profile;
import com.jica.instory.database.ProfileDao;

public class AddOrEditProfileActivity extends AppCompatActivity {

    private ImageView profile_pic;
    private ProfileDao profileDao;
    private RatingBar ratingBar;
    private TextView name, comment;
    private Button button;

    private static final int CAPTURE_PHOTO_FROM_CAMERA = 1;
    private static final int SELECT_IMAGE_FROM_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit_profile);

        //set up back button
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        profileDao = AppDatabase.getInstance(getApplicationContext()).profileDao();

        ratingBar = findViewById(R.id.ratingBar);
        name = findViewById(R.id.name);
        comment = findViewById(R.id.comment);
        button = findViewById(R.id.button);

        profile_pic = findViewById(R.id.profile_pic);
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //만약 extra 데이터가 있으면(수정하기 위해 호출되었다면) 해당 프로필 객체를 불러온다.
        Intent intent = getIntent();
        if(intent.hasExtra("position")) {
            int position = intent.getIntExtra("position",-1);
            Profile profile = profileDao.getProfileById(position);
            ratingBar.setRating(profile.getRating());
            name.setText(profile.getName());
            comment.setText(profile.getComment());

        } else {
            //do nothing

        }

        //save to db
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile p = new Profile();
                String fName = name.getText().toString();
                if (fName.equals("")) {
                    Toast.makeText(getApplicationContext(), "name field is empty", Toast.LENGTH_SHORT).show();
                    return;//do not add unless there is name;
                }
                p.setRating((int)ratingBar.getRating());
                p.setName(fName);
                p.setComment(comment.getText().toString());

                profileDao.insertAll(p);
                //프로필 id를 얻어서 숫자 폴더안에 profile.jpg로 사진을 저장한다.
                //사진이 없다면 그대로 냅둔다.
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //사진을 가져오면 프로필 사진에 사진을 보여준다.
    }
}
