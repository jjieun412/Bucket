package com.example.bucket;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyBKListReviewActivity extends AppCompatActivity {


    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    private final int GET_GALLERY_IMAGE = 300;
    private final int REQUEST_IMAGE_CODE = 300;

    EditText review_content;
    ImageButton btn_back;
    FrameLayout frameLayout;
    List<View> myIMGs = new ArrayList<>();
    ImageButton delete_img1;
    ImageView set_img1, set_img2, set_img3, set_img4, set_img5, set_img6, set_img7, set_img8, set_img9,set_img10;
    ImageView add_img1;
    Button btn_review;

    private RecyclerView recyclerView;
    private ReviewImageAdapter adapter;
    private ArrayList<Uri> uriList = new ArrayList<>();
    private ArrayList<String> imgList = new ArrayList<>();

    String mediaPath, CameraUri;
    Uri selectedImageUri;
    Bitmap bitmap;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybklist_review);

        btn_back = (ImageButton) findViewById(R.id.btn_back);
        review_content = (EditText) findViewById(R.id.review_content);
        set_img1 = (ImageView) findViewById(R.id.review_img);
        add_img1 = (ImageView) findViewById(R.id.btn_imgAdd1);
        delete_img1 = (ImageButton) findViewById(R.id.btn_imgDelete);
        btn_review = (Button) findViewById(R.id.btn_set_review);


        // 뒤로 가기 버튼
        btn_back.setOnClickListener(view -> {
            finish();
        });


        // 후기 이미지 업로드(1~10)
        add_img1.setOnClickListener(view -> {
            AlertChoice();
        });

        delete_img1.setOnClickListener(view -> {
            set_img1.setImageResource(0);
            delete_img1.setVisibility(View.GONE);
            add_img1.setVisibility(View.VISIBLE);
        });

        recyclerView = findViewById(R.id.reviewImg_recycler);



        // 리뷰 작성 완료 버튼
        btn_review.setOnClickListener(view -> {
            String content = review_content.getText().toString().trim();

            if (content.trim().length() == 0 || content == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyBKListReviewActivity.this);
                builder.setTitle("알림")
                        .setMessage("버킷리스트의 후기 내용을 작성해 주세요.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                MyBKListReviewResponse();
            }
        });
    }

    public void AlertChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyBKListReviewActivity.this);
        builder.setTitle("사진 업로드 방식 선택")
                .setPositiveButton("갤러리", (dialogInterface, i) -> TakeIMGAlbum())
                .setNegativeButton("취소", null)
                .create()
                .show();
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // 갤러리에서 이미지 가져오기
    public void TakeIMGAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GET_GALLERY_IMAGE);
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
            set_img1.setImageURI(selectedImageUri);
            add_img1.setVisibility(View.GONE);
            delete_img1.setVisibility(View.VISIBLE);
            Log.d("message", "uri(from gallery) = " + selectedImageUri);

            Cursor cursor = getContentResolver().query(selectedImageUri, null, null, null, null);
            cursor.moveToFirst();
            mediaPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            Log.d("message", "path(from gallery) = " + mediaPath);

            SharedPrefManager.setPreference(MyBKListReviewActivity.this, "review_pic", mediaPath);
            SharedPrefManager.setPreference(MyBKListReviewActivity.this, "review_uri", String.valueOf(selectedImageUri));
            Log.d("message", "send_Data_review : " + mediaPath + ",  " + selectedImageUri);

        } else if (requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK && data.hasExtra("data")) {
            bitmap = (Bitmap) data.getExtras().get("data");
            set_img1.setImageBitmap(bitmap);
            add_img1.setVisibility(View.GONE);
            delete_img1.setVisibility(View.VISIBLE);
            Log.d("message", "bitmap(from camera) = " + bitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            CameraUri = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null);
            Log.d("message", "uri(from camera) = " + CameraUri);

            Cursor cursor = getContentResolver().query(Uri.parse(CameraUri), null, null, null, null);
            cursor.moveToFirst();
            mediaPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            Log.d("message", "path(from camera) = " + mediaPath);

            SharedPrefManager.setPreference(MyBKListReviewActivity.this, "review_pic", mediaPath);
            SharedPrefManager.setPreference(MyBKListReviewActivity.this, "review_uri", String.valueOf(CameraUri));
            Log.d("message", "send_Data_review : " + mediaPath + ",  " + CameraUri);

        } else {
            Log.d("MyBKListReview_IMG", "upload fail");
        }
    }


    public void MyBKListReviewResponse() {
        String content = review_content.getText().toString().trim();
        String token = SharedPrefManager.getPreferenceString(MyBKListReviewActivity.this, "token");
        token = token.replaceAll("\"", "");
        String uri = SharedPrefManager.getPreferenceString(MyBKListReviewActivity.this, "review_uri");

        String id = SharedPrefManager.getPreferenceString(MyBKListReviewActivity.this, "bkID_for_review");
        System.out.println("id: " + id);
        Integer bucketId = Integer.parseInt(id);
        System.out.println("id: " + bucketId);

        System.out.println(token + "\n" + bucketId + "\n" + content + "\n" + uri);

        // 후기 이미지가 1장 일 경우
        File file = new File(mediaPath);
        String strFileName = file.getName();
        Log.d("message", "file = " + file);
        Log.d("message", "fileName = " + strFileName);

        byte[] data = null;
        try {
            ContentResolver contentResolver = getBaseContext().getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(Uri.parse(uri));
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            String stringData = byteArrayOutputStream.toString();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            data = byteArrayOutputStream.toByteArray();
            for (int i = 0; i < data.length; i++) {
                System.out.print((char) data[i]);
            }
            System.out.println("\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        RequestBody profile = RequestBody.create(MediaType.parse("multipart/form-data"), data);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("picture", strFileName, profile);

        System.out.println(token + "\n" + bucketId + "\n" + content + "\n" + fileToUpload);

        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getMyBKListReviewResponse(token, bucketId, content, fileToUpload);

        uploadCall.enqueue(new Callback<JsonObject>() {
            String statusCode;
            final String success = "\"0\"";
            final String fail = "\"1\"";

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());
                Log.d("retrofit", "Data fetch success");

                MyBKListReviewResponse status = null;
                //통신 성공
                if (response.isSuccessful()) {
                    try {
                        status = new MyBKListReviewResponse(response.body());
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);

                    if (statusCode.equals(success)) {
                        System.out.println("나의 버킷 후기 등록 성공");
                        Toast.makeText(MyBKListReviewActivity.this, "버킷리스트 후기등록을 완성하였습니다. \n버킷리스트를 완료하였습니다!!! 수고했습니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MyBKListActivity.class);
                        startActivity(intent);
                    } else if (statusCode.equals(fail)) {
                        System.out.println("나의 버킷 후기 등록 실패");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyBKListReviewActivity.this);
                        builder.setTitle("알림")
                                .setMessage("버킷리스트 후기 등록에 실패하였습니다. \n다시 시도해 주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyBKListReviewActivity.this);
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