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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MySetPageEditActivity extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    private final int GET_GALLERY_IMAGE = 300;

    ImageButton btn_back, camera;
    Button btn_edit_clear;
    FrameLayout editProfile;
    CircleImageView profile;
    Uri selectedImageUri;
    String imagePath;
    EditText nickname;
    List<String> Tags = new ArrayList<String>();
    ToggleButton travel, food, challenge, exercise, develop, diet, hobby, work, language, finance, certificate, health, book, anything;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myset_edit);

        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_edit_clear = (Button) findViewById(R.id.btn_clear);
        nickname = (EditText) findViewById(R.id.my_nickname);
        editProfile = (FrameLayout) findViewById(R.id.layout_profileIMG);
        profile = (CircleImageView) findViewById(R.id.profile_img);
        camera = (ImageButton) findViewById(R.id.btn_camera);



        // 뒤로 가기 버튼
        btn_back.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MySetPageActivity.class);
            startActivity(intent);
        });

        // 프로필 설정 사진 가져오기 (갤러리에서)
        editProfile.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, GET_GALLERY_IMAGE);
        });

        btn_edit_clear.setOnClickListener(view -> {
            String nick = nickname.getText().toString();

            // 회원가입 정보 미 입력시
            if( nick.trim().length() == 0 || nick == null ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MySetPageEditActivity.this);
                builder.setTitle("알림")
                        .setMessage("닉네임을 입력하지 않았습니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else if (profile.getDrawable() == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MySetPageEditActivity.this);
                builder.setTitle("알림")
                        .setMessage("프로필 사진을 설정해 주세요.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else if (travel.isChecked()==false  && food.isChecked()==false && challenge.isChecked()==false && exercise.isChecked()==false && develop.isChecked()==false && diet.isChecked()==false
                    && hobby.isChecked()==false && work.isChecked()==false && language.isChecked()==false && finance.isChecked()==false && certificate.isChecked()==false &&  health.isChecked()==false ) {
                // 카테고리 선택 최소 1개 이상 해야하는데 안함
                AlertDialog.Builder builder = new AlertDialog.Builder(MySetPageEditActivity.this);
                builder.setTitle("알림")
                        .setMessage("관심 카테고리 선택을 최소 1개 이상 해야 합니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            else { // 회원가입 통신
                MySetPageEditResponse();
            }
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
            } else {
                anything.setTextColor(Color.parseColor("#808080"));
                anything.setBackgroundColor(Color.parseColor("#FFFFFF"));
                Tags.remove("기타");
            }
            System.out.println("list : " +  Tags);
        });
    }


    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            profile.setImageURI(selectedImageUri);
            camera.setVisibility(View.GONE);
            Log.d("message", "uri = " + selectedImageUri);

            Cursor cursor = getContentResolver().query(selectedImageUri, null, null, null, null);
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            Log.d("message", "path = " + imagePath);

        } else {
            Log.d("MySetPageEdit", "upload fail");
        }
    }


    public void MySetPageEditResponse() {
        String token = SharedPrefManager.getPreferenceString(MySetPageEditActivity.this, "token");
        token = token.replaceAll("\"", "");
        String nameString = nickname.getText().toString().trim();

        File file = new File(imagePath);
        String strFileName = file.getName();

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

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getMySetPageEditResponse(token, jsonStr, nameString, fileToUpload);

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
                        MySetPageEditResponse status = new MySetPageEditResponse(response.body());
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);


                    if(statusCode.equals(success)) {
                        System.out.println("내 정보 수정 성공");
                        Intent intent = new Intent(getApplicationContext(), MySetPageActivity.class);
                        startActivity(intent);

                    } else if (statusCode.equals(fail)) {
                        System.out.println("내 정보 수정 실패");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MySetPageEditActivity.this);
                        builder.setTitle("알림")
                                .setMessage("내 정보를 수정하는데 실패하였습니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else {
                        System.out.println("에러 발생");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MySetPageEditActivity.this);
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