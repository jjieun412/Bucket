package com.example.bucket;



import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class PWsetActivity extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    Button pw_set;
    EditText PW1, PW2;   // 새로운 비번, 비번재확인
    ImageView setImage1, setImage2;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_pw_setpage);


        PW1 = (EditText) findViewById(R.id.PW);
        PW2 = (EditText) findViewById(R.id.PW_check);
        setImage1 = (ImageView) findViewById(R.id.setImage1);    // 새로운 비번 유효성 검사 이미지 ㅊ체크
        setImage2 = (ImageView) findViewById(R.id.setImage2);    // 비번 재확인 이미지 체크
        pw_set = (Button) findViewById(R.id.btn_pwset);

/*
        //새로운 비번 유효성 검사하기
        PW1.addTextChangedListener(new TextWatcher() {
            String pswd = PW1.getText().toString();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9]+)(?=.*[~`!@#$%\\^&*()-]).{8,16}$", pswd)) {
                    setImage1.setImageResource(R.drawable.error);
                } else {
                    setImage1.setImageResource(R.drawable.okay_check);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9]+)(?=.*[~`!@#$%\\^&*()-]).{8,16}$", pswd)) {
                    setImage1.setImageResource(R.drawable.error);
                } else {
                    setImage1.setImageResource(R.drawable.okay_check);
                }
            }
        });
 */


        //비번 일치 여부 확인
        PW2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (PW1.getText().toString().equals(PW2.getText().toString())) {
                    setImage2.setImageResource(R.drawable.okay_check);
                } else {
                    setImage2.setImageResource(R.drawable.error);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (PW1.getText().toString().equals(PW2.getText().toString())) {
                    setImage2.setImageResource(R.drawable.okay_check);
                } else {
                    setImage2.setImageResource(R.drawable.error);
                }
            }
        });


        // 비밀번호 변경 완료 페이지로 이동
        pw_set.setOnClickListener(view -> {

            String pw1 = PW1.getText().toString();
            String pw2 = PW2.getText().toString();

            if( pw1.trim().length() == 0 || pw1 == null ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PWsetActivity.this);
                builder.setTitle("알림")
                        .setMessage("새로운 비밀번호를 입력하지 않았습니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else if (pw2.trim().length() == 0 || pw2 == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PWsetActivity.this);
                builder.setTitle("알림")
                        .setMessage("비밀번호 재확인을 해주세요.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else if (pw1.equals(pw2) == false) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PWsetActivity.this);
                builder.setTitle("알림")
                        .setMessage("비밀번호와 재확인한 비밀번호가 일치하지 않습니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else if(!Pattern.matches("^(?=.*[A-Za-z]+)(?=.*[0-9]+).(?=.*[~`!@#$%\\^&*()-]).{8,16}$", pw1)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PWsetActivity.this);
                builder.setTitle("알림")
                        .setMessage("비밀번호와 재확인한 비밀번호가 일치하지 않습니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else if(!Pattern.matches("^(?=.*[A-Za-z]+)(?=.*[0-9]+).(?=.*[~`!@#$%\\^&*()-]).{8,16}$", pw2)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PWsetActivity.this);
                builder.setTitle("알림")
                        .setMessage("비밀번호와 재확인한 비밀번호가 일치하지 않습니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                NewPWsetResponse();
            }
        });
    }


    public void NewPWsetResponse() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        String my_Email = bundle.getString("email");      // (이전에 입력받은 이메일 값)
        Integer my_checkNum = bundle.getInt("code");     // (이전에 입력받은 인증번호)
        String password = PW1.getText().toString();



        NewPWsetRequest newPWsetRequest= new NewPWsetRequest(my_Email, my_checkNum, password);
        System.out.println("the NewPWsetRequest is : " + newPWsetRequest);

        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();


        initMyApi.getNewPWsetResponse(newPWsetRequest).enqueue(new Callback<JsonObject>() {

            String statusCode;
            String success = "\"0\"";   // 새로운 비번 생성 완료
            String failEmail = "\"1\"";  // 인증완료 안된 이메일
            String NotSendEmail = "\"2\"";   // 코드를 보내지 않았던 이메일
            String failCode = "\"3\"";    // 코드가 맞지 않은 경우

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());

                Log.d("retrofit", "Data fetch success");

                //통신 성공
                if (response.isSuccessful()) {
                    try {
                        //response.body()를 status에 저장
                        NewPWsetResponse status = new NewPWsetResponse(response.body());
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println(statusCode);
                    System.out.println("인증번호 인증 상태 무사 전송");

                    if (statusCode.equals(success)) {
                        Toast.makeText(PWsetActivity.this, "새로운 비밀번호가 설정되었습니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), PWokayActivity.class);
                        startActivity(intent);
                    } else if (statusCode.equals(failEmail)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PWsetActivity.this);
                        builder.setTitle("알림")
                                .setMessage("인증되지 않은 이메일 입니다. \n다시 입력해 주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else if (statusCode.equals(NotSendEmail)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PWsetActivity.this);
                        builder.setTitle("알림")
                                .setMessage("인증번호 전송이 되지 않은 이메일 입니다. \n다시 입력해 주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else if (statusCode.equals(failCode)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PWsetActivity.this);
                        builder.setTitle("알림")
                                .setMessage("유효하지 않은 인증번호 입니다. \n다시 입력해 주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PWsetActivity.this);
                        builder.setTitle("알림")
                                .setMessage("예기치 못한 에러가 발생하였습니다. \n고객센터로 문의해주십시오.")
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