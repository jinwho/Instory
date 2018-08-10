package com.jica.instory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jica.instory.adapter.BandAdapter;
import com.jica.instory.database.AppDatabase;
import com.jica.instory.database.dao.BandDao;
import com.jica.instory.database.entity.Band;
import com.jica.instory.listener.OnGroupClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupActivity extends AppCompatActivity implements View.OnClickListener{
    private BandDao bandDao = AppDatabase.getInstance(this).bandDao();
    private List<Band> bands;
    private BandAdapter bandAdapter;

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
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        rv.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        // 호출 액티비티가 result를 원한다면 선택모드로 바꿔서 보여준다.
        OnGroupClickListener groupClick = null;
        //그룹 선택 모드
        if (getCallingActivity() != null) {
            //로고 텍스트를 "그룹관리" 에서 "그룹선택" 으로 바꾸기
            logo_text.setText(R.string.group_select);
            // 어댑터에서 그룹을 클릭했을 때 결과를 받을 수 있게 리스너를 할당한다
            groupClick = bid -> {
                //send bid back
                Intent resultIntent = new Intent();
                resultIntent.putExtra("bid", bid);
                setResult(RESULT_OK,resultIntent);
                finish();
            };
        }

        //get db
        bands = bandDao.getAll();
        bandAdapter = new BandAdapter(this,groupClick);
        bandAdapter.setBands(bands);
        rv.setAdapter(bandAdapter);

        add_group.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back :
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.add_group :
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
                break;
        }
    }
}
