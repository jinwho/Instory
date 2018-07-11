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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.dao.NoteDao;
import com.jica.instory.database.entity.Note;
import com.jica.instory.database.entity.Profile;
import com.jica.instory.database.dao.ProfileDao;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewProfileActivity extends AppCompatActivity{
    //DB
    private ProfileDao profileDao = AppDatabase.getInstance(this).profileDao();
    private Profile profile;
    private NoteDao noteDao = AppDatabase.getInstance(this).noteDao();
    private List<Note> notes;
    private Intent intent;

    //views
    @BindView(R.id.name) TextView name;
    @BindView(R.id.comment) TextView comment;
    @BindView(R.id.ratingBar) RatingBar ratingBar;
    @BindView(R.id.profile_pic) ImageView profile_pic;

    //icons
    @BindView(R.id.phone_img) ImageView phone;
    @BindView(R.id.email_img) ImageView email;
    @BindView(R.id.birthday_img) ImageView birthday;
    @BindView(R.id.address_img) ImageView address;

    //buttons
    @BindView(R.id.back) ImageView back;
    @BindView(R.id.del) ImageView del;
    @BindView(R.id.edit) ImageView edit;
    @BindView(R.id.add_note) ImageView add_note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        ButterKnife.bind(this);

        intent = getIntent();
        Integer id = intent.getIntExtra("id", -1);
        //DB 검색
        profile = profileDao.get(id);
        //view 값 할당
        ratingBar.setRating(profile.getRating());
        name.setText(profile.getName());
        comment.setText(profile.getComment());

        // if data exist than make it clickable otherwise make it grey //
        /*
        phone.setText(profile.getPhone());
        email.setText(profile.getEmail());
        birthday.setText(profile.getBirthday());
        address.setText(profile.getAddress());
        */
        phone.setOnClickListener(v -> Toast.makeText(ViewProfileActivity.this, profile.getPhone(), Toast.LENGTH_SHORT).show());
        email.setOnClickListener(v -> Toast.makeText(ViewProfileActivity.this, profile.getEmail(), Toast.LENGTH_SHORT).show());
        birthday.setOnClickListener(v -> Toast.makeText(ViewProfileActivity.this, profile.getBirthday(), Toast.LENGTH_SHORT).show());
        address.setOnClickListener(v -> Toast.makeText(ViewProfileActivity.this, profile.getAddress(), Toast.LENGTH_SHORT).show());


        //buttons
        back.setOnClickListener(v -> finish());
        del.setOnClickListener(v -> {
            profileDao.delete(profile);
            finish();
        });
        edit.setOnClickListener(v -> {
            intent = new Intent(this, AddOrEditProfileActivity.class);
            intent.putExtra("id", profile.getPid());
            startActivity(intent);
            finish();
        });
        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //노틑 추가 화면을 불러온다.
                //제목과 내용값을 얻어온다.
            }
        });
    }
}
