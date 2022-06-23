package com.example.bucket;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.util.Locale;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class PwFindActivity extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    Integer input_num;
    EditText email, checkNum;
    Button send_num, checking;
    String input_email;
    TextView timer;   // 인증번호
    LinearLayout visibility;
    CountDownTimer countDownTimer;

    Pattern emailPattern = android.util.Patterns.EMAIL_ADDRESS;



    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pwfind_page);

        // 뒤로 가기 버튼
        ImageButton button_back = (ImageButton) findViewById(R.id.btn_back);
        button_back.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        //입력받을 부분 (이메일, 인증번호), 인증전송, 인증 버튼
        email = (EditText) findViewById(R.id.ID_email);     // 인증번호 보낼 이메일
        checkNum = (EditText) findViewById(R.id.checknum);   // 인증번호
        send_num = (Button) findViewById(R.id.btn_checknumsend);  // 인증번호 전송버튼
        checking = (Button) findViewById(R.id.btn_checknum);    // 인증번호 (확인)일치불일치 여부 버튼
        timer = (TextView) findViewById(R.id.input_timer);   // 인증번호 timer
        visibility = (LinearLayout) findViewById(R.id.checknumber);  // 인증번호 칸 보이고 안보이고


        // 버튼 비활성화, 인증번호 비활성화
        send_num.setEnabled(false);
        checking.setEnabled(false);
        send_num.setBackgroundColor(Color.parseColor("#C0C0C0"));
        checking.setBackgroundColor(Color.parseColor("#C0C0C0"));
        timer.setVisibility(timer.INVISIBLE);
        visibility.setVisibility(visibility.INVISIBLE);


        // 인증번호 받을 이메일 입력
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input_email = email.getText().toString().trim();

                if(input_email.length() == 0) {
                    send_num.setEnabled(false);
                } else {
                    send_num.setEnabled(true);
                    send_num.setBackgroundColor(Color.parseColor("#191970"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        // 이메일로 인증번호 전송 버튼
        send_num.setOnClickListener(view -> {
            input_email= email.getText().toString();

            if (email.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PwFindActivity.this);
                builder.setTitle("알림")
                        .setMessage("이메일을 입력하지 않았습니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else if(!emailPattern.matcher(input_email).matches()) {
                Toast.makeText(PwFindActivity.this, "올바르지 않는 이메일 형식입니다.\n다시 입력해 주세요.", Toast.LENGTH_SHORT).show();

                return;
            } else {
                SendCodeResponse();
            }
        });


        // 인증번호 입력
        checkNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                input_num = Integer.parseInt(checkNum.getText().toString());
                if(input_num == 0) {
                    checking.setEnabled(false);
                } else {
                    checking.setEnabled(true);
                    checking.setBackgroundColor(Color.parseColor("#191970"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        // 인증번호 일치여부 확인 버튼
        checking.setOnClickListener(view -> {
            input_num = Integer.parseInt(checkNum.getText().toString());

            if (email.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PwFindActivity.this);
                builder.setTitle("알림")
                        .setMessage("인증번호를 입력하지 않았습니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                //인증번호 맞는지 아닌지 확인
                VerifyCodeResponse();
            }
        });


        // 인증번호 제한 시간 카운트다운
        countDownTimer = new CountDownTimer(3*60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d : %02d", minutes, seconds);

                timer.setVisibility(timer.VISIBLE);
                timer.setText(timeLeftFormatted);
            }

            @Override
            public void onFinish() {
                timer.setText("00:00");
                Toast.makeText(getApplicationContext(), "인증시간이 만료되었습니다. \n인증번호를 재전송 해주세요.", Toast.LENGTH_SHORT).show();
            }
        };
    }


    public void SendCodeResponse() {
        String sendEmail = email.getText().toString().trim();

        SendCodeRequest sendCodeRequest = new SendCodeRequest(sendEmail);
        System.out.println("the sendCodeRequest is : " + sendCodeRequest);

        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();


        initMyApi.getSendCodeResponse(sendCodeRequest).enqueue(new Callback<JsonObject>() {

            String statusCode;
            String success = "\"0\"";  // 인증코드 전송 완료
            String failEmail = "\"1\"";  // 등록되지 않은 이메일
            String failSend = "\"2\"";   // 인증번호 전송 실패

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());

                Log.d("retrofit", "Data fetch success");

                //통신 성공
                if (response.isSuccessful()) {
                    try {
                        //response.body()를 status에 저장
                        SendCodeResponse status = new SendCodeResponse(response.body());
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println(statusCode);
                    System.out.println("인증번호 전송 상태 무사 전송");

                    if (statusCode.equals(success)) {
                        visibility.setVisibility(visibility.VISIBLE);
                        send_num.setText("인증번호 재전송");
                        countDownTimer.start();
                        Toast.makeText(PwFindActivity.this, "등록된 이메일 입니다. \n인증번호를 성공적으로 전송하였습니다.", Toast.LENGTH_LONG).show();
                    } else if (statusCode.equals(failEmail)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PwFindActivity.this);
                        builder.setTitle("알림")
                                .setMessage("등록되지 않은 이메일 입니다. \n다시 입력해 주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        visibility.setVisibility(visibility.INVISIBLE);
                    } else if (statusCode.equals(failSend)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PwFindActivity.this);
                        builder.setTitle("알림")
                                .setMessage("인증번호를 전송하지 못했습니다. \n다시 입력해 주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        visibility.setVisibility(visibility.INVISIBLE);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PwFindActivity.this);
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



    public void VerifyCodeResponse() {
        String my_Email = email.getText().toString().trim();
        Integer my_checkNum = Integer.parseInt(checkNum.getText().toString());


        VerifyCodeRequest verifyCodeRequest= new VerifyCodeRequest(my_Email, my_checkNum);
        System.out.println("the VerifyCodeRequest is : " + verifyCodeRequest);

        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();


        initMyApi.getVerifyCodeResponse(verifyCodeRequest).enqueue(new Callback<JsonObject>() {

            String statusCode;
            String success = "\"0\"";   // 인증번호 일치
            String failCode = "\"1\"";  // 인증번호 불일치
            String failSend = "\"2\"";   // 해당 이메일에 인증코드 보낸적 없음

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());

                Log.d("retrofit", "Data fetch success");

                //통신 성공
                if (response.isSuccessful()) {
                    try {
                        //response.body()를 status에 저장
                        VerifyCodeResponse status = new VerifyCodeResponse(response.body());
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println(statusCode);
                    System.out.println("인증번호 인증 상태 무사 전송");

                    if (statusCode.equals(success)) {
                        // 코드 전송 성공한 이메일 저장
                        Toast.makeText(PwFindActivity.this, "인증이 완료되었습니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getBaseContext(), PWsetActivity.class);
                        intent.putExtra("email", my_Email);
                        intent.putExtra("code", my_checkNum);
                        startActivity(intent);
                    } else if (statusCode.equals(failCode)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PwFindActivity.this);
                        builder.setTitle("알림")
                                .setMessage("유효하지 않는 인증번호 입니다. \n다시 입력해 주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else if (statusCode.equals(failSend)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PwFindActivity.this);
                        builder.setTitle("알림")
                                .setMessage("인증번호를 전송하지 못했습니다. \n다시 전송해 주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PwFindActivity.this);
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