package com.jica.instory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jica.instory.adapter.NoteAdapter;
import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.dao.NoteDao;
import com.jica.instory.database.entity.Note;
import com.jica.instory.database.entity.Profile;
import com.jica.instory.database.dao.ProfileDao;
import com.jica.instory.manager.MyFileManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfileActivity extends AppCompatActivity implements View.OnClickListener{
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
    CircleImageView profile_pic;
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
    @BindView(R.id.menu)
    ImageView menu;
    @BindView(R.id.add_note)
    ImageView add_note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.enter,R.anim.exit);
        setContentView(R.layout.activity_profile_view);
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

        //get photo from profile file name, and set pic only if it's exist
        Bitmap bitmap = MyFileManager.getInstance().loadImage(this,profile.getFilename());
        if (bitmap != null) profile_pic.setImageBitmap(bitmap);

        //set click listeners
        phone.setOnClickListener(this);
        email.setOnClickListener(this);
        birthday.setOnClickListener(this);
        address.setOnClickListener(this);

        //buttons
        back.setOnClickListener(v -> onBackPressed());
        menu.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.choose_menu);
            builder.setItems(R.array.main_menu, (dialog, which) -> {
                switch (which) {
                    //수정
                    case 0:
                        intent = new Intent(getApplicationContext(), NewProfileActivity.class);
                        intent.putExtra("id", profile.getPid());
                        startActivity(intent);
                        finish();
                        break;
                    //삭제
                    case 1:
                        //how to ask here??
                        profileDao.delete(profile);
                        finish();
                        break;
                }
            });
            builder.create().show();
        });


        add_note.setOnClickListener(view -> {
            Note note = new Note();
            note.setPid(profile.getPid());

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.new_memo);

            final EditText contents = new EditText(this);
            contents.setHint(R.string.note_contents);

            builder.setView(contents);

            builder.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                String getTime = sdf.format(date);
                note.setDate(getTime);
                note.setContent(contents.getText().toString());
                note.setNid((int) noteDao.insert(note));
                notes.add(note);
                noteAdapter.notifyItemInserted(noteAdapter.getItemCount());
            });
            builder.setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
            builder.create().show();
        });
    }

    //handle 4 image button clicks
    @Override
    public void onClick(View v) {
        String data;
        switch (v.getId()) {
            case R.id.phone_img :
                data = profile.getPhone();
                if ( !data.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+data));
                    startActivity(intent);
                }
                break;
            case R.id.email_img :
                data = profile.getEmail();
                if ( !data.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:"+data));
                    startActivity(intent);
                }
                break;
            case R.id.birthday_img :
                data = profile.getBirthday();
                if ( !data.isEmpty()) {
                    //오늘 날짜를 생일과 비교해서 남은 일을 알려준다.
                    Toast.makeText(this, "생일 : " + data, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.address_img :
                data = profile.getAddress();
                if ( !data.isEmpty()) {
                    Toast.makeText(this, "주소 : " + data, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left2right,R.anim.right2left);
    }
}
