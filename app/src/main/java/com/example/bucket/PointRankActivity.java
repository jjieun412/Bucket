package com.example.bucket;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PointRankActivity extends AppCompatActivity {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    private RecyclerView recyclerView;
    private PointRankAdapter adapter;
    private ArrayList<PointRankItems> rItem;
    PointRankItems items;
    PointRankItem mitem;

    ImageButton btn_back;
    CircleImageView first_img, second_img, third_img;   // 1등, 2등, 3등  프로필이미지
    TextView first_nick, second_nick, third_nick;   // 1등, 2등, 3등  닉네임
    TextView pfirst, psecond, pthird;   // 1등, 2등, 3등  포인트

    LinearLayout myLayout1, myLayout2;
    ImageView myCrown, triangle;
    TextView myRank, myNick, myPoint;
    CircleImageView myImg;
    int index;




    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_rank);

        btn_back = (ImageButton) findViewById(R.id.btn_back);
        first_img = (CircleImageView) findViewById(R.id.profile_img1);
        second_img = (CircleImageView) findViewById(R.id.profile_img2);
        third_img = (CircleImageView) findViewById(R.id.profile_img3);
        first_nick = (TextView) findViewById(R.id.nickname1);
        second_nick = (TextView) findViewById(R.id.nickname2);
        third_nick = (TextView) findViewById(R.id.nickname3);
        pfirst = (TextView) findViewById(R.id.point1);
        psecond = (TextView) findViewById(R.id.point2);
        pthird = (TextView) findViewById(R.id.point3);

        myLayout1 = (LinearLayout) findViewById(R.id.rank_mine1);
        myLayout2 = (LinearLayout) findViewById(R.id.rank_mine2);
        myCrown = (ImageView) findViewById(R.id.crown_mine);
        triangle = (ImageView) findViewById(R.id.triangle);
        myRank = (TextView) findViewById(R.id.my_rankNum);
        myImg = (CircleImageView) findViewById(R.id.my_profile);
        myNick = (TextView) findViewById(R.id.my_nickname);
        myPoint = (TextView) findViewById(R.id.my_point);

        myLayout1.setBackgroundResource(R.drawable.radius_rank);
        myLayout2.setBackgroundResource(R.drawable.radius_rank2);


        // 뒤로 가기 버튼
        btn_back.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        });
    }


    protected void onStart() {
        super.onStart();

        PointResponse();
        PointRankResponse();
    }

    public void PointResponse() {
        String token = SharedPrefManager.getPreferenceString(PointRankActivity.this, "token");
        token = token.replaceAll("\"", "");

        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getPointResponse(token);

        uploadCall.enqueue(new Callback<JsonObject>() {
            String statusCode;
            String success = "\"0\"";
            String fail = "\"1\"";

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());
                Log.d("retrofit", "Data fetch success");

                PointResponse status = null;
                if (response.isSuccessful()) {
                    try {
                        status = new PointResponse(response.body());
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);


                    if(statusCode.equals(success)) {
                        System.out.println("포인트 조회 성공");
                        myPoint.setText(" " + status.getPoint() + " P");

                    } else if (statusCode.equals(fail)) {
                        System.out.println("포인트 조회 실패");
                    } else {
                        System.out.println("에러 발생");
                        AlertDialog.Builder builder = new AlertDialog.Builder(PointRankActivity.this);
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


    public void PointRankResponse() {
        String token = SharedPrefManager.getPreferenceString(PointRankActivity.this, "token");
        token = token.replaceAll("\"", "");

        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getPointRankResponse(token);

        uploadCall.enqueue(new Callback<JsonObject>() {
            String statusCode;
            String success = "\"0\"";
            String fail = "\"1\"";

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                System.out.println("response " + response);
                System.out.println("body " + response.body());
                Log.d("retrofit", "Data fetch success");

                PointRankResponse status = null;
                if (response.isSuccessful()) {
                    JsonArray temp = new JsonArray();
                    try {
                        status = new PointRankResponse(response.body());
                        //temp = status.details;
                        statusCode = status.getStatus();
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);


                    if(statusCode.equals(success)) {
                        System.out.println("랭킹 조회 성공");

                        items = new PointRankItems(status.getRanking().getAsJsonArray());
                        ArrayList<PointRankItem> tempList = items.list;
                        Log.d("message", "User_rankItems  : " + items);

                        // 1등
                        Glide.with(getApplicationContext()).load(tempList.get(0).getPic()).into(first_img);
                        first_nick.setText(tempList.get(0).getNick());
                        pfirst.setText(tempList.get(0).getPoint() + " P");

                        // 2등
                        Glide.with(getApplicationContext()).load(tempList.get(1).getPic()).into(second_img);
                        second_nick.setText(tempList.get(1).getNick());
                        psecond.setText(tempList.get(1).getPoint() + " P");

                        // 3등
                        Glide.with(getApplicationContext()).load(tempList.get(2).getPic()).into(third_img);
                        third_nick.setText(tempList.get(2).getNick());
                        pthird.setText(tempList.get(2).getPoint() + " P");


                        System.out.println("myinfo:");
                        System.out.println(status.getMyinfo());

                        mitem = new PointRankItem(status.getMyinfo());
                        Log.d("message", "My _rankItems  : " + mitem);

                        // 나의 랭킹
                        Glide.with(getApplicationContext()).load(mitem.getPic()).into(myImg);
                        myNick.setText(mitem.getNick());

                        System.out.println(tempList.get(0).getNick());
                        System.out.println(mitem.getNick());
                        if(tempList.get(0).getNick().equals(mitem.getNick())) {  // 내가 1등이면 왕관 이미지 불러오기, 색상변경, 랭킹 앞 번호 변경
                            myCrown.setVisibility(View.VISIBLE);
                            myRank.setText("1");
                            //triangle.setImageResource(R.drawable.triangle2);
                            myLayout1.setBackgroundColor(Color.parseColor("#FFFEBB"));
                            myLayout2.setBackgroundColor(Color.parseColor("#FDF98A"));
                            myLayout1.setBackgroundResource(R.drawable.radius_rank);
                            myLayout2.setBackgroundResource(R.drawable.radius_rank2);
                        } else { // 1등이 아니라면
                            myCrown.setVisibility(View.GONE);
                            /*
                            for(int j=1; j<tempList.size(); j++) {
                                String t = tempList.get(j).getNick();
                                if(t == mitem.getNick()) {
                                    String index = Arrays.asList(tempList).indexOf(t) + 1;
                                    myRank.setText(Arrays.asList(tempList).indexOf(t)+1);
                                }
                            }

                             */
                            myRank.setText("2");
                            triangle.setImageResource(R.drawable.rankline);
                            myLayout1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            myLayout2.setBackgroundColor(Color.parseColor("#D4D3D3"));
                            myLayout1.setBackgroundResource(R.drawable.radius_rank);
                            myLayout2.setBackgroundResource(R.drawable.radius_rank2);
                        }
                        recyclerView = (RecyclerView) findViewById(R.id.addRankView);
                        adapter = new PointRankAdapter(items.list);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(PointRankActivity.this));
                        adapter.notifyItemInserted(0);

                    } else if (statusCode.equals(fail)) {
                        System.out.println("랭킹 조회 실패");
                    } else {
                        System.out.println("에러 발생");
                        AlertDialog.Builder builder = new AlertDialog.Builder(PointRankActivity.this);
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
