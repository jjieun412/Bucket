package com.example.bucket;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WriteBucket4Activity extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private RecyclerView recyclerView;
    private MyRecyclerAdapter adapter;
    private ArrayList<DatePlanItem> dateitem = new ArrayList<>();
    private List<Map<String, Object>> detail_Rule= new ArrayList<Map<String, Object>>();

    ToggleButton date, rule;
    Button goto_nextPage4;
    FrameLayout frame;
    LinearLayout layout_date, layout_rule;
    LayoutInflater layoutInflater;

    ImageButton btn_plus, btn_minus;  // layout 추가 삭제 버튼
    EditText content_rule;
    CheckBox allday, weekday, weekend;

    int diffDay;
    //Duration duration;
    Period period;
    LocalDate startD=null, endD=null;
    LocalDate Date = null;
    String N,D;
    String jsonListR;
    int dayOfWeekNum = 0;
    int dayOfWeekendNum = 0;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writebucket_page4);

        // 뒤로 가기 버튼
        ImageButton button_back = (ImageButton) findViewById(R.id.btn_back);
        button_back.setOnClickListener(view -> {
            finish();
            //Intent intent = new Intent(getApplicationContext(), WriteBucket3Activity.class);
            //startActivity(intent);
        });

        date = (ToggleButton) findViewById(R.id.btn_date);
        rule = (ToggleButton) findViewById(R.id.btn_rule);

        layout_date = (LinearLayout) findViewById(R.id.layout_datepick);  // frame에 나타낼 화면 선택 (날짜)
        layout_rule = (LinearLayout) findViewById(R.id.layout_rule);      // frame에 나타낼 화면 선택 (규칙)
        goto_nextPage4 = (Button) findViewById(R.id.btn_writebucket4);


        // 날짜 선택
        date.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                changeView(0);

                date.setTextColor(Color.parseColor("#FFFFFF"));
                date.setBackgroundResource(R.drawable.button_shape_on);
                rule.setTextColor(Color.parseColor("#191970"));
                rule.setBackgroundResource(R.drawable.button_shape);
                goto_nextPage4.setVisibility(View.VISIBLE);


                recyclerView = (RecyclerView) findViewById(R.id.addlistView);
                dateitem.add(new DatePlanItem(dateitem.size()+1, ""));
                adapter = new MyRecyclerAdapter(dateitem);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                adapter.notifyItemInserted(0);

                btn_plus = (ImageButton) findViewById(R.id.plus_icon);
                btn_minus = (ImageButton) findViewById(R.id.minus_icon);

                // layout 더하기 버튼
                btn_plus.setOnClickListener(v-> {
                    recyclerView = (RecyclerView) findViewById(R.id.addlistView);
                    dateitem.add(new DatePlanItem(dateitem.size()+1, ""));
                    adapter = new MyRecyclerAdapter(dateitem);

                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    adapter.notifyItemInserted(0);

                    // layout 삭제 버튼 보이도록
                    btn_minus.setVisibility(View.VISIBLE);
                });

                // layout 삭제 버튼
                btn_minus.setOnClickListener(v-> {
                    recyclerView = (RecyclerView) findViewById(R.id.addlistView);
                    dateitem.remove(dateitem.size()-1);
                    adapter = new MyRecyclerAdapter(dateitem);

                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                });

                // 다음페이지로 이동 (4-> end) date
                goto_nextPage4.setOnClickListener(v-> {
                    WriteBucketResponse();
                });
            } else {
                date.setTextColor(Color.parseColor("#191970"));
                date.setBackgroundResource(R.drawable.button_shape);
            }

        });



        // 규칙적
        rule.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                changeView(1);

                allday = (CheckBox) findViewById(R.id.check_allday);   // 매일
                weekday = (CheckBox) findViewById(R.id.check_week);    // 평일
                weekend = (CheckBox) findViewById(R.id.check_weekend);  // 주말
                content_rule = (EditText) findViewById(R.id.text_content);  // 입력 내용

                rule.setTextColor(Color.parseColor("#FFFFFF"));
                rule.setBackgroundResource(R.drawable.button_shape_on);
                date.setTextColor(Color.parseColor("#191970"));
                date.setBackgroundResource(R.drawable.button_shape);
                goto_nextPage4.setVisibility(View.VISIBLE);

                String start = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "startDate");
                String end = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "endDate");
                System.out.println(start + " " + end);  // -> yyyy-m-d

                String [] S = start.split("-");
                String syear = S[0];
                String smonth = S[1];
                String sday = S[2];

                String [] E = end.split("-");
                String eyear = E[0];
                String emonth = E[1];
                String eday = E[2];

                startD = LocalDate.of(Integer.parseInt(syear), Integer.parseInt(smonth), Integer.parseInt(sday));
                endD = LocalDate.of(Integer.parseInt(eyear), Integer.parseInt(emonth), Integer.parseInt(eday));
                System.out.println("localDate : " + startD + " ~ " + endD);  // -> yyyy-mm-dd

                //period = Period.between(startD, endD);
                //diffDay = period.getDays(); // 차이 일수
                //System.out.println("차이일 수 : " + period.getDays() + " " + diffDay);

                LocalDateTime Sdate = startD.atStartOfDay();
                LocalDateTime Edate = endD.atStartOfDay();
                diffDay = (int) Duration.between(Sdate, Edate).toDays();
                System.out.println("차이일 수 : " + diffDay);

                String content = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "content_rule_write");

                allday.setOnCheckedChangeListener((buttonView2, isChecked2) -> { //7 (월, 화, 수, 목, 금, 토, 일)
                    if(isChecked) {
                        for(int i=0; i<=diffDay; i++) {
                            N = String.valueOf(i+1);  // 1,2,3,,, (순서)
                            Date = startD.plusDays(i);
                            D = Date.format(dtf);
                            System.out.println(N + ", " + D);

                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("orderNumb", N);
                            map.put("content", content);
                            map.put("planDate", D);

                            detail_Rule.add(map);
                            Log.d("message", "detailPlans : " + detail_Rule);
                        }
                        jsonListR = new Gson().toJson(detail_Rule);
                        System.out.println("detailPlans : " + jsonListR);
                    }
                });

                weekday.setOnCheckedChangeListener((buttonView2, isChecked2) -> {  //5 (월1, 화2, 수3, 목4, 금5)
                    if (isChecked) {
                        for(int i=0; i<=diffDay; i++) {
                            Date = startD.plusDays(i);
                            DayOfWeek day = Date.getDayOfWeek();
                            System.out.println(day);

                            int number = day.getValue();
                            System.out.println(number);
                            if(number == 1 || number == 2 || number == 3 || number == 4 || number == 5) {
                                dayOfWeekNum++;
                                N = String.valueOf(dayOfWeekNum);
                                D = Date.format(dtf);
                                System.out.println(N + ", " + D + ", " + content);

                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("orderNumb", N);
                                map.put("content", content);
                                map.put("planDate", D);

                                detail_Rule.add(map);
                                Log.d("message", "detailPlans : " + detail_Rule);
                            }
                        }
                        jsonListR = new Gson().toJson(detail_Rule);
                        System.out.println("detailPlans : " + jsonListR);
                    }
                });

                weekend.setOnCheckedChangeListener((buttonView2, isChecked2) -> {  //2  (토, 일)
                    if (isChecked) {
                        for(int i=0; i<=diffDay; i++) {
                            Date = startD.plusDays(i);
                            DayOfWeek day = Date.getDayOfWeek();
                            System.out.println(day);

                            int number = day.getValue();
                            System.out.println(number);
                            if(number == 6 || number == 7) {
                                dayOfWeekNum++;
                                N = String.valueOf(dayOfWeekNum);
                                D = Date.format(dtf);
                                System.out.println(N + ", " + D + ", " + content);

                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("orderNumb", N);
                                map.put("content", content);
                                map.put("planDate", D);

                                detail_Rule.add(map);
                                Log.d("message", "detailPlans : " + detail_Rule);
                            }
                        }
                        jsonListR = new Gson().toJson(detail_Rule);
                        System.out.println("detailPlans : " + jsonListR);
                    }
                });


                // 다음페이지로 이동 (4-> end) rule
                goto_nextPage4.setOnClickListener(v -> {
                    String content_R = content_rule.getText().toString();  //규칙적 페이지 작성 내용
                    SharedPrefManager.setPreference(WriteBucket4Activity.this, "content_rule_write", content_R);

                    // 규칙적 세부계획 미 클릭시
                    if (allday.isChecked()==false && weekday.isChecked()==false && weekend.isChecked()==false) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(WriteBucket4Activity.this);
                        builder.setTitle("알림")
                                .setMessage("버킷리스트 규칙적 세부계획을 실행 주기를 선택해주세요. ")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else if (content_R.trim().length() == 0 || content_R == null ) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(WriteBucket4Activity.this);
                        builder.setTitle("알림")
                                .setMessage("버킷리스트 규칙적 세부계획의 내용을 입력해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else { // 다음 작성페이지로 이동 + 데이터 전송
                        WriteBucketResponse2();
                    }
                });
            } else {
                rule.setTextColor(Color.parseColor("#191970"));
                rule.setBackgroundResource(R.drawable.button_shape);
            }
        });
    }

    
    private void changeView(int index) {
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        frame = (FrameLayout) findViewById(R.id.layout_page4);

        View view = null;
        switch (index) {
            case 0:
                view = layoutInflater.inflate(R.layout.writebucket_datepage, frame, false);
                break;
            case 1:
                view = layoutInflater.inflate(R.layout.writebucket_rulepage, frame, false);
                break;
        }

        if (view != null) {
            frame.addView(view);
        }
    }



    public void WriteBucketResponse() {   //date
        String token = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "token");
        String start = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "startDate");
        String end = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "endDate");
        String title = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "title");
        String content1 = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "content");
        String visibility = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "isVisible");

        ArrayList<Map<String ,Object>> detailPlans = new ArrayList<>();
        for(DatePlanItem t: dateitem){
            detailPlans.add(t.toMap());
        }
        String detailPlansJson = new Gson().toJson(detailPlans);

        String path = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "picture");
        String uri = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "uri");
        String tag = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "tags");

        token = token.replaceAll("\"", "");

        int visible = Integer.parseInt(visibility);
        File file = new File(path);
        String strFileName = file.getName();


        byte[] data = null;
        try {
            ContentResolver contentResolver = getBaseContext().getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(Uri.parse(uri));
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            String stringData = byteArrayOutputStream.toString();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            data = byteArrayOutputStream.toByteArray();
            for(int i =0; i<data.length; i++) {
                System.out.print((char)data[i]);
            }
            System.out.println("\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        RequestBody profile = RequestBody.create(MediaType.parse("multipart/form-data"), data);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("picture", strFileName, profile);


        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getWriteBucketResponse(token, start, end, title, content1, visible, detailPlansJson, fileToUpload, tag);


        uploadCall.enqueue(new Callback<JsonObject>() {
            String statusCode;
            final String success = "\"0\"";   // 작성 성공
            final String fail = "\"1\"";   // 작성 실패

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());
                Log.d("retrofit", "Data fetch success");

                //통신 성공
                if(response.isSuccessful()) {
                    try {
                        WriteBucketResponse status = new WriteBucketResponse(response.body());
                        Log.d("message" , "status :  " + status);
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println(statusCode);
                    System.out.println("버킷리스트 작성 상태 무사 저장");

                    if (statusCode.equals(success)) {
                        Toast.makeText(WriteBucket4Activity.this, "버킷리스트 작성을 완료하였습니다!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), WriteBucketEndActivity.class);
                        startActivity(intent);
                    } else if (statusCode.equals(fail)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(WriteBucket4Activity.this);
                        builder.setTitle("알림")
                                .setMessage("버킷리스트 작성하기에 실패하셨습니다. \n다시 확인해 주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(WriteBucket4Activity.this);
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
                Log.d("WriteBucket", "Internet contact error");
            }
        });
    }


    public void WriteBucketResponse2() {   //rule
        String token = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "token");
        String start = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "startDate");
        String end = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "endDate");
        String title = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "title");
        String content1 = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "content");
        String visibility = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "isVisible");
        String path = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "picture");
        String uri = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "uri");
        String tag = SharedPrefManager.getPreferenceString(WriteBucket4Activity.this, "tags");
        Log.d("message", "get_Data : " + token + start + ", " + end + ", " + title + ", " + content1 + ", " + Integer.parseInt(visibility) + ", " + path + ", " + uri + ", " + tag );

        token = token.replaceAll("\"", "");


        int visible = Integer.parseInt(visibility);
        File file = new File(path);
        String strFileName = file.getName();

        byte[] data = null;
        try {
            ContentResolver contentResolver = getBaseContext().getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(Uri.parse(uri));
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            String stringData = byteArrayOutputStream.toString();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            data = byteArrayOutputStream.toByteArray();
            for(int i =0; i<data.length; i++) {
                System.out.print((char)data[i]);
            }
            System.out.println("\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        RequestBody profile = RequestBody.create(MediaType.parse("multipart/form-data"), data);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("picture", strFileName, profile);


        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getWriteBucketResponse2(token, start, end, title, content1, visible, jsonListR, fileToUpload, tag);

        uploadCall.enqueue(new Callback<JsonObject>() {
            String statusCode;
            final String success = "\"0\"";   // 작성 성공
            final String fail = "\"1\"";   // 작성 실패

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());
                Log.d("retrofit", "Data fetch success");

                //통신 성공
                if (response.isSuccessful()) {
                    try {
                        WriteBucketResponse2 status = new WriteBucketResponse2(response.body());
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);

                    if (statusCode.equals(success)) {
                        Toast.makeText(WriteBucket4Activity.this, "버킷리스트 작성을 완료하였습니다!!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), WriteBucketEndActivity.class);
                        startActivity(intent);
                    } else if (statusCode.equals(fail)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(WriteBucket4Activity.this);
                        builder.setTitle("알림")
                                .setMessage("버킷리스트 작성하기에 실패하셨습니다. \n다시 확인해 주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(WriteBucket4Activity.this);
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
                Log.d("WriteBucket", "Internet contact error");
            }
        });
    }
}
