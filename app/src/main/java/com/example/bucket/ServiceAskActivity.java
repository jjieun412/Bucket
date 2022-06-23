package com.example.bucket;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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


public class ServiceAskActivity extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    private final int REQUEST_IMAGE_CODE = 300;

    EditText ask_content;
    ImageView ask_imageView;
    String mediaPath, CameraUri;
    Bitmap bitmap;
    Button btn_askImg, btn_cancel, btn_receipt;
    ImageButton btn_back;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_ask);

        btn_back = (ImageButton) findViewById(R.id.btn_back);
        ask_content = (EditText) findViewById(R.id.ask_content);
        ask_imageView = (ImageView) findViewById(R.id.set_Image);
        btn_askImg = (Button) findViewById(R.id.btn_askImage);
        System.out.println(btn_askImg);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_receipt = (Button) findViewById(R.id.btn_receipt);


        btn_back.setOnClickListener(view -> {
            finish();
        });

        btn_cancel.setOnClickListener(view -> {
            finish();
        });

        btn_receipt.setOnClickListener(view -> {
            String askContent = ask_content.getText().toString();

            if( askContent.trim().length() == 0 || askContent == null ) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ServiceAskActivity.this);
                builder.setTitle("알림")
                        .setMessage("문의 내용을 입력하지 않았습니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else if (ask_imageView.getDrawable() == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ServiceAskActivity.this);
                builder.setTitle("알림")
                        .setMessage("문의할 사진을 설정해 주세요.(달성사진)")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                ServiceAskResponse();
            }
        });


        btn_askImg.setOnClickListener(view -> {
            ask_imageView.setVisibility(View.VISIBLE);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.d("message", "권한 설정 완료");
                } else {
                    Log.d("message", "권한 설정 요청");
                    ActivityCompat.requestPermissions(ServiceAskActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }
            Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
            ask_imageView.setImageBitmap(bitmap);
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


    public void ServiceAskResponse() {
        String token = SharedPrefManager.getPreferenceString(ServiceAskActivity.this, "token");
        token = token.replaceAll("\"", "");
        String content_ask = ask_content.getText().toString().trim();

        String id = SharedPrefManager.getPreferenceString(ServiceAskActivity.this, "bkID_for_serviceAsk");
        Integer bucketId = Integer.parseInt(id);

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
        Call<JsonObject> uploadCall = initMyApi.getServiceAskResponse(token, bucketId, content_ask, fileToUpload);

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
                        ServiceAskResponse status = new ServiceAskResponse(response.body());
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);


                    if(statusCode.equals(success)) {
                        System.out.println("달성 인증 문의 성공");
                        Toast.makeText(ServiceAskActivity.this, "문의가 접수되었습니다. \n문의 내역은 나의 설정에서 확인하실 수 있습니다.", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), ServiceAskListActivity.class);
                        startActivity(intent);

                    } else if (statusCode.equals(fail)) {
                        System.out.println("달성 인증 문의 실패");
                        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceAskActivity.this);
                        builder.setMessage("문의 접수하는 것을 실패하였습니다. \n다시 시도해 주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        System.out.println("에러 발생");
                        AlertDialog.Builder builder = new AlertDialog.Builder(ServiceAskActivity.this);
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