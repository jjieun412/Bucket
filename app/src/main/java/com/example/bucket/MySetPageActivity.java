package com.example.bucket;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MySetPageActivity extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    ImageButton btn_back;
    Button btn_asklist, btn_logout, btn_profile_edit;
    CircleImageView profile;
    TextView nickName, myEmail;
    String Travel = "여행";
    String Food = "맛집";
    String Challenge = "도전";
    String Exercise = "운동";
    String Develop = "자기계발";
    String Diet = "자기관리";
    String Hobby = "취미";
    String Work = "입시/취업";
    String Language = "외국어";
    String Finance = "재테크";
    String Certificate = "자격증";
    String Health = "건강";
    String Book = "독서";
    String Anything = "기타";
    ToggleButton travel, food, challenge, exercise, develop, diet, hobby, work, language, finance, certificate, health, book, anything;
    UserItem_my uitem;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myset_page);

        profile = (CircleImageView) findViewById(R.id.profile_img);
        nickName = (TextView) findViewById(R.id.my_nickname);
        myEmail = (TextView) findViewById(R.id.my_email);

        // 관심카테고리
        travel = findViewById(R.id.btn_travel);
        food = findViewById(R.id.btn_food);
        challenge = findViewById(R.id.btn_challenge);
        exercise = findViewById(R.id.btn_exercise);
        develop = findViewById(R.id.btn_develop);
        diet = findViewById(R.id.btn_diet);
        hobby = findViewById(R.id.btn_hobby);
        work = findViewById(R.id.btn_work);
        language = findViewById(R.id.btn_language);
        finance = findViewById(R.id.btn_finance);
        certificate = findViewById(R.id.btn_certificate);
        health = findViewById(R.id.btn_health);
        book = findViewById(R.id.btn_book);
        anything = findViewById(R.id.btn_anything);


        // 뒤로 가기 버튼
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MyBKListActivity.class);
            startActivity(intent);
        });

        // 문의내역 확인 버튼
        btn_asklist = findViewById(R.id.btn_asklist);
        btn_asklist.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ServiceAskListActivity.class);
            startActivity(intent);
        });

        // 로그아웃 버튼
        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MySetPageActivity.this);
            builder.setMessage("로그아웃 하시겠습니까?")
                    .setNegativeButton("취소", null)
                    .setPositiveButton("확인", (dialogInterface, i) -> {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    })
                    .create()
                    .show();
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        // 프로필 수정 버튼
        btn_profile_edit = findViewById(R.id.btn_profile_edit);
        btn_profile_edit.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MySetPageEditActivity.class);
            startActivity(intent);
        });

    }


    protected void onStart() {
        super.onStart();

        MySetPageResponse();
    }


    public void MySetPageResponse() {
        String token = SharedPrefManager.getPreferenceString(MySetPageActivity.this, "token");
        token = token.replaceAll("\"", "");

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getMySetPageResponse(token);

        uploadCall.enqueue(new Callback<JsonObject>() {
            String statusCode;
            String success = "\"0\"";
            String fail = "\"1\"";

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());
                Log.d("retrofit", "Data fetch success");

                MySetPageResponse status = null;

                if (response.isSuccessful()) {
                    try {
                        status = new MySetPageResponse(response.body());
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);


                    if (statusCode.equals(success)) {
                        System.out.println("내 정보 불러오기 성공");
                        uitem = new UserItem_my(status.getUser());
                        Log.d("message", "userItem  : " + uitem);

                        Glide.with(getApplicationContext()).load(uitem.getPicturePath()).into(profile);
                        nickName.setText(uitem.getNickname());
                        myEmail.setText(uitem.getEmail());

                        List<String> Tag = uitem.getTags();  // 카테고리 가져옴
                        if(Tag.contains(Travel)) {  // 여행
                            travel.setTextColor(Color.parseColor("#FFFFFF"));
                            travel.setBackgroundColor(Color.parseColor("#191970"));
                        }
                        if(Tag.contains(Food)) {  // 맛집
                            food.setTextColor(Color.parseColor("#FFFFFF"));
                            food.setBackgroundColor(Color.parseColor("#191970"));
                        }
                        if(Tag.contains(Challenge)) {  // 도전
                            challenge.setTextColor(Color.parseColor("#FFFFFF"));
                            challenge.setBackgroundColor(Color.parseColor("#191970"));
                        }
                        if(Tag.contains(Exercise)) {
                            exercise.setTextColor(Color.parseColor("#FFFFFF"));
                            exercise.setBackgroundColor(Color.parseColor("#191970"));
                        }
                        if(Tag.contains(Develop)) {
                            develop.setTextColor(Color.parseColor("#FFFFFF"));
                            develop.setBackgroundColor(Color.parseColor("#191970"));
                        }
                        if(Tag.contains(Diet)) {
                            diet.setTextColor(Color.parseColor("#FFFFFF"));
                            diet.setBackgroundColor(Color.parseColor("#191970"));
                        }
                        if(Tag.contains(Hobby)) {
                            hobby.setTextColor(Color.parseColor("#FFFFFF"));
                            hobby.setBackgroundColor(Color.parseColor("#191970"));
                        }
                        if(Tag.contains(Work)) {
                            work.setTextColor(Color.parseColor("#FFFFFF"));
                            work.setBackgroundColor(Color.parseColor("#191970"));
                        }
                        if(Tag.contains(Language)) {
                            language.setTextColor(Color.parseColor("#FFFFFF"));
                            language.setBackgroundColor(Color.parseColor("#191970"));
                        }
                        if(Tag.contains(Finance)) {
                            finance.setTextColor(Color.parseColor("#FFFFFF"));
                            finance.setBackgroundColor(Color.parseColor("#191970"));
                        }
                        if(Tag.contains(Certificate)) {
                            certificate.setTextColor(Color.parseColor("#FFFFFF"));
                            certificate.setBackgroundColor(Color.parseColor("#191970"));
                        }
                        if(Tag.contains(Health)) {
                            health.setTextColor(Color.parseColor("#FFFFFF"));
                            health.setBackgroundColor(Color.parseColor("#191970"));
                        }
                        if(Tag.contains(Book)) {
                            book.setTextColor(Color.parseColor("#FFFFFF"));
                            book.setBackgroundColor(Color.parseColor("#191970"));
                        }
                        if(Tag.contains(Anything)){
                            anything.setTextColor(Color.parseColor("#FFFFFF"));
                            anything.setBackgroundColor(Color.parseColor("#191970"));
                        }

                    } else if (statusCode.equals(fail)) {
                        System.out.println("내 정보 불러오기 실패");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MySetPageActivity.this);
                        builder.setTitle("알림")
                                .setMessage("내 정보를 불러오지 못했습니다.\n다시 시도해 주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else {
                        System.out.println("에러 발생");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MySetPageActivity.this);
                        builder.setTitle("알림")
                                .setMessage("예기치 못한 오류가 발생하였습니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

}
