package com.example.bucket;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;



public class AlarmActivity extends AppCompatActivity {

    TabLayout tabLayout;
    FrameLayout frameLayout;
    LayoutInflater inflater;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_page);

        // 뒤로 가기 버튼
        ImageButton button_back = findViewById(R.id.btn_back);
        button_back.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        });

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        frameLayout = (FrameLayout) findViewById(R.id.layout_alarm);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 첫화면 띄우기
        //getSupportFragmentManager().beginTransaction().add(R.id.layout_alarmquest, new AlarmQuestActivity()).commit();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Intent intent;

                if(position == 0) {
                    inflater.inflate(R.layout.alarm_quest, frameLayout, true);
                } else if(position == 1) {
                    inflater.inflate(R.layout.alarm_bucketlist, frameLayout, true);
                } else if(position == 2) {
                    inflater.inflate(R.layout.alarm_point, frameLayout, true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
