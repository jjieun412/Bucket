package com.example.bucket;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ServiceAskListAdapter extends RecyclerView.Adapter<ServiceAskListAdapter.ViewHolder> {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    private List<ServiceAsklistItem> serviceitem = new ArrayList<>();

    Context con;

    @NonNull
    @Override
    public ServiceAskListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.service_asklist_add, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, context);
        this.con = context;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceAskListAdapter.ViewHolder holder, int position) {
        holder.onBind(serviceitem.get(position));
    }


    public ServiceAskListAdapter(List<ServiceAsklistItem> list) {
        serviceitem = list;
    }

    @Override
    public int getItemCount() {
        return serviceitem.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout1, layout2;
        TextView date, title;
        ImageView picture;
        TextView content;
        ImageButton noticeView, noticeHide;
        Button noticeDelete;
        Context context;
        ServiceAsklistItem sItem;


        public ViewHolder(@NonNull View itemView , Context context) {
            super(itemView);
            this.context = context;

            layout1 = (LinearLayout) itemView.findViewById(R.id.layout_askList1);
            layout2 = (LinearLayout) itemView.findViewById(R.id.layout_askList2);  // 숨겼다 말았다할 list
            date = (TextView) itemView.findViewById(R.id.ask_date);
            title = (TextView) itemView.findViewById(R.id.ask_title);
            picture = (ImageView) itemView.findViewById(R.id.ask_img);
            content = (TextView) itemView.findViewById(R.id.ask_content_list);
            noticeView = (ImageButton) itemView.findViewById(R.id.content_down);
            noticeDelete = (Button) itemView.findViewById(R.id.btn_serviceRemove);
            noticeHide = (ImageButton) itemView.findViewById(R.id.content_up);



            // 문의 내역 삭제 버튼
            // 나의 버킷리스트 삭제 버튼
            noticeDelete.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("해당 문의내역을 삭제 하시겠습니까?")
                        .setNegativeButton("취소", null)
                        .setPositiveButton("확인", (dialogInterface, i) -> ServiceAskListDeleteResponse(sItem.getBK_id(), sItem.getDate()))
                        .create()
                        .show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            });


            // 유의사항 보여주기 버튼
            noticeView.setOnClickListener(view -> {
                noticeView.setVisibility(View.INVISIBLE);
                layout2.setVisibility(View.VISIBLE);
                noticeDelete.setVisibility(View.VISIBLE);
            });

            // 유의사항 숨기기 버튼
            noticeHide.setOnClickListener(view -> {
                noticeView.setVisibility(View.VISIBLE);
                noticeDelete.setVisibility(View.GONE);
                layout2.setVisibility(View.GONE);
            });
        }

        void onBind(ServiceAsklistItem item) {
            System.out.println("ServiceItem: " + item);

            String D = item.getDate().substring(0,10);
            String str[] = D.split("-");
            String y = str[0];
            String m = str[1];
            String d = str[2];

            // 날짜는 문의한 현재 날짜 표시하기
            date.setText(" " + str[0] + "년 " + str[1] + "월 " + str[2] + "일 ");
            title.setText("[ " + item.getTitle() + " ]");
            Glide.with(context).load(item.getPic()).into(picture);
            content.setText(item.getContent());
            this.sItem = item;
        }

    }


    public void ServiceAskListDeleteResponse(Integer id, String requestDate) {
        String token = SharedPrefManager.getPreferenceString(con, "token");
        token = token.replaceAll("\"", "");




        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();
        Call<JsonObject> uploadCall = initMyApi.getServiceAskListDeleteResponse(token, id, requestDate);

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
                        ServiceAskListDeleteResponse status = new ServiceAskListDeleteResponse(response.body());
                        Log.d("message" , "status :  " + status);
                        statusCode = status.getStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(statusCode);


                    if (statusCode.equals(success)) {
                        System.out.println("문의내역 삭제 성공");

                        Intent intent = new Intent(con, ServiceAskListActivity.class);
                        con.startActivity(intent);
                    } else if (statusCode.equals(fail)) {
                        System.out.println("문의내역 삭제 실패");
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(con);
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
                Log.d("Delete_AskList", "Internet contact error");
            }
        });
    }

}