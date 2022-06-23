package com.example.bucket;


import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class RetrofitClient {

    private static RetrofitClient instance = null;
    private static initMyApi initMyApi;

    // 사용하고 있는 서버 BASE주소
    private static String baseUrl = "https://5ns0yfs844.execute-api.ap-northeast-2.amazonaws.com/test/";


    private RetrofitClient() {
        //로그 보기 위한 interceptor
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor) // update the token
                .authenticator(new TokenAuthenticator())  // set the token in the header
                .build();


        //retrofit 설정
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)   //로그 기능 추가
                .build();

        initMyApi = retrofit.create(initMyApi.class);
        Log.d("TEST", "실행완료!");
        Log.d("TEST", "Logging :  " + httpLoggingInterceptor);
    }


    public static RetrofitClient getInstance() {
        if(instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }


    public static initMyApi getRetrofitInterface() {
        return initMyApi;
    }

}
