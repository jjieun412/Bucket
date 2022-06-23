package com.example.bucket;



import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class JoinActivity extends AppCompatActivity {


    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    private final int GET_GALLERY_IMAGE = 300;

    String mediaPath;
    Uri selectedImageUri;
    List<String> Tags = new ArrayList<String>();
    ImageView profile, setImage1, setImage2;
    ToggleButton nick_check, email_check;    // 중복체크 버튼
    EditText fakename, ID, PW1, PW2;
    ToggleButton travel, food, challenge, exercise, develop, diet, hobby, work, language, finance, certificate, health, book, anything;

    Pattern emailPattern = Patterns.EMAIL_ADDRESS;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_page);


        // 뒤로 가기 버튼
        ImageButton button_back = findViewById(R.id.btn_back);
        button_back.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });


        //기입 항목, 중복체크버튼
        fakename = findViewById(R.id.signName);
        ID = findViewById(R.id.ID_email);
        PW1 = findViewById(R.id.PW);
        PW2 = findViewById(R.id.PW_check);
        setImage1 = findViewById(R.id.setImage1);    // 새로운 비번 유효성 검사 이미지 ㅊ체크
        setImage2 = findViewById(R.id.setImage2);    // 비번 재확인 이미지 체크
        nick_check = findViewById(R.id.btn_doublecheck1);
        email_check = findViewById(R.id.btn_doublecheck2);
        profile = findViewById(R.id.set_profile);  // 프로필 사진 (선택사항)

/*
        //비번 유효성 확인 (옆의 이미지 뷰를 통해)
        PW1.addTextChangedListener(new TextWatcher() {
            final String pswd = PW1.getText().toString();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9]+)(?=.*[~`!@#$%&*()-]).{8,16}$", pswd)) {
                    setImage1.setImageResource(R.drawable.okay_check);
                } else {
                    setImage1.setImageResource(R.drawable.error);
                }
            }
        });

 */


        //비번 일치 여부 확인 (옆의 이미지 뷰를 통해)
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


        // 닉네임 중복 버튼
        nick_check.setOnClickListener(v -> {
            String nickname = fakename.getText().toString();

            if (nickname.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                builder.setTitle("알림")
                        .setMessage("닉네임을 입력하지 않았습니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                NicknameResponse();
            }
        });

        // 아이디(이메일 중복버튼)
        email_check.setOnClickListener(v -> {
            String email = ID.getText().toString();

            if (email.equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                builder.setTitle("알림")
                        .setMessage("이메일을 입력하지 않았습니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                EmailResponse();
            }
        });

        nick_check.setOnCheckedChangeListener((buttonView, isChecked) -> {
            nick_check.setEnabled(!isChecked);
        });

        email_check.setOnCheckedChangeListener((buttonView, isChecked) -> {
            email_check.setEnabled(!isChecked);
        });


        // 관심카테고리 선택
        travel = findViewById(R.id.btn_travel);
        food = findViewById(R.id.btn_food);
        challenge = findViewById(R.id.btn_challenge);
        exercise = findViewById(R.id.btn_exercise);
        develop = findViewById(R.id.btn_develop);
        diet = findViewById(R.id.btn_diet);
        hobby = findViewById(R.id.btn_hobby);
        work = findViewById(R.id.btn_work);
        language = findViewById(R.id.btn_language);
        finance = findViewById(R.id.btn_finance);
        certificate = findViewById(R.id.btn_certificate);
        health = findViewById(R.id.btn_health);
        book = findViewById(R.id.btn_book);
        anything = findViewById(R.id.btn_anything);


        travel.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                travel.setTextColor(Color.parseColor("#FFFFFF"));
                travel.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("여행");
                System.out.println("list : " +  Tags);
            } else {
                travel.setTextColor(Color.parseColor("#808080"));
                travel.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("여행");
                System.out.println("list : " +  Tags);
            }
        });

        food.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                food.setTextColor(Color.parseColor("#FFFFFF"));
                food.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("맛집");
                System.out.println("list : " +  Tags);
            } else {
                food.setTextColor(Color.parseColor("#808080"));
                food.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("맛집");
                System.out.println("list : " +  Tags);
            }
        });

        challenge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                challenge.setTextColor(Color.parseColor("#FFFFFF"));
                challenge.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("도전");
                System.out.println("list : " +  Tags);
            } else {
                challenge.setTextColor(Color.parseColor("#808080"));
                challenge.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("도전");
                System.out.println("list : " +  Tags);
            }
        });

        exercise.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                exercise.setTextColor(Color.parseColor("#FFFFFF"));
                exercise.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("운동");
                System.out.println("list : " +  Tags);
            } else {
                exercise.setTextColor(Color.parseColor("#808080"));
                exercise.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("운동");
                System.out.println("list : " +  Tags);
            }
        });

        develop.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                develop.setTextColor(Color.parseColor("#FFFFFF"));
                develop.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("자기계발");
                System.out.println("list : " +  Tags);
            } else {
                develop.setTextColor(Color.parseColor("#808080"));
                develop.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("자기계발");
                System.out.println("list : " +  Tags);
            }
        });

        diet.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                diet.setTextColor(Color.parseColor("#FFFFFF"));
                diet.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("자기관리");
                System.out.println("list : " +  Tags);
            } else {
                diet.setTextColor(Color.parseColor("#808080"));
                diet.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("자기관리");
                System.out.println("list : " +  Tags);
            }
        });

        hobby.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                hobby.setTextColor(Color.parseColor("#FFFFFF"));
                hobby.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("취미");
                System.out.println("list : " +  Tags);
            } else {
                hobby.setTextColor(Color.parseColor("#808080"));
                hobby.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("취미");
                System.out.println("list : " +  Tags);
            }
        });

        work.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                work.setTextColor(Color.parseColor("#FFFFFF"));
                work.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("입시/취업");
                System.out.println("list : " +  Tags);
            } else {
                work.setTextColor(Color.parseColor("#808080"));
                work.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("입시/취업");
                System.out.println("list : " +  Tags);
            }
        });

        language.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                language.setTextColor(Color.parseColor("#FFFFFF"));
                language.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("외국어");
                System.out.println("list : " +  Tags);
            } else {
                language.setTextColor(Color.parseColor("#808080"));
                language.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("외국어");
                System.out.println("list : " +  Tags);
            }
        });

        finance.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                finance.setTextColor(Color.parseColor("#FFFFFF"));
                finance.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("재테크");
                System.out.println("list : " +  Tags);
            } else {
                finance.setTextColor(Color.parseColor("#808080"));
                finance.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("재테크");
                System.out.println("list : " +  Tags);
            }
        });

        certificate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                certificate.setTextColor(Color.parseColor("#FFFFFF"));
                certificate.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("자격증");
                System.out.println("list : " +  Tags);
            } else {
                certificate.setTextColor(Color.parseColor("#808080"));
                certificate.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("자격증");
                System.out.println("list : " +  Tags);
            }
        });

        health.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                health.setTextColor(Color.parseColor("#FFFFFF"));
                health.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("건강");
                System.out.println("list : " +  Tags);
            } else {
                health.setTextColor(Color.parseColor("#808080"));
                health.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("건강");
                System.out.println("list : " +  Tags);
            }
        });

        book.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                book.setTextColor(Color.parseColor("#FFFFFF"));
                book.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("독서");
                System.out.println("list : " +  Tags);
            } else {
                book.setTextColor(Color.parseColor("#808080"));
                book.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("독서");
                System.out.println("list : " +  Tags);
            }
        });

        anything.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                anything.setTextColor(Color.parseColor("#FFFFFF"));
                anything.setBackgroundColor(Color.parseColor("#191970"));
                Tags.add("기타");
                System.out.println("list : " +  Tags);
            } else {
                anything.setTextColor(Color.parseColor("#808080"));
                anything.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("기타");
                System.out.println("list : " +  Tags);
            }
        });


        // 회원가입 버튼 -> 성공 시 웰컴페이지로 이동      --> 유효성 검사 필요
        Button join = findViewById(R.id.btn_join);
        join.setOnClickListener(view -> {

            String name = fakename.getText().toString();
            String email = ID.getText().toString();
            String pw1 = PW1.getText().toString();
            String pw2 = PW2.getText().toString();

            // 회원가입 정보 미 입력시
            if( name.trim().length() == 0 || name == null ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                builder.setTitle("알림")
                        .setMessage("닉네임을 입력하지 않았습니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else if (nick_check.isChecked() == false) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                builder.setTitle("알림")
                        .setMessage("닉네임 중복체크를 해주세요.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else if ( email.trim().length() == 0 || email == null ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                builder.setTitle("알림")
                        .setMessage("이메일을 입력하지 않았습니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else if(!emailPattern.matcher(email).matches()) {
                Toast.makeText(JoinActivity.this, "올바르지 않는 이메일 형식입니다.", Toast.LENGTH_SHORT).show();
                return;
            } else if (email_check.isChecked() == false) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                builder.setTitle("알림")
                        .setMessage("이메일 중복체크를 해주세요.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else if ( pw1.trim().length() == 0 || pw1 == null ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                builder.setTitle("알림")
                        .setMessage("비밀번호를 입력하지 않았습니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else if ( pw2.trim().length() == 0 || pw2 == null ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                builder.setTitle("알림")
                        .setMessage("비밀번호 재확인을 하지 않았습니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else if (travel.isChecked()==false  && food.isChecked()==false && challenge.isChecked()==false && exercise.isChecked()==false && develop.isChecked()==false && diet.isChecked()==false
                    && hobby.isChecked()==false && work.isChecked()==false && language.isChecked()==false && finance.isChecked()==false && certificate.isChecked()==false &&  health.isChecked()==false ) {
                // 카테고리 선택 최소 1개 이상 해야하는데 안함
                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                builder.setTitle("알림")
                        .setMessage("관심 카테고리 선택을 최소 1개 이상 해야 합니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            else { // 회원가입 통신
                JoinResponse();
            }
        });


        // 프로필 설정 사진 가져오기 (갤러리에서)
        profile.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            startActivityForResult(intent, GET_GALLERY_IMAGE);
        });

    }


    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // 이미지를 갤러리에서 클릭해 사진을 이미지 뷰에 보여주는 코드
            selectedImageUri = data.getData();
            profile.setImageURI(selectedImageUri);
            Log.d("message", "uri = " + selectedImageUri);


            Cursor cursor = getContentResolver().query(selectedImageUri, null, null, null, null);
            cursor.moveToFirst();
            mediaPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            Log.d("message", "path = " + mediaPath);

        } else {
            Log.d("Join", "upload fail");
        }
    }



    public void JoinResponse() {
        String nameString = fakename.getText().toString().trim();
        String signIDString = ID.getText().toString().trim();
        String signPWString = PW1.getText().toString().trim();

        Intent send_data = new Intent(getApplicationContext(), LoginActivity.class);
        send_data.putExtra("fakename", nameString);
        startActivity(send_data);

        File file = new File(mediaPath);
        String strFileName = file.getName();
        Log.d("message", "file = " + file);
        Log.d("message", "fileName = " + strFileName);

        byte[] data = null;
        try {
            ContentResolver contentResolver = getBaseContext().getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(selectedImageUri);
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

        String jsonStr = new Gson().toJson(Tags);
        System.out.println("list : " + jsonStr);

        //MediaType mediaType = MediaType.parse("text/plain");
        /*RequestBody body = new MultipartBody.Builder()f
                .setType(MultipartBody.FORM)
                .addFormDataPart("nickName", nameString)
                .addFormDataPart("email", signIDString)
                .addFormDataPart("password", signPWString)
                .addFormDataPart("tags", jsonStr)
                .addFormDataPart("file", strFileName, RequestBody.create(MediaType.parse("application/octet-stream"),file))
                .build();
         */



        //JoinRequest에 사용자가 입력한 닉네임, 이메일, 비밀번호, 카테고리 , 이미지 저장 --> 이미지 추가해야함
        //JoinRequest joinRequest = new JoinRequest(nameString, signIDString, signPWString, Tags, fileToUpload);
        Call<JsonObject> uploadCall = initMyApi.getJoinResponse( nameString, signIDString, signPWString, jsonStr, fileToUpload);
        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();

        //joinRequest에 저장된 데이터와 함께 joininterface에서 정의한 getJoinResponse 함수 실행 후 응답받음
        uploadCall.enqueue(new Callback<JsonObject>() {

            String statusCode;
            final String success = "\"0\"";
            final String fail = "\"1\"";

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());
                Log.d("retrofit", "Data fetch success");

                //통신 성공
                if(response.isSuccessful()) {
                    try {
                        //response.body()를 status에 저장
                        JoinResponse status = new JoinResponse(response.body());
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (statusCode.equals(success)) {
                        SharedPrefManager.setPreference(JoinActivity.this, "tags", jsonStr);
                        SharedPrefManager.setPreference(JoinActivity.this, "nickname", nameString);
                        SharedPrefManager.setPreference(JoinActivity.this, "profilepath",mediaPath);
                        SharedPrefManager.setPreference(JoinActivity.this, "profileuri", String.valueOf(selectedImageUri));

                        String nickname = fakename.getText().toString().trim();
                        Toast.makeText(JoinActivity.this, nickname + "님 환영합니다!!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.putExtra("nickname :", nickname);
                        startActivity(intent);
                    } else if (statusCode.equals(fail)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                        builder.setTitle("알림")
                                .setMessage("회원가입에 실패하셨습니다. \n다시 확인해 주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                        builder.setTitle("알림")
                                .setMessage("회원가입 오류가 발생하였습니다. \n다시 시도해 주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Join", "Internet contact error");
            }
        });
    }



    public void NicknameResponse() {
        String nickname = fakename.getText().toString().trim();

        //NicknameRequest에 사용자가 입력한 닉네임 저장
        NicknameRequest nicknameRequest = new NicknameRequest(nickname);
        System.out.println("the nicknameRequest is: " + nicknameRequest);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();

        //joinRequest에 저장된 데이터와 함께 joininterface에서 정의한 getJoinResponse 함수 실행 후 응답받음
        initMyApi.getNicknameResponse(nicknameRequest).enqueue(new Callback<JsonObject>() {

            String statusCode;
            final String success = "\"0\"";
            final String fail = "\"1\"";

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());

                Log.d("retrofit", "Data fetch success");

                //통신 성공
                if (response.isSuccessful()) {
                    try {
                        //response.body()를 status에 저장
                        NicknameResponse status = new NicknameResponse(response.body());
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println(statusCode);
                    System.out.println("닉네임 중복 상태 무사 저장");

                    if (statusCode.equals(success)) {
                        String nickname = fakename.getText().toString().trim();
                        Toast.makeText(JoinActivity.this, nickname + "은 사용가능한 닉네임 입니다!!", Toast.LENGTH_LONG).show();
                    } else if (statusCode.equals(fail)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                        builder.setTitle("알림")
                                .setMessage("이미 존재하는 닉네임 입니다. \n다시 입력해 주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                        builder.setTitle("알림")
                                .setMessage("닉네임 중복 오류가 발생하였습니다. \n다시 시도해 주세요.")
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


    public void EmailResponse() {
        String signID = ID.getText().toString().trim();

        //EmailRequest에 사용자가 입력한 이메일 저장
        EmailRequest emailRequest = new EmailRequest(signID);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();


        initMyApi.getEmailResponse(emailRequest).enqueue(new Callback<JsonObject>() {

            String statusCode;
            final String success = "\"0\"";
            final String fail = "\"1\"";

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());

                Log.d("retrofit", "Data fetch success");

                //통신 성공
                if (response.isSuccessful()) {
                    try {
                        //response.body()를 status에 저장
                        EmailResponse status = new EmailResponse(response.body());
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println(statusCode);
                    System.out.println("이메일 중복 상태 무사 저장");

                    if (statusCode.equals(success)) {
                        String email = ID.getText().toString().trim();
                        Toast.makeText(JoinActivity.this, email + "은 사용가능한 이메일 입니다!!", Toast.LENGTH_LONG).show();
                    } else if (statusCode.equals(fail)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                        builder.setTitle("알림")
                                .setMessage("이미 존재하는 이메일 입니다. \n다시 입력해 주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                        builder.setTitle("알림")
                                .setMessage("이메일 중복 오류가 발생하였습니다. \n다시 시도해 주세요.")
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
