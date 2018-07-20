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
import com.jica.instory.database.dao.BandDao;
import com.jica.instory.database.dao.NoteDao;
import com.jica.instory.database.dao.ProfileDao;
import com.jica.instory.database.entity.Band;
import com.jica.instory.database.entity.Note;
import com.jica.instory.database.entity.Profile;
import com.jica.instory.manager.MyFileManager;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfileActivity extends AppCompatActivity implements View.OnClickListener {
    //DB profile
    private ProfileDao profileDao = AppDatabase.getInstance(this).profileDao();
    private Profile profile;
    //DB note
    private NoteDao noteDao = AppDatabase.getInstance(this).noteDao();
    private List<Note> notes;
    private NoteAdapter noteAdapter;
    //DB group
    private BandDao bandDao = AppDatabase.getInstance(this).bandDao();
    private Band band;

    private Intent intent;
    final static int GROUP_SELECT_REQUEST = 100;

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
    @BindView(R.id.group)
    TextView group;
    @BindView(R.id.menu)
    ImageView menu;
    @BindView(R.id.add_note)
    ImageView add_note;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.enter, R.anim.exit);
        setContentView(R.layout.activity_profile_view);
        ButterKnife.bind(this);

        intent = getIntent();
        Integer pid = intent.getIntExtra("id", -1);

        //DB 검색
        profile = profileDao.get(pid);
        notes = noteDao.ownBy(pid);

        //bid가 있으면 그룹이름을 보여줌
        Integer bid = profile.getBid();
        if (bid != null) {
            band = bandDao.get(bid);
            group.setText(band.getName());
        }

        //set profile view
        ratingBar.setRating(profile.getRating());
        name.setText(profile.getName());
        String tComment = profile.getComment();
        if (tComment.isEmpty()) {
            comment.setVisibility(View.INVISIBLE);
        } else {
            comment.setText(tComment);
        }

        //set note view
        noteAdapter = new NoteAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(noteAdapter);
        noteAdapter.setNotes(notes);

        //get photo from profile file name, and set pic only if it's exist
        Bitmap bitmap = MyFileManager.getInstance().loadImage(this, profile.getFilename());
        if (bitmap != null) profile_pic.setImageBitmap(bitmap);

        //set click listeners
        phone.setOnClickListener(this);
        email.setOnClickListener(this);
        birthday.setOnClickListener(this);
        address.setOnClickListener(this);

        //if data is empty then make it gone
        if (profile.getPhone().isEmpty()) phone.setVisibility(View.GONE);
        if (profile.getEmail().isEmpty()) email.setVisibility(View.GONE);
        if (profile.getYear() == 0) birthday.setVisibility(View.GONE);
        if (profile.getAddress().isEmpty()) address.setVisibility(View.GONE);

        //buttons
        back.setOnClickListener(v -> onBackPressed());
        menu.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.choose_menu);
            builder.setItems(R.array.view_menu, (dialog, which) -> {
                switch (which) {
                    //그룹 선택
                    case 0:
                        Intent intent = new Intent(this, GroupActivity.class);
                        startActivityForResult(intent, GROUP_SELECT_REQUEST);
                        break;
                    //수정
                    case 1:
                        intent = new Intent(getApplicationContext(), NewProfileActivity.class);
                        intent.putExtra("id", profile.getPid());
                        startActivity(intent);
                        finish();
                        break;
                    //삭제
                    case 2:
                        AlertDialog.Builder YorN = new AlertDialog.Builder(this);
                        YorN.setTitle(R.string.really_delete);
                        YorN.setPositiveButton(android.R.string.yes, (sub_dialog, sub_which) -> {
                            //혹시 프사가 있으면 프사도 삭제함.
                            if (profile.getFilename() != null) {
                                deleteFile(profile.getFilename());
                            }
                            profileDao.delete(profile);
                            Toast.makeText(getApplicationContext(), "프로필이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                        YorN.setNegativeButton(android.R.string.no, (sub_dialog, sub_which) -> sub_dialog.dismiss());
                        YorN.create().show();
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
                Calendar calendar = Calendar.getInstance();
                String today = getString(R.string.calendar_format, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
                note.setDate(today);
                note.setContent(contents.getText().toString());
                note.setNid((int) noteDao.insert(note));
                notes.add(note);
                noteAdapter.notifyItemInserted(noteAdapter.getItemCount());
            });
            builder.setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
            builder.create().show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GROUP_SELECT_REQUEST) {
            if (resultCode != -1) {
                //resultCode에 있는 bid를 받아와서, 로고텍스트를 바꾸고, 프로필 db의 bid를 업데이트 해준다.
                band = bandDao.get(resultCode);
                group.setText(band.getName());
                profile.setBid(resultCode);
                profileDao.update(profile);
            }
        }
    }

    //handle 4 image button clicks
    @Override
    public void onClick(View v) {
        String data;
        switch (v.getId()) {
            case R.id.phone_img:
                data = profile.getPhone();
                if (!data.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data));
                    startActivity(intent);
                }
                break;
            case R.id.email_img:
                data = profile.getEmail();
                if (!data.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + data));
                    startActivity(intent);
                }
                break;
            case R.id.birthday_img:
                if (profile.getYear() != 0) {
                    //오늘 날짜를 생일과 비교해서 남은 일을 알려준다.
                    Calendar today = Calendar.getInstance();
                    Calendar birthday = Calendar.getInstance();
                    birthday.set(today.get(Calendar.YEAR), profile.getMonth(), profile.getDay());
                    Calendar d_day = Calendar.getInstance();
                    d_day.setTimeInMillis(birthday.getTimeInMillis() - today.getTimeInMillis());
                    data = getString(R.string.calendar_format, profile.getYear(), profile.getMonth() + 1, profile.getDay());
                    Toast.makeText(this, data + "\n" + "생일까지 " + (d_day.get(Calendar.DAY_OF_YEAR) - 1) + "일", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.address_img:
                data = profile.getAddress();
                if (!data.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + data));
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left2right, R.anim.right2left);
    }
}
