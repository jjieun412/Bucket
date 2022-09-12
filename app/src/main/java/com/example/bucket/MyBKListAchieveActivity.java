package com.example.bucket;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyBKListAchieveActivity extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    private final int REQUEST_IMAGE_CODE = 300;

    ImageButton btn_back, camera;
    ImageView set_image;
    Button btn_certificate, btn_gotoService;
    LinearLayout layout_notAchieved;

    String mediaPath, CameraUri;
    Bitmap bitmap;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybklist_achieve);


        btn_back = (ImageButton) findViewById(R.id.btn_back);
        camera = (ImageButton) findViewById(R.id.cameraIcon);
        set_image = (ImageView) findViewById(R.id.set_Image);
        btn_certificate = (Button) findViewById(R.id.btn_certificate_achieve);
        layout_notAchieved = (LinearLayout) findViewById(R.id.layout_ifnotachieved);
        btn_gotoService = (Button) findViewById(R.id.btn_gotoservice);




        // 뒤로 가기 버튼
        btn_back.setOnClickListener(view -> {
            finish();
        });


        // 달성 인증 버튼
        btn_certificate.setOnClickListener(view -> {
            if (set_image.getDrawable() == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyBKListAchieveActivity.this);
                builder.setTitle("알림")
                        .setMessage("버킷리스트의 달성을 인증하기 위한 이미지를 등록해 주세요.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                MyBKListAchievedResponse();
            }
        });


        // 카메라로 사진 촬영해서 사진 가져오기
        camera.setOnClickListener(view -> {
            set_image.setVisibility(View.VISIBLE);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.d("message", "권한 설정 완료");
                } else {
                    Log.d("message", "권한 설정 요청");
                    ActivityCompat.requestPermissions(MyBKListAchieveActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }

            Intent imageTakeIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CODE);
        });
    }


    // 결과값 가져오기
    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK && data.hasExtra("data")) {
            bitmap = (Bitmap) data.getExtras().get("data");
            set_image.setImageBitmap(bitmap);
            Log.d("message", "bitmap(from camera) = " + bitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            CameraUri = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "IMG" + Calendar.getInstance().getTime(), null);
            Log.d("message", "uri(from camera) = " + CameraUri);

            Cursor cursor = getContentResolver().query(Uri.parse(CameraUri), null, null, null, null);
            cursor.moveToFirst();
            mediaPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            Log.d("message", "path(from camera) = " + mediaPath);

        } else {
            Log.d("MyBKListAchieve_IMG", "upload fail");
        }
    }


    public void MyBKListAchievedResponse() {
        String token = SharedPrefManager.getPreferenceString(MyBKListAchieveActivity.this, "token");
        token = token.replaceAll("\"", "");


        String id = SharedPrefManager.getPreferenceString(MyBKListAchieveActivity.this, "bkID_for_achieved");
        System.out.println("id: " + id);
        Integer bucketId = Integer.parseInt(id);
        System.out.println("id: " + bucketId);

        File file = new File(mediaPath);
        String strFileName = file.getName();

        byte[] data = null;
        try {
            ContentResolver contentResolver = getBaseContext().getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(Uri.parse(CameraUri));
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

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getMyBKListAchievedResponse(token, bucketId, fileToUpload);

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
                        MyBKListAchievedResponse status = new MyBKListAchievedResponse(response.body());
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);


                    if(statusCode.equals(success)) {
                        System.out.println("달성 인증 성공");

                        Toast.makeText(MyBKListAchieveActivity.this, "버킷리스트 달성인증에 성공하였습니다. \n후기를 등록해 주세요!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MyBKListActivity.class);
                        startActivity(intent);

                    } else if (statusCode.equals(fail)) {
                        System.out.println("달성 인증 실패");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyBKListAchieveActivity.this);
                        builder.setMessage("달성 인증에 실패하였습니다.\n다시 시도하거나 고객센터로 문의해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        btn_certificate.setBackgroundColor(Color.RED);
                        btn_certificate.setText("달성 인증 재시도");
                        layout_notAchieved.setVisibility(View.VISIBLE);
                        btn_certificate.setBackgroundResource(R.drawable.radius);

                        // 달성 안될시 문의하기로 이동
                        btn_gotoService.setOnClickListener(view -> {
                            Intent intent = new Intent(getApplicationContext(), ServiceAskActivity.class);
                            startActivity(intent);
                        });

                    } else {
                        System.out.println("에러 발생");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyBKListAchieveActivity.this);
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
