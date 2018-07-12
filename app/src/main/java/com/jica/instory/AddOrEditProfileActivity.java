package com.jica.instory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.entity.Profile;
import com.jica.instory.database.dao.ProfileDao;

import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddOrEditProfileActivity extends AppCompatActivity {
    //DB
    private ProfileDao profileDao = AppDatabase.getInstance(this).profileDao();
    private Profile profile;

    //request code
    static final int REQUEST_IMAGE_CAPTURE = 0;
    static final int SELECT_FROM_GALLERY = 1;

    //사진 데이터
    private Bitmap profile_photo = null;
    private boolean photo_changed = false;

    //views
    @BindView(R.id.profile_pic)  ImageView profile_pic;
    @BindView(R.id.ratingBar) RatingBar ratingBar;
    @BindView(R.id.name) EditText name;
    @BindView(R.id.comment) EditText comment;
    @BindView(R.id.phone) EditText  phone;
    @BindView(R.id.email) EditText email;
    @BindView(R.id.birthday) TextView birthday;
    @BindView(R.id.address) EditText address;

    //toolbar buttons
    @BindView(R.id.back) ImageView back;
    @BindView(R.id.toolbartext) TextView toolbartext;
    @BindView(R.id.save) ImageView save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit_profile);
        ButterKnife.bind(this);

        //수정하기 위해 호출되었다면 id를 얻어 프로필 객체를 불러온다.
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            Integer id = intent.getIntExtra("id", -1);
            profile = profileDao.get(id);
            //set view
            ratingBar.setRating(profile.getRating());
            name.setText(profile.getName());
            comment.setText(profile.getComment());
            phone.setText(profile.getPhone());
            email.setText(profile.getEmail());
            birthday.setText(profile.getBirthday());
            address.setText(profile.getAddress());

            //"프로필 추가" -> "프로필 수정"
            toolbartext.setText(R.string.profile_edit);
        } else {
            profile = new Profile();
        }

        //back button
        back.setOnClickListener(v -> {
            finish();
        });
        //save button
        save.setOnClickListener(v -> {
            SaveProfile();
        });
    }

    //저장 버튼 클릭
    void SaveProfile() {
        //view 에서 값을 얻어와 프로필 객체에 삽입
        profile.setRating((int) ratingBar.getRating());
        profile.setName(name.getText().toString());
        profile.setComment(comment.getText().toString());
        profile.setPhone(phone.getText().toString());
        profile.setEmail(email.getText().toString());
        profile.setBirthday(birthday.getText().toString());
        profile.setAddress(address.getText().toString());

        //pid는 수정시에만 존재한다(왜냐면 autoGenerate 때문에)
        long insertedID;
        if (profile.getPid() != null) {
            //업데이트
            profileDao.update(profile);
            insertedID = profile.getPid();
        } else {
            //추가
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

    //사진공간이 클릭 되었을 때
    public void onClickPhoto(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddOrEditProfileActivity.this);
        builder.setTitle(R.string.get_photo).setItems(R.array.photo_choice, (dialog, which) -> {
            switch (which) {
                //카메라
                case REQUEST_IMAGE_CAPTURE:
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
                    break;
                //갤러리
                case SELECT_FROM_GALLERY:
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, SELECT_FROM_GALLERY);
                    break;
            }
        });
        builder.create().show();
    }

    //사진을 가져왔을 때
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();

                    if (extras != null) {
                        bitmap = (Bitmap) extras.get("data");
                    }
                    profile_pic.setImageBitmap(bitmap);
                    profile_photo = bitmap;
                }
                break;
            case SELECT_FROM_GALLERY:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    profile_pic.setImageBitmap(bitmap);
                    profile_photo = bitmap;
                }
                break;
        }
    }

}
