package com.example.bucket;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



public class PWokayActivity extends AppCompatActivity{

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwset_okaypage);

        // 로그인 페이지로 이동
        Button goto_login = (Button) findViewById(R.id.btn_gotologin);
        goto_login.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
    }
}
