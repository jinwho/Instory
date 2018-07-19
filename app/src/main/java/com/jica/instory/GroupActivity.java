package com.jica.instory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jica.instory.adapter.BandAdapter;
import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.dao.BandDao;
import com.jica.instory.database.entity.Band;
import com.jica.instory.listener.OnGroupClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupActivity extends AppCompatActivity {
    private BandDao bandDao = AppDatabase.getInstance(this).bandDao();
    private List<Band> bands;
    private BandAdapter bandAdapter;
    private boolean isSelectionMod = false;

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.add_group)
    Button add_group;
    @BindView(R.id.logo_text)
    TextView logo_text;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);


        // 여기서는 클릭한 그룹 bid를 인텐트에 넣고  를 통해 결과를 반환 한다.

        // 호출 액티비티가 result를 원한다면 선택모드로 바꿔서 보여준다.
        OnGroupClickListener groupClick;
        if (getCallingActivity() != null) {
            isSelectionMod = true;
            //그룹 선택 모드
            // 그룹추가 버튼 없애기
            add_group.setVisibility(View.GONE);
            //로고 텍스트를 "그룹관리" 에서 "그룹선택" 으로 바꾸기
            logo_text.setText(R.string.menu_select_group);
            // 어댑터에서 그룹을 클릭했을 때 결과를 받을 수 있게 리스너를 할당한다
            groupClick = bid -> {
                //send bid back
                setResult(bid);
                Toast.makeText(this, "그룹이 선택되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            };
        } else {
            //그룹 관리 모드
            add_group.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupActivity.this);
                builder.setTitle(R.string.new_group);
                // Set up the input
                final EditText group_name = new EditText(GroupActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                group_name.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(group_name);
                builder.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                    Band band = new Band();
                    band.setName(group_name.getText().toString());

                    band.setBid((int) bandDao.insert(band));
                    bands.add(band);
                    bandAdapter.notifyItemInserted(bandAdapter.getItemCount());
                });
                builder.setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.dismiss());
                builder.create().show();
            });
            //어댑터에 리스너를 null로 할당한다.
            groupClick = null;
        }

        //get db
        bands = bandDao.getAll();
        bandAdapter = new BandAdapter(this,groupClick);
        rv.setAdapter(bandAdapter);
        bandAdapter.setBands(bands);

        back.setOnClickListener(v -> onBackPressed());


    }

    @Override
    public void onBackPressed() {
        if (isSelectionMod) setResult(-1);
        finish();
        overridePendingTransition(R.anim.left2right, R.anim.right2left);
    }
}
