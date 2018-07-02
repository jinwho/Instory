package com.jica.instory;

import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.TextView;

public class ViewProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        TextView tvName = findViewById(R.id.name);
        TextView tvComment = findViewById(R.id.comment);
        RatingBar rating = findViewById(R.id.rating);


    }
}
