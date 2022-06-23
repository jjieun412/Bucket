package com.example.bucket;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyBKListDetailActivity extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    private RecyclerView recyclerView;
    private BKDetailTagsAdapter adapter_tag;
    private BKDetailImgAdapter adapter_img;
    private BKDetailPlansAdapter_my adapter_plan_my;
    private ArrayList<BKDetailTagsItem> tagsItems;
    private ArrayList<BKDetailImgItem> imgsItems;
    private List<MyBKListItem.detailPlan> plansItems;

    ImageButton btn_back, btn_delete, btn_edit;
    Button btn_review, btn_achieve;
    ToggleButton btn_complete;

    ImageView bk_image;
    LinearLayout linearLayout_img, linearLayout_tag;
    TextView tags, title, date, visibility, bk_content;
    TextView complete_text;
    TextView count_img;
    ImageView complete_img;
    List<View> mytags = new ArrayList<>();

    List<View> myimgs = new ArrayList<>();
    MyBKListItem mybkitem;
    ViewGroup viewGroup;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybklist_detail);

        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_delete = (ImageButton) findViewById(R.id.btn_delete);
        btn_edit = (ImageButton) findViewById(R.id.btn_edit);
        btn_review = (Button) findViewById(R.id.btn_review);
        btn_achieve = (Button) findViewById(R.id.btn_achieve);
        complete_img = (ImageView) findViewById(R.id.complete_mark_img);
        complete_text = (TextView) findViewById(R.id.complete_mark_text);
        //count_img = (TextView) findViewById(R.id.count_img);


        recyclerView = (RecyclerView) findViewById(R.id.addDetailsView);
        btn_complete = (ToggleButton) findViewById(R.id.btn_complete_detail);


        viewGroup = (ViewGroup) findViewById(R.id.layout_bkdetail_tag);
        bk_image = (ImageView) findViewById(R.id.mbklist_img);
        title = (TextView) findViewById(R.id.mbklist_title);
        date = (TextView) findViewById(R.id.mbklist_date);
        visibility = (TextView) findViewById(R.id.mbklist_visible);
        bk_content = (TextView) findViewById(R.id.mbklist_content);
        // detailPlan 추가

        // 뒤로 가기 버튼
        btn_back.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MyBKListActivity.class);
            startActivity(intent);
        });

        // 나의 버킷리스트 삭제 버튼
        btn_delete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MyBKListDetailActivity.this);
            builder.setMessage("버킷리스트를 삭제 하시겠습니까?")
                    .setNegativeButton("취소", null)
                    .setPositiveButton("확인", (dialogInterface, i) -> MyBKListDeleteResponse())
                    .create()
                    .show();
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        // 나의 버킷리스트 수정 버튼
        btn_edit.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), EditBucket1Activity.class);
            startActivity(intent);
        });

        // 나의 버킷리스트 후기 등록하기 버튼 ( 달성 후 가능)
        btn_review.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MyBKListReviewActivity.class);
            startActivity(intent);
        });

        // 나의 버킷리스트 달성 인증 버튼(이미지 등록해서 인증)
        btn_achieve.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MyBKListAchieveActivity.class);
            startActivity(intent);
        });
    }

    // 타유저 상세조회 get
    protected void onStart() {
        super.onStart();

        BKListDetailResponse();
        Log.d("msg", "Doing OnStart");
    }

    protected void onStop() {
        super.onStop();

        for(View t : mytags)
        {
            if(t != null) {
                viewGroup.removeView(t);
            }
        }

        for(View t : myimgs)
        {
            if(t != null) {
                viewGroup.removeView(t);
            }
        }

        Log.d("msg", "Doing OnStop");
    }

    // 나의 버킷 상세조회 GET
    public void BKListDetailResponse() {
        String token = SharedPrefManager.getPreferenceString(MyBKListDetailActivity.this, "token");
        token = token.replaceAll("\"", "");

        Intent intent = getIntent();
        String id = intent.getStringExtra("BK_ID");
        System.out.println("id: " + id);
        Integer bucketId = Integer.parseInt(id);
        System.out.println("id: " + bucketId);

        SharedPrefManager.setPreference(MyBKListDetailActivity.this, "bkID_for_achieved", id);
        SharedPrefManager.setPreference(MyBKListDetailActivity.this, "bkID_for_serviceAsk", id);
        SharedPrefManager.setPreference(MyBKListDetailActivity.this, "bkID_for_review", id);
        SharedPrefManager.setPreference(MyBKListDetailActivity.this, "bkID_for_edit", id);

        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getBKListDetailResponse(token, bucketId);


        uploadCall.enqueue(new Callback<JsonObject>() {
            String statusCode;
            final String success = "\"0\"";   // 조회 성공
            final String fail = "\"1\"";   // 조회 실패

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());
                Log.d("retrofit", "Data fetch success");

                BKListDetailResponse status = null;
                //통신 성공
                if(response.isSuccessful()) {
                    try {
                        status = new BKListDetailResponse(response.body());
                        Log.d("message" , "status :  " + status);
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);


                    if (statusCode.equals(success)) {
                        System.out.println("나의 버킷 조회 성공");
                        mybkitem = new MyBKListItem(status.getBucketList());
                        Log.d("message", "bkitem  : " + mybkitem);

                        imgsItems = new ArrayList<>();
                        String Img_bk = mybkitem.getPicture();   // 버킷이미지
                        String Img_achieve = mybkitem.getAchievedPicture();  // 버킷달성이미지
                        List<String> Img_review = mybkitem.getReviewPicture();  // 버킷후기이미지
                        List<String> Images = new ArrayList<>();
                        Images.add(Img_bk);
                        Images.add(Img_achieve);
                        for(int i=0; i<Img_review.size(); i++) {
                            String str = Img_review.get(i);
                            System.out.println("reviewImg : " + str + "_");
                            Images.add(str);
                        }

                        linearLayout_img = (LinearLayout) findViewById(R.id.layout_bkdetail_img);
                        LayoutInflater inflater1 = getLayoutInflater();
                        int insertedImageCount = 0;
                        for(int i=0; i<Images.size(); i++) {
                            String temp1 = Images.get(i);
                            System.out.println("path:");
                            System.out.println(temp1);

                            if(temp1 == null)
                            {
                                continue;
                            }

                            View view = inflater1.inflate(R.layout.bkdetail_image, null);
                            bk_image = (ImageView) view.findViewById(R.id.mbklist_img);
                            System.out.println("memory:");
                            System.out.println(bk_image);


                            //Glide.with(getApplicationContext()).load(temp).signature(new StringSignature(uuidAsString)).into(bk_image);


                            myimgs.add(view);

                            viewGroup = (ViewGroup) findViewById(R.id.layout_bkdetail_img);
                            viewGroup.addView(view, insertedImageCount);
                            insertedImageCount++;

                            UUID uuid = UUID.randomUUID();
                            String uuidAsString = uuid.toString();

                            Glide.with(getApplicationContext()).load(temp1).signature(new StringSignature(uuidAsString)).into(bk_image);
                            //count_img.setText(" " + temp1+1 + " / " + Images.size() + " ");
                        }


                        title.setText(mybkitem.getTitle());

                        tagsItems = new ArrayList<>();
                        List<String> Tag = mybkitem.getTags();  // 태그 가져옴
                        linearLayout_tag = (LinearLayout) findViewById(R.id.layout_bkdetail_tag);
                        LayoutInflater inflater2 = getLayoutInflater();
                        for(int i=0; i<Tag.size(); i++) {
                            View view = inflater2.inflate(R.layout.bkdetail_tags, null);
                            tags = (TextView) view.findViewById(R.id.mbklist_tag);
                            System.out.println(tags);

                            String str = Tag.get(i);
                            String temp = "#" + str;
                            System.out.println(temp);

                            tags.setText(temp);
                            System.out.println(tags);

                            mytags.add(view); // textview list로 저장

                            viewGroup = (ViewGroup) findViewById(R.id.layout_bkdetail_tag);
                            viewGroup.addView(view, i);
                        }

                        recyclerView = (RecyclerView) findViewById(R.id.addDetailsView);
                        plansItems = mybkitem.getDetailplans();
                        adapter_plan_my = new BKDetailPlansAdapter_my(plansItems, mybkitem.bucketId, MyBKListDetailActivity.this);
                        recyclerView.setAdapter(adapter_plan_my);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MyBKListDetailActivity.this));
                        adapter_plan_my.notifyItemInserted(0);

                        String stDate = mybkitem.getStartDate();
                        String endDate = mybkitem.getEndDate();
                        date.setText(stDate + " ~ " + endDate);
                        if(mybkitem.getIsVisible() == 1){
                            visibility.setText("게시물 - 공개");
                        } else {
                            visibility.setText("게시물 - 비공개");
                        }
                        bk_content.setText(mybkitem.getBucketContent());

                        // 달성이미지
                        if(mybkitem.getAchievedPicture() == null) { // 실패
                            btn_delete.setEnabled(true);
                            btn_edit.setEnabled(true);
                            btn_review.setEnabled(false);
                            btn_achieve.setEnabled(true);
                        } else {  // 성공
                            //Glide.with(getApplicationContext()).load(mybkitem.getAchievedPicture()).into(bk_image);
                            btn_delete.setEnabled(false);
                            btn_edit.setEnabled(false);
                            btn_edit.setBackgroundResource(R.drawable.radius);

                            btn_review.setBackgroundColor(Color.parseColor("#191970"));
                            btn_review.setEnabled(true);
                            btn_review.setBackgroundResource(R.drawable.radius);

                            btn_achieve.setEnabled(false);
                            btn_achieve.setText("달성 인증 완료");
                            btn_achieve.setBackgroundColor(Color.parseColor("#FFDEAD"));
                            btn_achieve.setBackgroundResource(R.drawable.radius);
                        }

                        for(String temp : mybkitem.getReviewPicture()) {
                            //Glide.with(getApplicationContext()).load(temp).into(bk_image);
                        }
                        if(mybkitem.getReviewPicture().size() == 0) { // 실패
                            btn_review.setEnabled(true);
                        } else {  // 성공
                            complete_img.setVisibility(View.VISIBLE);
                            complete_text.setVisibility(View.VISIBLE);
                            date.setText(stDate + " ~ " + endDate + "  (완료) ");

                            btn_edit.setBackgroundColor(Color.parseColor("#A0A0A0"));
                            btn_edit.setEnabled(false);
                            btn_edit.setBackgroundResource(R.drawable.radius);

                            btn_review.setBackgroundColor(Color.parseColor("#483D8B"));
                            btn_review.setEnabled(false);
                            btn_review.setText("후기 등록 완료");
                            btn_review.setBackgroundResource(R.drawable.radius);

                            btn_achieve.setText("달성 인증 완료");
                            btn_achieve.setBackgroundColor(Color.parseColor("#FFDEAD"));
                            btn_achieve.setEnabled(false);
                            btn_achieve.setBackgroundResource(R.drawable.radius);
                        }

                        // 버킷 제목, 버킷 내용, 공개여부, 해당카테고리, 버킷이미지, 버킷시작/끝 날짜
                        SharedPrefManager.setPreference(MyBKListDetailActivity.this, "BK_title", mybkitem.getTitle());
                        SharedPrefManager.setPreference(MyBKListDetailActivity.this, "BK_content", mybkitem.getBucketContent());
                        SharedPrefManager.setPreference(MyBKListDetailActivity.this, "BK_visibility", String.valueOf(mybkitem.getIsVisible()));
                        SharedPrefManager.setPreference(MyBKListDetailActivity.this, "BK_tags", String.valueOf(Tag));
                        SharedPrefManager.setPreference(MyBKListDetailActivity.this, "BK_img", mybkitem.getPicture());
                        SharedPrefManager.setPreference(MyBKListDetailActivity.this, "BK_sdate", stDate);
                        SharedPrefManager.setPreference(MyBKListDetailActivity.this, "BK_edate", endDate);
                    } else if (statusCode.equals(fail)) {
                        System.out.println("나의 버킷 조회 실패");
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyBKListDetailActivity.this);
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
                Log.d("set_another_user_bk_detail", "Internet contact error");
            }
        });
    }


    // 버킷리스트 삭제 통신
    public void MyBKListDeleteResponse() {
        String token = SharedPrefManager.getPreferenceString(MyBKListDetailActivity.this, "token");
        token = token.replaceAll("\"", "");

        Intent intent = getIntent();
        String id = intent.getStringExtra("BK_ID");
        System.out.println("id: " + id);
        Integer bucketId = Integer.parseInt(id);
        System.out.println("id: " + bucketId);

        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getMyBKListDeleteResponse(token, bucketId);

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
                        MyBKListDeleteResponse status = new MyBKListDeleteResponse(response.body());
                        Log.d("message" , "status :  " + status);
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);


                    if (statusCode.equals(success)) {
                        System.out.println("나의 버킷 삭제 성공");
                        Toast.makeText(MyBKListDetailActivity.this, "버킷리스트 삭제를 완료하였습니다!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MyBKListActivity.class);
                        startActivity(intent);
                    } else if (statusCode.equals(fail)) {
                        System.out.println("나의 버킷 삭제 실패");
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyBKListDetailActivity.this);
                        builder.setTitle("알림")
                                .setMessage("예기치 못한 오류가 발생하였습니다. \n고객센터로 문의해주십시오.")
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
