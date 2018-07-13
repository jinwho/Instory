package com.jica.instory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
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
import android.widget.Toast;

import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.entity.Profile;
import com.jica.instory.database.dao.ProfileDao;
import com.jica.instory.manager.MyFileManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewProfileActivity extends AppCompatActivity {
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
    @BindView(R.id.logo_text) TextView logo_text;
    @BindView(R.id.ok) TextView ok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_new);
        ButterKnife.bind(this);

        //수정하기 위해 호출되었다면 id를 얻어 프로필 객체를 불러온다.
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            Integer id = intent.getIntExtra("id", -1);
            profile = profileDao.get(id);
            //"프로필 추가" -> "프로필 수정"
            logo_text.setText(R.string.profile_edit);
            //set view
            ratingBar.setRating(profile.getRating());
            name.setText(profile.getName());
            comment.setText(profile.getComment());
            phone.setText(profile.getPhone());
            email.setText(profile.getEmail());
            birthday.setText(profile.getBirthday());
            address.setText(profile.getAddress());

            //수정할 때 이미지파일이 존재한다면 가져온다.
            Bitmap bitmap = MyFileManager.getInstance().loadImage(this,profile.getFilename());
            if (bitmap != null) profile_pic.setImageBitmap(bitmap);

        } else {
            profile = new Profile();
        }

        //back button
        back.setOnClickListener(v -> {
            finish();
        });
        //ok button
        ok.setOnClickListener(v -> {
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


        //사진이 변경되었다면 저장한다.
        if (photo_changed) {

            //파일이름이 없다면 처음 세팅하는 것이므로 유니크한 이름을 할당한다.
            if (profile.getFilename() == null) {
                //epoch? 부터 현재까지의 시간을 숫자로 반환
                String filename = String.valueOf(Calendar.getInstance().getTimeInMillis());
                profile.setFilename(filename);
            }

            //파일 저장함
            /*FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(profile.getFilename(), Context.MODE_PRIVATE);
                //photo has already resized, although small size would be better..
                profile_photo.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            MyFileManager.getInstance().saveImage(profile_photo,this,profile.getFilename());
        }

        //pid는 수정시에만 존재한다(autoGenerate 때문에)
        if (profile.getPid() != null) {
            //업데이트
            profileDao.update(profile);
        } else {
            //추가
            profileDao.insert(profile);
        }
        finish();
    }

    //사진공간이 클릭 되었을 때
    public void onClickPhoto(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewProfileActivity.this);
        builder.setTitle(R.string.photo_menu).setItems(R.array.photo_choice, (dialog, which) -> {
            Intent intent;
            switch (which) {
                //카메라
                case 0:
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    break;
                //갤러리
                case 1:
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, SELECT_FROM_GALLERY);
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
                    bitmap = ThumbnailUtils.extractThumbnail((Bitmap) extras.get("data"),100,100);
                }
                break;
            case SELECT_FROM_GALLERY:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    try {
                        bitmap = ThumbnailUtils.extractThumbnail(MediaStore.Images.Media.getBitmap(getContentResolver(), uri),100, 100);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        profile_photo = bitmap;
        profile_pic.setImageBitmap(profile_photo);
        photo_changed = true;
    }

}
