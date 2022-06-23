package com.example.bucket;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;

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


public class SearchActivity extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    ImageButton btn_back, btn_search;
    EditText search_keyword;

    ArrayList<bucketLists_user> mybkitem = new ArrayList<>();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_search = (ImageButton) findViewById(R.id.btn_search);
        search_keyword = (EditText) findViewById(R.id.search_keyword);


        // 뒤로 가기 버튼
        btn_back.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        });

        // 검색 버튼
        btn_search.setOnClickListener(view -> {
            String searchWord = search_keyword.getText().toString();

            if( searchWord.trim().length() == 0 || searchWord == null ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                builder.setTitle("알림")
                        .setMessage("검색 키워드를 입력하지 않았습니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                SearchResponse();
            }
        });
    }


    public void SearchResponse() {
        String token = SharedPrefManager.getPreferenceString(SearchActivity.this, "token");
        token = token.replaceAll("\"", "");
        String set_keyword= search_keyword.getText().toString().trim();

        //SearchRequest searchRequest = new SearchRequest(toset_keyword);
        //System.out.println("the searchRequest is: " + searchRequest);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getSearchResponse(token, set_keyword);

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
                        SearchResponse status = new SearchResponse(response.body());
                        temp = status.bucketLists;
                        Log.d("message" , "status :  " + status);
                        statusCode = status.getStatus();
                        Log.d("message", "statusCode : " + statusCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);


                    if(statusCode.equals(success)) {
                        System.out.println("검색 성공");

                        for(JsonElement t : temp) {
                            bucketLists_user tempBuck = new bucketLists_user(t);
                            System.out.println(tempBuck);
                            mybkitem.add(tempBuck);
                        }

                        SearchAdapter adapter = new SearchAdapter(SearchActivity.this, mybkitem);  // 어댑터 생성
                        GridView gridView = (GridView) findViewById(R.id.search_gridView);   // 어댑터 설정
                        gridView.setAdapter(adapter);

                    } else if (statusCode.equals(fail)) {
                        System.out.println("검색 실패");
                        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                        builder.setTitle("알림")
                                .setMessage("검색에 실패하였습니다. \n키워드를 다시 입력해 주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else {
                        System.out.println("에러 발생");
                        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
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
