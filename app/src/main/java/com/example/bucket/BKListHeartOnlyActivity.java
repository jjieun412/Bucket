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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BKListHeartOnlyActivity extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    Intent intent;
    ImageButton btn_back;
    LinearLayout layout_bucket;
    ImageView bk_image;
    TextView bk_title;
    ImageView btn_favorite;

    ArrayList<bucketLists_user> bkitem = new ArrayList<>();


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heartonly_page);

        btn_back = (ImageButton) findViewById(R.id.btn_back);
        layout_bucket = (LinearLayout) findViewById(R.id.layout_bklist);
        bk_image = (ImageView) findViewById(R.id.list_image);
        bk_title = (TextView) findViewById(R.id.list_title);
        btn_favorite = (ImageView) findViewById(R.id.favorite_mark);

    }

    protected void onStart() {
        super.onStart();

        btn_back.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MyBKListActivity.class);
            startActivity(intent);
        });

        BKListHeartOnlyResponse();
    }


    public void BKListHeartOnlyResponse() {
        String token = SharedPrefManager.getPreferenceString(BKListHeartOnlyActivity.this, "token");
        token = token.replaceAll("\"", "");

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getBKListHeartOnlyResponse(token);

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
                        BKListHeartOnlyResponse status = new BKListHeartOnlyResponse(response.body());
                        temp = status.bucketLists;
                        Log.d("message" , "status :  " + status);
                        statusCode = status.getStatus();
                        Log.d("message", "statusCode : " + statusCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);


                    if(statusCode.equals(success)) {
                        System.out.println("관심목록 조회 성공");

                        for(JsonElement t : temp) {
                            bucketLists_user tempBuck = new bucketLists_user(t);
                            System.out.println(tempBuck);
                            bkitem.add(tempBuck);
                        }

                        BKListHeartOnlyAdapter adapter = new BKListHeartOnlyAdapter(BKListHeartOnlyActivity.this, bkitem);  // 어댑터 생성
                        GridView gridView = (GridView) findViewById(R.id.heartonly_gridView);   // 어댑터 설정
                        gridView.setAdapter(adapter);

                    } else if (statusCode.equals(fail)) {
                        System.out.println("관심목록 조회 실패");
                    } else {
                        System.out.println("에러 발생");
                        AlertDialog.Builder builder = new AlertDialog.Builder(BKListHeartOnlyActivity.this);
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
