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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    BottomNavigationView bottomNavigationView;
    FloatingActionButton floatingActionButton;

    ImageButton search;
    Intent intent;
    LinearLayout layout_bucket;
    ImageView bk_image;
    TextView bk_title;
    ImageView btn_favorite;

    GridView gridView;
    List<GridView> layout = new ArrayList<>();
    ArrayList<bucketLists_user> bkitem = new ArrayList<>();


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation1);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatbar1);

        gridView = (GridView) findViewById(R.id.recommend_gridView);
        layout_bucket = (LinearLayout) findViewById(R.id.layout_bklist);
        bk_image = (ImageView) findViewById(R.id.list_image);
        bk_title = (TextView) findViewById(R.id.list_title);
        btn_favorite = (ImageView) findViewById(R.id.favorite_mark);


        search = (ImageButton) findViewById(R.id.btn_search);
        search.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        });


        // case 함수 통해 클릭 할 때마다 화면 전환
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.nav_point:
                    intent = new Intent(getApplicationContext(), PointActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_mybucky:
                    intent = new Intent(getApplicationContext(), MyBKListActivity.class);
                    startActivity(intent);
                    break;
            }
            return true;
        });

        // 상점 페이지로 이동
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), PointRankActivity.class);
            startActivity(intent);
        });
    }

    protected void onStart() {
        super.onStart();
        // 회원가입시 선택했던 tag와 일치하는 버킷들을 보여줌 (타유저 버킷임)
        RecommendResponse();
    }

    protected void onStop() {
        super.onStop();

        for(GridView t : layout)
        {
            if(t != null) {
                gridView.removeView(t);
            }
        }
        Log.d("msg", "Doing OnStop");
    }


    public void RecommendResponse() {
        String token = SharedPrefManager.getPreferenceString(HomeActivity.this, "token");
        token = token.replaceAll("\"", "");

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getRecommendResponse(token);

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
                        RecommendResponse status = new RecommendResponse(response.body());
                        temp = status.bucketLists;
                        Log.d("message" , "status :  " + status);
                        statusCode = status.getStatus();
                        Log.d("message", "statusCode : " + statusCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);


                    if(statusCode.equals(success)) {
                        System.out.println("추천 성공");

                        for(JsonElement t : temp) {
                            bucketLists_user tempBuck = new bucketLists_user(t);
                            System.out.println(tempBuck);
                            bkitem.add(tempBuck);
                        }

                        RecommendBKAdapter adapter = new RecommendBKAdapter(HomeActivity.this, bkitem);  // 어댑터 생성
                        gridView = (GridView) findViewById(R.id.recommend_gridView);   // 어댑터 설정
                        System.out.println("그리드뷰: " + gridView);

                        gridView.setAdapter(adapter);


                    } else if (statusCode.equals(fail)) {
                        System.out.println("추천 실패");
                    } else {
                        System.out.println("에러 발생");
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
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
