package com.jica.instory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.Profile;
import com.jica.instory.database.ProfileDao;

public class AddProfileActivity extends AppCompatActivity {

    ProfileDao profileDao;
    RatingBar ratingBar;
    TextView name,comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile);

        profileDao = AppDatabase.getInstance(getApplicationContext()).profileDao();
        ratingBar = findViewById(R.id.ratingBar);
        name = findViewById(R.id.name);
        comment = findViewById(R.id.comment);
    }


    public void addProfile(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Profile p = new Profile();
                            p.setRating(ratingBar.getNumStars());
                            p.setName(name.getText().toString());
                            p.setComment(comment.getText().toString());
                    profileDao.insertAll(p);
                }
            });
            finish();
    }
}
