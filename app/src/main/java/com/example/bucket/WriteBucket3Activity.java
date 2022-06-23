package com.example.bucket;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class WriteBucket3Activity extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    Calendar calendar = Calendar.getInstance();
    int y = calendar.get(Calendar.YEAR);
    int m = calendar.get(Calendar.MONTH);
    int d = calendar.get(Calendar.DAY_OF_MONTH);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    String startday = null, endday = null;
    int S=0, E=0;
    Date start = null, end = null;

    DatePickerDialog datePickerDialog;
    Button set_start, set_end, goto_nextPage3;
    TextView set_syear, set_smonth, set_sday, set_eyear, set_emonth, set_eday;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writebucket_page3);

        // 뒤로 가기 버튼
        ImageButton button_back = (ImageButton) findViewById(R.id.btn_back);
        button_back.setOnClickListener(view -> {
            finish();
            //Intent intent = new Intent(getApplicationContext(), WriteBucket2Activity.class);
            //startActivity(intent);
        });

        set_start = (Button) findViewById(R.id.btn_start);
        set_end = (Button) findViewById(R.id.btn_end);
        set_syear = (TextView) findViewById(R.id.start_year);
        set_smonth = (TextView) findViewById(R.id.start_month);
        set_sday = (TextView) findViewById(R.id.start_day);
        set_eyear = (TextView) findViewById(R.id.end_year);
        set_emonth = (TextView) findViewById(R.id.end_month);
        set_eday = (TextView) findViewById(R.id.end_day);
        goto_nextPage3 = (Button) findViewById(R.id.btn_writebucket3);


        set_start.setOnClickListener(view -> {

            showDate_start();
        });

        set_end.setOnClickListener(view -> {

            showDate_end();
        });

        goto_nextPage3.setOnClickListener(view -> {
            /*
            try {
                start = sdf.parse(String.valueOf(S));
                end = sdf.parse(String.valueOf(E));
            } catch (ParseException e) {
                e.printStackTrace();
            }
             */
            SharedPrefManager.setPreference(WriteBucket3Activity.this, "date_S", String.valueOf(S));
            SharedPrefManager.setPreference(WriteBucket3Activity.this, "date_E", String.valueOf(E));

            // 시작, 종료 날짜 미등록 시
            if(TextUtils.isEmpty(set_syear.getText()) || TextUtils.isEmpty(set_smonth.getText()) || TextUtils.isEmpty(set_sday.getText()) ||
                    TextUtils.isEmpty(set_eyear.getText()) || TextUtils.isEmpty(set_emonth.getText()) || TextUtils.isEmpty(set_eday.getText())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WriteBucket3Activity.this);
                builder.setTitle("알림")
                        .setMessage("시작 날짜와 종료 날짜를 설정해 주세요.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else { // 다음 작성페이지로 이동 + 데이터 전
                SharedPrefManager.setPreference(WriteBucket3Activity.this, "startDate", startday);
                SharedPrefManager.setPreference(WriteBucket3Activity.this, "endDate", endday);
                Log.d("message", "send_Data_3 : " + startday + " , " + endday );

                // 다음 화면 이동
                Intent intent = new Intent(getApplicationContext(), WriteBucket4Activity.class);
                startActivity(intent);
            }
        });
    }

    public void showDate_start() {
        datePickerDialog = new DatePickerDialog(WriteBucket3Activity.this, (datePicker, year, month, dayOfMonth) -> {
            set_syear.setText(String.format("%d 년", year));
            set_smonth.setText(String.format("%d 월", month + 1));
            set_sday.setText(String.format("%d 일", dayOfMonth));// 설정 날짜 보여주기
            startday = year + "-" + (month+1) + "-" + dayOfMonth;
            S = year + (month+1) + dayOfMonth;
            //Log.d("message", "startday : " + S);
        }, y, m, d);
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.show();
    }


    public void showDate_end() {
        datePickerDialog = new DatePickerDialog(WriteBucket3Activity.this, (datePicker, year, month, dayOfMonth) -> {
            set_eyear.setText(String.format("%d 년", year));
            set_emonth.setText(String.format("%d 월", month+1));
            set_eday.setText(String.format("%d 일", dayOfMonth));  // 설정 날짜 보여주기
            endday = year + "-" + (month+1) + "-" + dayOfMonth;
            E = year + (month+1) + dayOfMonth;
            //Log.d("message", "endday : " + E);
        }, y, m, d);
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
        datePickerDialog.show();
    }

}
