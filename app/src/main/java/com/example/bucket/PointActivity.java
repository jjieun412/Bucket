package com.example.bucket;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PointActivity extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    private RecyclerView recyclerView;
    private PointListAdapter adapter;
    private ArrayList<PointListItem> pItem;

    ImageButton btn_back;
    TextView mypoint, bktitle, getpoint, getpoint_date;
    LinearLayout layout_notice, layout_pointlist;
    Button btn_setnotice, btn_hidenotice;
    PointListItems items;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_page);

        btn_back = (ImageButton) findViewById(R.id.btn_back);
        mypoint = (TextView) findViewById(R.id.myPoint); // 포인트 잔액 보여주기
        recyclerView = (RecyclerView) findViewById(R.id.pointlistView);
        layout_pointlist = (LinearLayout) findViewById(R.id.layout_pointlist);
        bktitle = (TextView) findViewById(R.id.bk_title);
        getpoint = (TextView) findViewById(R.id.getpoint);
        getpoint_date = (TextView) findViewById(R.id.getpoint_date);
        btn_setnotice = (Button) findViewById(R.id.btn_pointNotice);
        layout_notice = (LinearLayout) findViewById(R.id.layout_pointNotice);
        btn_hidenotice = (Button) findViewById(R.id.btn_hideNotice);


        // 뒤로 가기 버튼
        btn_back.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        });

        // 유의사항 보여주기 버튼
        btn_setnotice.setOnClickListener(view -> {
            btn_setnotice.setVisibility(View.INVISIBLE);
            layout_notice.setVisibility(View.VISIBLE);
        });

        // 유의사항 숨기기 버튼
        btn_hidenotice.setOnClickListener(view -> {
            btn_setnotice.setVisibility(View.VISIBLE);
            layout_notice.setVisibility(View.GONE);
        });
    }

    protected void onStart() {
        super.onStart();

        PointResponse();
        PointListResponse();
    }


    public void PointResponse() {
        String token = SharedPrefManager.getPreferenceString(PointActivity.this, "token");
        token = token.replaceAll("\"", "");

        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getPointResponse(token);

        uploadCall.enqueue(new Callback<JsonObject>() {
            String statusCode;
            String success = "\"0\"";
            String fail = "\"1\"";

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());
                Log.d("retrofit", "Data fetch success");

                PointResponse status = null;
                if (response.isSuccessful()) {
                    try {
                        status = new PointResponse(response.body());
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);


                    if(statusCode.equals(success)) {
                        System.out.println("포인트 조회 성공");
                        mypoint.setText(" " + status.getPoint() + " P");

                    } else if (statusCode.equals(fail)) {
                        System.out.println("포인트 조회 실패");
                    } else {
                        System.out.println("에러 발생");
                        AlertDialog.Builder builder = new AlertDialog.Builder(PointActivity.this);
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


    public void PointListResponse() {
        String token = SharedPrefManager.getPreferenceString(PointActivity.this, "token");
        token = token.replaceAll("\"", "");

        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getPointListResponse(token);

        uploadCall.enqueue(new Callback<JsonObject>() {
            String statusCode;
            String success = "\"0\"";
            String fail = "\"1\"";

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());
                Log.d("retrofit", "Data fetch success");

                PointListResponse status = null;
                if (response.isSuccessful()) {
                    JsonArray temp = new JsonArray();
                    try {
                        status = new PointListResponse(response.body());
                        temp = status.details;
                        Log.d("message" , "status :  " + status);
                        statusCode = status.getStatus();
                        Log.d("message", "statusCode : " + statusCode);
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);


                    if(statusCode.equals(success)) {
                        System.out.println("포인트 내역 조회 성공");

                        items = new PointListItems(status.getDetails().getAsJsonArray());
                        Log.d("message", "pointItems  : " + items);

                        recyclerView = (RecyclerView) findViewById(R.id.pointlistView);
                        adapter = new PointListAdapter(items.list);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(PointActivity.this));
                        adapter.notifyItemInserted(0);
                    } else if (statusCode.equals(fail)) {
                        System.out.println("포인트 내역 조회 실패");
                    } else {
                        System.out.println("에러 발생");
                        AlertDialog.Builder builder = new AlertDialog.Builder(PointActivity.this);
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
