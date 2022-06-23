package com.example.bucket;


import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ServiceAskListActivity extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    // 문의내역
    LinearLayout layout_list1, layout_list2;
    TextView ask_bkTitle, ask_Date;
    ImageButton btn_back, noticeView, noticeHide;
    Button noticeDelete;
    ImageView ask_imageView_list;
    TextView ask_content_list;

    private RecyclerView recyclerView;
    private ServiceAskListAdapter adapter;
    ArrayList<ServiceAsklistItem> sItems;



    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_asklist);

        layout_list1 = (LinearLayout) findViewById(R.id.layout_askList1);  // recycler에 추가할 list
        layout_list2 = (LinearLayout) findViewById(R.id.layout_askList2);  // 숨겼다 말았다할 list
        btn_back = (ImageButton) findViewById(R.id.btn_back);


        // 뒤로 가기 버튼
        btn_back.setOnClickListener(view -> {
            finish();
        });

    }


    protected void onStart() {
        super.onStart();

        ServiceAskListResponse();
    }


    public void ServiceAskListResponse() {
        String token = SharedPrefManager.getPreferenceString(ServiceAskListActivity.this, "token");
        token = token.replaceAll("\"", "");

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getServiceAskListResponse(token);

        uploadCall.enqueue(new Callback<JsonObject>() {
            String statusCode;
            String success = "\"0\"";
            String fail = "\"1\"";


            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());
                Log.d("retrofit", "Data fetch success");

                ServiceAskListResponse status = null;
                if (response.isSuccessful()) {
                    try {
                        status = new ServiceAskListResponse(response.body());
                        System.out.println(status);
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);


                    if(statusCode.equals(success)) {
                        sItems = new ArrayList<ServiceAsklistItem>();
                        System.out.println("달성 인증 문의내역 조회 성공");

                        JsonArray tempArr = status.getVerifyrequests();
                        System.out.println(tempArr);

                        for(JsonElement t: tempArr)
                        {
                            ServiceAsklistItem tem = new ServiceAsklistItem(t.getAsJsonObject());
                            sItems.add(tem);
                        }


                        recyclerView = (RecyclerView) findViewById(R.id.asklistView);
                        adapter = new ServiceAskListAdapter(sItems);
                        recyclerView.setAdapter(adapter);

                        recyclerView.setLayoutManager(new LinearLayoutManager(ServiceAskListActivity.this));
                        adapter.notifyItemInserted(0);


                    } else if (statusCode.equals(fail)) {
                        System.out.println("달성 인증 문의 내역 조회 실패");
                        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceAskListActivity.this);
                        builder.setMessage("문의 내역 조회하는 것을 실패하였습니다. \n다시 시도해 주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        System.out.println("에러 발생");
                        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceAskListActivity.this);
                        builder.setTitle("알림")
                                .setMessage("예기치 못한 오류가 발생하였습니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

}