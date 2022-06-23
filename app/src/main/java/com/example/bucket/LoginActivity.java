package com.example.bucket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    //private static final String DATA_STORE = "data" ;
    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;
    EditText inputID, inputPW;
    Pattern p;
    Matcher m;
    final String EMAIL_CHECK = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    final String PW_CHECK = "^[A-Za-z0-9_@./!$%^=#&+-]*.{8,16}$";



    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);


        // 비밀번호 찾기로 이동
        Button findPW = (Button) findViewById(R.id.btn_findPW);
        findPW.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), PwFindActivity.class);
            startActivity(intent);
        });

        // 회원가입 페이지로 이동
        Button join = (Button) findViewById(R.id.btn_join);
        join.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
            startActivity(intent);
        });

        // 로그인페이지 아이디 비번 텍스트 창 (사용자 입력)
        inputID = (EditText) findViewById(R.id.ID_email);
        inputPW = (EditText) findViewById(R.id.PW);


        //로그인 버튼 클릭시 ~
        Button login = (Button) findViewById(R.id.btn_login);
        login.setOnClickListener(view -> {
            String email = inputID.getText().toString();
            String pswd = inputPW.getText().toString();

            //로그인 정보 미 입력시
            if(email.trim().length() == 0 || pswd.trim().length() == 0 || email == null || pswd == null ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("알림")
                        .setMessage("로그인 정보를 입력바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {  //로그인 통신
                LoginResponse();
            }
        });

    }


    public void LoginResponse() {
        String userID = inputID.getText().toString().trim();
        String userPW = inputPW.getText().toString().trim();

        //loginRequest에 사용자가 입력한 id와 pw 저장
        LoginRequest loginRequest = new LoginRequest(userID, userPW);
        System.out.println("the loginrequest is: " + loginRequest);


        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();

        //loginRequest에 저장된 데이터와 함께 logininterface에서 정의한 getLoginResponse 함수 실행 후 응답받음
        initMyApi.getLoginResponse(loginRequest).enqueue(new Callback<JsonObject>() {

            String statusCode, token;
            String success = "\"0\"";
            String fail = "\"1\"";

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());
                Log.d("retrofit", "Data fetch success");

                // 서버 통신 성공 시
                if (response.isSuccessful()) {
                    try {
                        //response.body()를 status에 저장
                        LoginResponse status = new LoginResponse(response.body());
                        statusCode = status.getStatus();
                        token = status.getToken();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println(statusCode);
                    //System.out.println("로그인 상태 무사 저장");

                    if(statusCode.equals(success)) {

                        // 로그인 정보 저장
                        SharedPrefManager.setPreference(LoginActivity.this, "token", token);
                        Log.d("message", "token : " + token);

                        SharedPrefManager.setPreference(LoginActivity.this, "email", userID);
                        SharedPrefManager.setPreference(LoginActivity.this, "pswd", userPW);
                        Log.d("message", "email, pswd : " + userID + userPW);


                        System.out.println("로그인 성공");
                        Toast.makeText(LoginActivity.this, "환영합니다!!", Toast.LENGTH_LONG).show();

                        // 화면 전환
                        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    } else if (statusCode.equals(fail)) {
                        System.out.println("로그인 실패");

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("알림")
                                .setMessage("회원정보가 존재하지 않습니다. \n회원가입을 진행해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else {

                        System.out.println("에러 발생");

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
