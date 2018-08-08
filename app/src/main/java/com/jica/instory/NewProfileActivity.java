package com.jica.instory;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.entity.Profile;
import com.jica.instory.database.dao.ProfileDao;
import com.jica.instory.manager.ImageFileManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewProfileActivity extends AppCompatActivity {

    private final static int REQUEST_IMAGE_CAPTURE = 200;
    private final static int SELECT_FROM_GALLERY = 201;

    //DB
    private ProfileDao profileDao = AppDatabase.getInstance(this).profileDao();
    private Profile profile;

    //사진 데이터
    private Bitmap profile_photo = null;
    private boolean photo_changed = false;

    //views
    @BindView(R.id.profile_pic)
    CircleImageView profile_pic;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.comment)
    EditText comment;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.birthday)
    TextView birthday;
    @BindView(R.id.address)
    EditText address;

    //toolbar buttons
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.logo_text)
    TextView logo_text;
    @BindView(R.id.ok)
    TextView ok;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_new);
        ButterKnife.bind(this);

        //수정하기 위해 호출되었다면 id를 얻어 프로필 객체를 불러온다.
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            //"프로필 추가" -> "프로필 수정"
            logo_text.setText(R.string.profile_edit);

            //프로필 가져오기
            Integer id = intent.getIntExtra("id", -1);
            profile = profileDao.get(id);

            //프로필 view 세팅
            ratingBar.setRating(profile.getRating());
            name.setText(profile.getName());
            comment.setText(profile.getComment());
            phone.setText(profile.getPhone());
            email.setText(profile.getEmail());
            if (profile.getBirthday() != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String birthday_string = format.format(profile.getBirthday());
                birthday.setText(birthday_string);
            }
            address.setText(profile.getAddress());

            //이미지파일이 존재한다면 가져온다.
            Bitmap bitmap = ImageFileManager.getInstance().loadImage(this, profile.getFilename());
            if (bitmap != null) profile_pic.setImageBitmap(bitmap);

        } else {
            //수정 모드가 아니라면 새 프로필 생성
            profile = new Profile();
        }

        //버튼 리스너
        back.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        profile_pic.setOnClickListener(v -> takePhoto());
        birthday.setOnClickListener(v -> SetCalendar());
        ok.setOnClickListener(v -> SaveProfile());
    }

    //저장 버튼 클릭
    void SaveProfile() {
        //view 에서 값을 얻어와 프로필 객체에 삽입
        profile.setRating((int) ratingBar.getRating());
        profile.setName(name.getText().toString());
        profile.setComment(comment.getText().toString());
        profile.setPhone(phone.getText().toString());
        profile.setEmail(email.getText().toString());
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
            ImageFileManager.getInstance().saveImage(profile_photo, this, profile.getFilename());
        }
        //pid는 수정시에만 존재한다(autoGenerate 때문에)
        if (profile.getPid() != null) {
            //업데이트
            profileDao.update(profile);
            Toast.makeText(this, "프로필이 변경 되었습니다.", Toast.LENGTH_SHORT).show();
        } else {
            //추가
            profileDao.insert(profile);
            Toast.makeText(this, "프로필이 저장 되었습니다.", Toast.LENGTH_SHORT).show();
        }
        // 메인에서 호출되었으면 프로필이 생성 된 것
        // 프로필뷰에서 호출되었으면 업데이트 되었음
        setResult(RESULT_OK);
        finish();
    }

    //생일 텍스트뷰 클릭
    void SetCalendar() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(NewProfileActivity.this,
                (view, year, monthOfYear, dayOfMonth) -> {

                    Calendar picked_birthday = Calendar.getInstance();
                    picked_birthday.set(year,monthOfYear,dayOfMonth);
                    profile.setBirthday(picked_birthday.getTime());

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String birthday_string = format.format(profile.getBirthday());
                    birthday.setText(birthday_string);

                }, mYear, mMonth, mDay);
        dpd.show();
    }

    //사진공간이 클릭 되었을 때
    public void takePhoto() {
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
        int THUMBSIZE = 256;
        //이미지를 가져온 경우에만
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        bitmap = ThumbnailUtils.extractThumbnail((Bitmap) extras.get("data"), THUMBSIZE, THUMBSIZE);
                    }
                    break;
                case SELECT_FROM_GALLERY:
                    Uri uri = data.getData();
                    try {
                        bitmap = ThumbnailUtils.extractThumbnail(MediaStore.Images.Media.getBitmap(getContentResolver(), uri), THUMBSIZE, THUMBSIZE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            photo_changed = true;
            profile_photo = bitmap;
            profile_pic.setImageBitmap(profile_photo);
        }
    }
}
