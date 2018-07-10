package com.jica.instory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.entity.Profile;
import com.jica.instory.database.dao.ProfileDao;

import java.io.FileOutputStream;
import java.io.IOException;

public class AddOrEditProfileActivity extends AppCompatActivity {
    //DB
    private ProfileDao profileDao;
    private Profile profile;

    //request code
    static final int REQUEST_IMAGE_CAPTURE = 0;
    static final int SELECT_IMAGE_GALLERY = 1;

    //사진,레이팅바,이름,한줄평,버튼
    private ImageView profile_pic;
    private Bitmap profile_photo = null;
    private RatingBar ratingBar;
    private TextView name, comment;

    private long insertedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit_profile);
        profileDao = AppDatabase.getInstance(this).profileDao();

        //뒤로 가기 버튼
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);

        //view 가져오기
        ratingBar = findViewById(R.id.ratingBar);
        name = findViewById(R.id.name);
        comment = findViewById(R.id.comment);
        profile_pic = findViewById(R.id.profile_pic);

        //수정하기 위해 호출되었다면 extra 데이터가 존재하므로 해당 프로필 객체를 불러온다.
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            Integer id = intent.getIntExtra("id", -1);
            //check if id has profile pic
            profile = profileDao.get(id);
            ratingBar.setRating(profile.getRating());
            name.setText(profile.getName());
            comment.setText(profile.getComment());
        } else {
            profile = new Profile();
        }
    }

    //저장 버튼 클릭
    void SaveProfile() {
        //view 에서 값을 얻어와 프로필 객체에 삽입
        profile.setRating((int) ratingBar.getRating());
        profile.setName(name.getText().toString());
        profile.setComment(comment.getText().toString());

        //pid는 수정시에만 존재한다(왜냐면 autoGenerate 때문에)
        if (profile.getPid() != null) {
            //프로필이 존재하면 업데이트
            profileDao.update(profile);
            insertedID = profile.getPid();
        } else {
            //아니라면 추가
            insertedID = profileDao.insert(profile);
        }

        //사진이 설정되었다면 저장 한다.
        if (profile_photo != null) {
            String filename = String.valueOf(insertedID);
            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                profile_photo.compress(Bitmap.CompressFormat.PNG, 30, outputStream);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_addedit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_save:
                SaveProfile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //사진공간이 클릭 되었을 때
    public void onClickPhoto(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddOrEditProfileActivity.this);
        builder.setTitle(R.string.get_photo).setItems(R.array.photo_choice, (dialog, which) -> {
            switch (which) {
                //카메라
                case REQUEST_IMAGE_CAPTURE:
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
                    return;
                //갤러리
                case SELECT_IMAGE_GALLERY:
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, SELECT_IMAGE_GALLERY);
                    return;
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = null;
                    if (extras != null) {
                        imageBitmap = (Bitmap) extras.get("data");
                    }
                    profile_pic.setImageBitmap(imageBitmap);
                    profile_photo = imageBitmap;
                }
                return;
            case SELECT_IMAGE_GALLERY:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    profile_pic.setImageBitmap(bitmap);
                    profile_photo = bitmap;
                }
                return;
        }
    }
}
