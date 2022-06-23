package com.example.bucket;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class WriteBucket1Activity extends AppCompatActivity {

    List<String> Tags = new ArrayList<String>();
    Button goto_nextPage1;
    EditText title, content;  // 제목, 내용 작성
    ToggleButton open, close;  //비공개, 공개
    ToggleButton travel, food, challenge, exercise, develop, diet, hobby, work, language, finance, certificate, health, book, anything;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writebucket_page1);


        // 뒤로 가기 버튼
        ImageButton button_back = (ImageButton) findViewById(R.id.btn_back);
        button_back.setOnClickListener(view -> {
            finish();
        });

        title = (EditText) findViewById(R.id.set_title);    // 제목
        content = (EditText) findViewById(R.id.set_content);   // 내용
        open = (ToggleButton) findViewById(R.id.btn_open);   // 공개버튼
        close = (ToggleButton) findViewById(R.id.btn_close);   // 비공개버튼
        goto_nextPage1 = (Button) findViewById(R.id.btn_writebucket1);  // 다음페이지버튼

        // 해당카테고리 선택
        travel = (ToggleButton) findViewById(R.id.btn_travel);
        food = (ToggleButton) findViewById(R.id.btn_food);
        challenge = (ToggleButton) findViewById(R.id.btn_challenge);
        exercise = (ToggleButton) findViewById(R.id.btn_exercise);
        develop = (ToggleButton) findViewById(R.id.btn_develop);
        diet = (ToggleButton) findViewById(R.id.btn_diet);
        hobby = (ToggleButton) findViewById(R.id.btn_hobby);
        work = (ToggleButton) findViewById(R.id.btn_work);
        language = (ToggleButton) findViewById(R.id.btn_language);
        finance = (ToggleButton) findViewById(R.id.btn_finance);
        certificate = (ToggleButton) findViewById(R.id.btn_certificate);
        health = (ToggleButton) findViewById(R.id.btn_health);
        book = (ToggleButton) findViewById(R.id.btn_book);
        anything = (ToggleButton) findViewById(R.id.btn_anything);


        // 공개 비공개 여부 선택
        open.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                open.setTextColor(Color.parseColor("#FFFFFF"));
                open.setBackgroundResource(R.drawable.button_shape_on);
                close.setTextColor(Color.parseColor("#191970"));
                close.setBackgroundResource(R.drawable.button_shape);
            }
        });

        close.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                close.setTextColor(Color.parseColor("#FFFFFF"));
                close.setBackgroundResource(R.drawable.button_shape_on);
                open.setTextColor(Color.parseColor("#191970"));
                open.setBackgroundResource(R.drawable.button_shape);
            }
        });


        // 카테고리 선택
        travel.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                travel.setTextColor(Color.parseColor("#FFFFFF"));
                travel.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("여행");
                System.out.println("list : " +  Tags);
            } else {
                travel.setTextColor(Color.parseColor("#808080"));
                travel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("여행");
                System.out.println("list : " +  Tags);
            }
        });

        food.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                food.setTextColor(Color.parseColor("#FFFFFF"));
                food.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("맛집");
                System.out.println("list : " +  Tags);
            } else {
                food.setTextColor(Color.parseColor("#808080"));
                food.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("맛집");
                System.out.println("list : " +  Tags);
            }
        });

        challenge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                challenge.setTextColor(Color.parseColor("#FFFFFF"));
                challenge.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("도전");
                System.out.println("list : " +  Tags);
            } else {
                challenge.setTextColor(Color.parseColor("#808080"));
                challenge.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("도전");
                System.out.println("list : " +  Tags);
            }
        });

        exercise.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                exercise.setTextColor(Color.parseColor("#FFFFFF"));
                exercise.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("운동");
                System.out.println("list : " +  Tags);
            } else {
                exercise.setTextColor(Color.parseColor("#808080"));
                exercise.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("운동");
                System.out.println("list : " +  Tags);
            }
        });

        develop.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                develop.setTextColor(Color.parseColor("#FFFFFF"));
                develop.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("자기계발");
                System.out.println("list : " +  Tags);
            } else {
                develop.setTextColor(Color.parseColor("#808080"));
                develop.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("자기계발");
                System.out.println("list : " +  Tags);
            }
        });

        diet.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                diet.setTextColor(Color.parseColor("#FFFFFF"));
                diet.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("자기관리");
                System.out.println("list : " +  Tags);
            } else {
                diet.setTextColor(Color.parseColor("#808080"));
                diet.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("자기관리");
                System.out.println("list : " +  Tags);
            }
        });

        hobby.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                hobby.setTextColor(Color.parseColor("#FFFFFF"));
                hobby.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("취미");
                System.out.println("list : " +  Tags);
            } else {
                hobby.setTextColor(Color.parseColor("#808080"));
                hobby.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("취미");
                System.out.println("list : " +  Tags);
            }
        });

        work.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                work.setTextColor(Color.parseColor("#FFFFFF"));
                work.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("입시/취업");
                System.out.println("list : " +  Tags);
            } else {
                work.setTextColor(Color.parseColor("#808080"));
                work.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("입시/취업");
                System.out.println("list : " +  Tags);
            }
        });

        language.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                language.setTextColor(Color.parseColor("#FFFFFF"));
                language.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("외국어");
                System.out.println("list : " +  Tags);
            } else {
                language.setTextColor(Color.parseColor("#808080"));
                language.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("외국어");
                System.out.println("list : " +  Tags);
            }
        });

        finance.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                finance.setTextColor(Color.parseColor("#FFFFFF"));
                finance.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("재테크");
                System.out.println("list : " +  Tags);
            } else {
                finance.setTextColor(Color.parseColor("#808080"));
                finance.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("재테크");
                System.out.println("list : " +  Tags);
            }
        });

        certificate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                certificate.setTextColor(Color.parseColor("#FFFFFF"));
                certificate.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("자격증");
                System.out.println("list : " +  Tags);
            } else {
                certificate.setTextColor(Color.parseColor("#808080"));
                certificate.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("자격증");
                System.out.println("list : " +  Tags);
            }
        });

        health.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                health.setTextColor(Color.parseColor("#FFFFFF"));
                health.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("건강");
                System.out.println("list : " +  Tags);
            } else {
                health.setTextColor(Color.parseColor("#808080"));
                health.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("건강");
                System.out.println("list : " +  Tags);
            }
        });

        book.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                book.setTextColor(Color.parseColor("#FFFFFF"));
                book.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("독서");
                System.out.println("list : " +  Tags);
            } else {
                book.setTextColor(Color.parseColor("#808080"));
                book.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("독서");
                System.out.println("list : " +  Tags);
            }
        });

        anything.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                anything.setTextColor(Color.parseColor("#FFFFFF"));
                anything.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("기타");
                System.out.println("list : " +  Tags);
            } else {
                anything.setTextColor(Color.parseColor("#808080"));
                anything.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("기타");
                System.out.println("list : " +  Tags);
            }
        });


        // 작성 다음페이지 버튼(1->2)
        goto_nextPage1.setOnClickListener(view -> {
            String bucket_title = title.getText().toString().trim();
            String bucket_content = content.getText().toString().trim();
            String visibility = null;
            String Tag = new Gson().toJson(Tags);
            //System.out.println("list : " + jsonStr);

            // 버킷리스트 정보 미 입력시
            if( bucket_title.trim().length() == 0 || bucket_title == null ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WriteBucket1Activity.this);
                builder.setTitle("알림")
                        .setMessage("버킷리스트 제목을 입력해주세요.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else if (bucket_content.trim().length() == 0 || bucket_content == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WriteBucket1Activity.this);
                builder.setTitle("알림")
                        .setMessage("버킷리스트 내용을 입력해주세요.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else if (open.isChecked()==false && close.isChecked()==false) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WriteBucket1Activity.this);
                builder.setTitle("알림")
                        .setMessage("공개 여부를 설정해주세요.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else if (travel.isChecked()==false  && food.isChecked()==false && challenge.isChecked()==false && exercise.isChecked()==false && develop.isChecked()==false && diet.isChecked()==false
                    && hobby.isChecked()==false && work.isChecked()==false && language.isChecked()==false && finance.isChecked()==false && certificate.isChecked()==false &&  health.isChecked()==false ) {
                // 카테고리 선택 최소 1개 이상 해야하는데 안함
                AlertDialog.Builder builder = new AlertDialog.Builder(WriteBucket1Activity.this);
                builder.setTitle("알림")
                        .setMessage("해당 카테고리 선택을 최소 1개 이상 해야 합니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            else { // 다음 작성페이지로 이동 + 데이터 저장
                // 작성페이지 1 데이터 저장
                SharedPrefManager.setPreference(WriteBucket1Activity.this, "title", bucket_title);
                SharedPrefManager.setPreference(WriteBucket1Activity.this, "content", bucket_content);
                SharedPrefManager.setPreference(WriteBucket1Activity.this, "tags", Tag);
                if(open.isChecked() == true) {
                    visibility = "1";
                    SharedPrefManager.setPreference(WriteBucket1Activity.this, "isVisible", visibility);
                } else if (close.isChecked() == true) {
                    visibility = "0";
                    SharedPrefManager.setPreference(WriteBucket1Activity.this, "isVisible", visibility);
                }
                Log.d("message", "send_Data_1 : " + bucket_title + " , " + bucket_content + " , " + visibility + " , " + Tag  );

                // 다음 화면 이동
                Intent intent = new Intent(getApplicationContext(), WriteBucket2Activity.class);
                startActivity(intent);
            }
        });

    }
}