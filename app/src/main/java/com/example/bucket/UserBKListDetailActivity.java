package com.example.bucket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserBKListDetailActivity extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    private LinearLayout linearLayout;
    private RecyclerView recyclerView;
    private BKDetailTagsAdapter adapter_tag;
    private BKDetailPlansAdapter adapter_plan;
    private ArrayList<BKDetailTagsItem> tagsItems;
    private List<UserBKListItem.detailPlan> plansItems;

    LinearLayout layout_tag, layout_detail;
    ImageView bk_image;
    ImageButton btn_back;
    ToggleButton btn_heart, btn_complete;
    CircleImageView profile;
    String temp;
    TextView nick, title, tags, date, visibility, bk_content, planDate, planContent;
    UserBKListItem bkitem;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userbklist_detail);

        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_heart = (ToggleButton) findViewById(R.id.favorite_mark);
        System.out.println("toggle button memory: " + btn_heart);

        bk_image = (ImageView) findViewById(R.id.bklist_img);
        profile = (CircleImageView) findViewById(R.id.profile_img);
        nick = (TextView) findViewById(R.id.bklist_nickname);
        title = (TextView) findViewById(R.id.bklist_title);

        layout_tag = (LinearLayout) findViewById(R.id.layout_bkdetail_tag);
        tags = (TextView) findViewById(R.id.mbklist_tag);

        recyclerView = (RecyclerView) findViewById(R.id.addDetailsView);
        layout_detail = (LinearLayout) findViewById(R.id.layout_detailplan);
        planDate = (TextView) findViewById(R.id.detail_date);
        planContent = (TextView) findViewById(R.id.detail_content);

        date = (TextView) findViewById(R.id.bklist_date);
        visibility = (TextView) findViewById(R.id.visibility);
        bk_content = (TextView) findViewById(R.id.bklist_content);

        // 뒤로 가기 버튼
        btn_back.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        });
    }

    // 타유저 상세조회 get
    protected void onStart() {
        super.onStart();

        btn_heart.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                btn_heart.setBackgroundDrawable(getResources().getDrawable(R.drawable.favorite_full));
                BKListHeartFullResponse();
            } else {
                btn_heart.setBackgroundDrawable(getResources().getDrawable(R.drawable.heart_null));
                BKListHeartNullResponse();
            }
        });

        BKListDetailResponse();

    }

    // 타유저 버킷 상세조회 GET
    public void BKListDetailResponse() {
        String token = SharedPrefManager.getPreferenceString(UserBKListDetailActivity.this, "token");
        token = token.replaceAll("\"", "");

        Intent intent = getIntent();
        String id = intent.getStringExtra("BK_ID");
        System.out.println("id: " + id);
        Integer bucketId = Integer.parseInt(id);
        System.out.println("id: " + bucketId);


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
                        System.out.println("타유저 버킷 조회 성공");
                        bkitem = new UserBKListItem(status.getBucketList());
                        // ≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤≤detail = new UserBKListItem(status.getDetailPlans());
                        Log.d("message", "bkitem  : " + bkitem);

                        Glide.with(getApplicationContext()).load(bkitem.getUserProfile()).into(profile);
                        nick.setText(bkitem.getNickname());
                        title.setText(bkitem.getTitle());

                        tagsItems = new ArrayList<>();
                        List<String> Tag = bkitem.getTags();  // 태그 가져옴
                        linearLayout = (LinearLayout) findViewById(R.id.layout_bkdetail_tag);
                        LayoutInflater inflater = getLayoutInflater();
                        for(int i=0; i<Tag.size(); i++) {
                            View view = inflater.inflate(R.layout.bkdetail_tags, null);
                            tags = (TextView) view.findViewById(R.id.mbklist_tag);
                            System.out.println(tags);

                            String str = Tag.get(i);
                            String temp = "#" + str;
                            System.out.println(temp);

                            tags.setText(temp);
                            System.out.println(tags);

                            ViewGroup viewGroup = (ViewGroup) findViewById(R.id.layout_bkdetail_tag);
                            viewGroup.addView(view, 0);
                        }


                        recyclerView = (RecyclerView) findViewById(R.id.addDetailsView);
                        plansItems = bkitem.getDetailplans();
                        adapter_plan = new BKDetailPlansAdapter(plansItems, bkitem.bucketId, UserBKListDetailActivity.this);
                        recyclerView.setAdapter(adapter_plan);
                        recyclerView.setLayoutManager(new LinearLayoutManager(UserBKListDetailActivity.this));
                        adapter_plan.notifyItemInserted(0);


                        String stDate = bkitem.getStartDate();
                        String endDate = bkitem.getEndDate();
                        date.setText(stDate + " ~ " + endDate);
                        if(bkitem.getIsVisible() == 1){
                            visibility.setText("게시물 - 공개");
                        } else {
                            visibility.setText("게시물 - 비공개");
                        }
                        if(bkitem.getHeart() == 1){
                            btn_heart.setBackgroundDrawable(getResources().getDrawable(R.drawable.favorite_full));
                        } else {
                            btn_heart.setBackgroundDrawable(getResources().getDrawable(R.drawable.heart_null));
                        }
                        bk_content.setText(bkitem.getBucketContent());

                        // detailplan, 버킷 이미지, 후기 이미지, 달성이미지 추가 필요
                        Glide.with(getApplicationContext()).load(bkitem.getUserProfile()).into(profile);

                    } else if (statusCode.equals(fail)) {
                        System.out.println("타유저 버킷 조회 실패");
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserBKListDetailActivity.this);
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

    private void getBKDetailTagsItem() {
        tagsItems = new ArrayList<>();
        List<String> Tag = bkitem.getTags();

        for(int i=0; i<Tag.size(); i++) {
            String str = Tag.get(i);
            BKDetailTagsItem data = new BKDetailTagsItem(str);
            data.setTag("#" + str);

            adapter_tag.addItem(data);
        }
        adapter_tag.notifyDataSetChanged();
    }


    // 버킷리스트 하트 누르기 통신
    public void BKListHeartFullResponse() {
        String token = SharedPrefManager.getPreferenceString(UserBKListDetailActivity.this, "token");
        token = token.replaceAll("\"", "");
        Intent intent = getIntent();
        String id = intent.getStringExtra("BK_ID");
        System.out.println("id: " + id);
        Integer bucketId = Integer.parseInt(id);
        System.out.println("id: " + bucketId);

        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getBKListHeartFullResponse(token, bucketId);


        uploadCall.enqueue(new Callback<JsonObject>() {
            String statusCode;
            final String success = "\"0\"";   // 성공
            final String fail = "\"1\"";   // 실패

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());
                Log.d("retrofit", "Data fetch success");

                //통신 성공
                if(response.isSuccessful()) {
                    try {
                        BKListHeartFullResponse status = new BKListHeartFullResponse(response.body());
                        Log.d("message" , "status :  " + status);
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);

                    if (statusCode.equals(success)) {
                        System.out.println("타유저 버킷 하트누르기 성공");
                    } else if (statusCode.equals(fail)) {
                        System.out.println("타유저 버킷 하트누르기 실패");
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserBKListDetailActivity.this);
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
                Log.d("getHeartFullResponse", "Internet contact error");
            }
        });
    }


    // 버킷리스트 하트 누르기 취소 통신
    public void BKListHeartNullResponse() {
        String token = SharedPrefManager.getPreferenceString(UserBKListDetailActivity.this, "token");
        token = token.replaceAll("\"", "");
        Intent intent = getIntent();
        String id = intent.getStringExtra("BK_ID");
        System.out.println("id: " + id);
        Integer bucketId = Integer.parseInt(id);
        System.out.println("id: " + bucketId);

        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getBKListHeartNullResponse(token, bucketId);


        uploadCall.enqueue(new Callback<JsonObject>() {
            String statusCode;
            final String success = "\"0\"";   // 성공
            final String fail = "\"1\"";   // 실패

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());
                Log.d("retrofit", "Data fetch success");

                //통신 성공
                if(response.isSuccessful()) {
                    try {
                        BKListHeartNullResponse status = new BKListHeartNullResponse(response.body());
                        Log.d("message" , "status :  " + status);
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);

                    if (statusCode.equals(success)) {
                        System.out.println("타유저 버킷 하트누르기 취소 성공");
                    } else if (statusCode.equals(fail)) {
                        System.out.println("타유저 버킷 하트누르기 취소 실패");
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserBKListDetailActivity.this);
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
                Log.d("getHeartNullResponse", "Internet contact error");
            }
        });
    }
}