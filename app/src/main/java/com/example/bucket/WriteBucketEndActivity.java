package com.example.bucket;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



public class WriteBucketEndActivity extends AppCompatActivity {


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writebucket_endpage);

        // 뒤로 가기 버튼
        ImageButton button_back = (ImageButton) findViewById(R.id.btn_back);
        button_back.setOnClickListener(view -> {
            finish();
            //Intent intent = new Intent(getApplicationContext(), WriteBucket4Activity.class);
            //startActivity(intent);
        });

        // 버킷리스트 작성 완료 (나의 버킷 조회 리스트 화면으로 이동)
        Button button_finish = (Button) findViewById(R.id.btn_writebucket_end);
        button_finish.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MyBKListActivity.class);
            startActivity(intent);
        });

    }
}