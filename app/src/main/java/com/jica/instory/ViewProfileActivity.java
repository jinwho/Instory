package com.jica.instory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jica.instory.adapter.NoteAdapter;
import com.jica.instory.adapter.ProfileAdapter;
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

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class ViewProfileActivity extends AppCompatActivity{
    //DB profile
    private ProfileDao profileDao = AppDatabase.getInstance(this).profileDao();
    private Profile profile;
    //DB note
    private NoteDao noteDao = AppDatabase.getInstance(this).noteDao();
    private List<Note> notes;
    private NoteAdapter noteAdapter;

    private Intent intent;

    //views
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.comment)
    TextView comment;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.profile_pic)
    ImageView profile_pic;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    //icons
    @BindView(R.id.phone_img)
    ImageView phone;
    @BindView(R.id.email_img)
    ImageView email;
    @BindView(R.id.birthday_img)
    ImageView birthday;
    @BindView(R.id.address_img)
    ImageView address;

    //buttons
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.del)
    ImageView del;
    @BindView(R.id.edit)
    ImageView edit;
    @BindView(R.id.add_note)
    ImageView add_note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        ButterKnife.bind(this);

        intent = getIntent();
        Integer pid = intent.getIntExtra("id", -1);

        //DB 검색
        profile = profileDao.get(pid);
        notes = noteDao.ownBy(pid);

        //set profile view
        ratingBar.setRating(profile.getRating());
        name.setText(profile.getName());
        comment.setText(profile.getComment());

        //set note view
        noteAdapter = new NoteAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(noteAdapter);
        noteAdapter.setNotes(notes);

        /*
        get photo from profile file name
         */



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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.delete_message);
            builder.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                profileDao.delete(profile);
                finish();
            });
            builder.setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
            builder.create().show();
        });
        edit.setOnClickListener(v -> {
            intent = new Intent(this, AddOrEditProfileActivity.class);
            intent.putExtra("id", profile.getPid());
            startActivity(intent);
            finish();
        });


        add_note.setOnClickListener(view -> {
            Note note = new Note();
            note.setPid(profile.getPid());

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("새 메모");
            // Set up the input
            final EditText title = new EditText(this);
            //final EditText contents = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            title.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(title);
            //builder.setAdapter()
            builder.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                note.setTitle(title.getText().toString());
                note.setContent("this is test note");
                dialogInterface.dismiss();
                noteDao.insert(note);
                notes.add(note);
                noteAdapter.notifyItemInserted(noteAdapter.getItemCount());
            });
            builder.setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
            builder.create().show();
        });
    }
}
