package com.example.bucket;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;


public class EditBucket2Activity extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    private final int GET_GALLERY_IMAGE = 300;
    private final int REQUEST_IMAGE_CODE = 300;

    ImageButton gallery, camera, internet;
    FrameLayout frameLayout;
    LinearLayout web_search;
    ImageView set_image;
    Button goto_nextPage2;

    String mediaPath, CameraUri;
    Uri selectedImageUri;
    Bitmap bitmap;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editbucket_page2);

        // 뒤로 가기 버튼
        ImageButton button_back = (ImageButton) findViewById(R.id.btn_back);
        button_back.setOnClickListener(view -> {
            finish();
        });


        gallery = (ImageButton) findViewById(R.id.galleryIcon);
        camera = (ImageButton) findViewById(R.id.cameraIcon);
        internet = (ImageButton) findViewById(R.id.internetIcon);

        frameLayout = (FrameLayout) findViewById(R.id.framLayout);
        web_search = (LinearLayout) findViewById(R.id.web_search);
        set_image = (ImageView) findViewById(R.id.set_Image);
        goto_nextPage2 = (Button) findViewById(R.id.btn_editbucket2);

        String bkProfile = SharedPrefManager.getPreferenceString(EditBucket2Activity.this, "BK_img");
        Glide.with(getApplicationContext()).load(bkProfile).into(set_image);

        // 작성 다음페이지 버튼(2->3)
        goto_nextPage2.setOnClickListener(view -> {
            if (set_image.getDrawable() == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditBucket2Activity.this);
                builder.setTitle("알림")
                        .setMessage("버킷리스트의 대표 이미지를 등록해 주세요.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                Intent intent = new Intent(getApplicationContext(), EditBucket3Activity.class);
                startActivity(intent);
            }
        });


        // 갤러리에서 사진 가져오기
        gallery.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, GET_GALLERY_IMAGE);
        });


        // 카메라로 사진 촬영해서 사진 가져오기
        camera.setOnClickListener(view -> {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.d("message", "권한 설정 완료");
                } else {
                    Log.d("message", "권한 설정 요청");
                    ActivityCompat.requestPermissions(EditBucket2Activity .this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }

            Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CODE);
        });

        /*
        // 인터넷으로 검색해서 사진 다운로드해서 가져오기
        internet.setOnClickListener(view -> {
            web_search.setVisibility(View.VISIBLE);

        });
         */
    }


    // 결과값 가져오기
    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 갤러리
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Log.d("message", "pick image from gallery");
            // 이미지를 갤러리에서 클릭해 사진을 이미지 뷰에 보여주는 코드
            selectedImageUri = data.getData();
            set_image.setImageURI(selectedImageUri);
            Log.d("message", "uri(from gallery) = " + selectedImageUri);

            Cursor cursor = getContentResolver().query(selectedImageUri, null, null, null, null);
            cursor.moveToFirst();
            mediaPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            Log.d("message", "path(from gallery) = " + mediaPath);

            // 데이터 4페이지로 전송
            SharedPrefManager.setPreference(EditBucket2Activity .this, "edit_picture", mediaPath);
            SharedPrefManager.setPreference(EditBucket2Activity .this, "edit_uri", String.valueOf(selectedImageUri));
            Log.d("message", "send_Data_2 : " + mediaPath + ",  " + selectedImageUri);

        } else if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK && data.hasExtra("data")) {
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

            SharedPrefManager.setPreference(EditBucket2Activity .this, "edit_picture", mediaPath);
            SharedPrefManager.setPreference(EditBucket2Activity .this, "edit_uri", String.valueOf(CameraUri));
            Log.d("message", "send_Data_2 : " + mediaPath + ",  " + CameraUri);

        } else {
            Log.d("WriteBucket_page2", "upload fail");
        }
    }
}
