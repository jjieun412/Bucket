package com.example.bucket;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyBKListActivity extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;

    ImageButton btn_back;
    Intent intent;
    LinearLayout layout_bucket;
    ImageView mybk_image;
    TextView mybk_title;

    GridView gridView;
    ArrayList<bucketList_my> mybkitem = new ArrayList<>();


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybklist_page);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation2);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatbar2);

        btn_back = (ImageButton) findViewById(R.id.btn_back);

        gridView = (GridView) findViewById(R.id.mybucket_gridView);
        layout_bucket = (LinearLayout) findViewById(R.id.layout_bklist);
        mybk_image = (ImageView) findViewById(R.id.list_image);
        mybk_title = (TextView) findViewById(R.id.list_title);


        // 뒤로 가기 버튼 (내버킷 상세조회 -> 홈화면)
        btn_back.setOnClickListener(v -> {
            intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        });


        // case 함수 통해 클릭 할 때마다 화면 전환
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.nav_list:
                    intent = new Intent(getApplicationContext(), BKListHeartOnlyActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_mypage:
                    intent = new Intent(getApplicationContext(), MySetPageActivity.class);
                    startActivity(intent);
                    break;
            }
            return true;
        });

        // 버킷 작성 페이지로 이동
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), WriteBucket1Activity.class);
            startActivity(intent);
        });
    }

    protected void onStart() {
        super.onStart();
        MyBKListResponse();
    }

    // 내가 작성한 버킷 보여주기 리스트 형식
    public void MyBKListResponse() {
        String token = SharedPrefManager.getPreferenceString(MyBKListActivity.this, "token");
        token = token.replaceAll("\"", "");

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getMyBKListResponse(token);

        uploadCall.enqueue(new Callback<JsonObject>() {
            String statusCode;
            String success = "\"0\"";
            String fail = "\"1\"";

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());
                Log.d("retrofit", "Data fetch success");

                // 서버 통신 성공 시
                if (response.isSuccessful()) {
                    JsonArray temp = new JsonArray();
                    try {
                        MyBKListResponse status = new MyBKListResponse(response.body());

                        temp = status.bucketLists;
                        Log.d("message" , "status :  " + status);
                        statusCode = status.getStatus();
                        Log.d("message", "statusCode : " + statusCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);


                    if(statusCode.equals(success)) {
                        System.out.println("내 버킷 보여주기 성공");

                        for(JsonElement t : temp) {
                            bucketList_my tempBuck = new bucketList_my(t);
                            System.out.println(tempBuck);
                            mybkitem.add(tempBuck);
                        }

                        MyBKListAdapter adapter = new MyBKListAdapter(MyBKListActivity.this, mybkitem);  // 어댑터 생성
                        GridView gridView = (GridView) findViewById(R.id.mybucket_gridView);   // 어댑터 설정
                        gridView.setAdapter(adapter);

                    } else if (statusCode.equals(fail)) {
                        System.out.println("내 버킷 보여주기 실패");
                    } else {
                        System.out.println("에러 발생");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyBKListActivity.this);
                        builder.setTitle("알림")
                                .setMessage("예기치 못한 오류가 발생하였습니다. \n고객센터로 문의해주십시오.")
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