package com.jica.instory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.Profile;
import com.jica.instory.database.ProfileDao;

public class AddOrEditProfileActivity extends AppCompatActivity {

    ProfileDao profileDao;
    RatingBar ratingBar;
    TextView name, comment;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit_profile);

        //set up back button
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        profileDao = AppDatabase.getInstance(this).profileDao();
        ratingBar = findViewById(R.id.ratingBar);
        name = findViewById(R.id.name);
        comment = findViewById(R.id.comment);
        button = findViewById(R.id.button);

        //save to db
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile p = new Profile();
                String fName = name.getText().toString();
                if (fName.equals("")) {
                    Toast.makeText(AddOrEditProfileActivity.this, "name field is empty", Toast.LENGTH_SHORT).show();
                    return;//do not add unless there is name;
                }
                p.setRating((int)ratingBar.getRating());
                p.setName(fName);
                p.setComment(comment.getText().toString());

                profileDao.insertAll(p);
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
}
