package com.example.bucket;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BKDetailPlansAdapter_my extends RecyclerView.Adapter<BKDetailPlansAdapter_my.ViewHolder> {

    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    private List<MyBKListItem.detailPlan> mPlans;

    int bucket_id;
    Context con;

    @NonNull
    @Override
    public BKDetailPlansAdapter_my.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.bkdetail_date, parent, false);
        BKDetailPlansAdapter_my.ViewHolder viewHolder = new BKDetailPlansAdapter_my.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BKDetailPlansAdapter_my.ViewHolder holder, int position) {
        holder.onBind(mPlans.get(position), con);
    }


    public BKDetailPlansAdapter_my(List<MyBKListItem.detailPlan> list, int bucket_id, Context con) {
        mPlans = list;
        this.bucket_id = bucket_id;
        this.con = con;
    }

    @Override
    public int getItemCount() {
        return mPlans.size();
    }

    void addItem(MyBKListItem.detailPlan data) {
        mPlans.add(data);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView Date;
        TextView Content;
        ToggleButton complete;
        int achieved_state;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Date = (TextView) itemView.findViewById(R.id.detail_date);
            Content = (TextView) itemView.findViewById(R.id.detail_content);
            complete = (ToggleButton) itemView.findViewById(R.id.btn_complete_detail);
        }

        void onBind(MyBKListItem.detailPlan item, Context con) {
            System.out.println("BKDetailPlans: " + item);

            Date.setText(item.getPlanDate());
            Content.setText(item.getContent());
            if(item.getAchieved() == 1) {
                complete.setBackgroundResource(R.drawable.detail_complete);
                achieved_state = 1;
            } else {
                complete.setBackgroundResource(R.drawable.detail_notcomplete);
                achieved_state = 0;
            }

            complete.setOnClickListener(view -> {
                int achieved_info_to_send;
                if(complete.isChecked() == true) {
                    achieved_info_to_send = 1;
                    if(achieved_state == 1){
                        achieved_info_to_send = 0;
                    }
                    BKDetailAchievedResponse(con, bucket_id, item.getOrderNumb(), achieved_info_to_send);
                } else {
                    achieved_info_to_send = 0;
                    BKDetailAchievedResponse(con, bucket_id, item.getOrderNumb(), achieved_info_to_send);
                }
            });
        }

        public void BKDetailAchievedResponse(Context cont, int bucket_id, int orderNumb, int achieved) {
            String token = SharedPrefManager.getPreferenceString(cont, "token");
            token = token.replaceAll("\"", "");


            retrofitClient = RetrofitClient.getInstance();
            initMyApi = RetrofitClient.getRetrofitInterface();
            Call<JsonObject> uploadCall = initMyApi.getBKDetailAchievedResponse(token, bucket_id, orderNumb, achieved);


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
                            BKDetailAchievedResponse status = new BKDetailAchievedResponse(response.body());
                            Log.d("message" , "status :  " + status);
                            statusCode = status.getStatus();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println(statusCode);

                        if (statusCode.equals(success)) {
                            System.out.println("세부계획 달성여부 표시 성공");
                            if(achieved_state == 1){
                                complete.setBackgroundResource(R.drawable.detail_notcomplete);
                                achieved_state = 0;
                            }
                            else{
                                complete.setBackgroundResource(R.drawable.detail_complete);
                                achieved_state = 1;
                            }
                        } else if (statusCode.equals(fail)) {
                            System.out.println("세부계획 달성여부 표시 실패");
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(cont);
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

    }




}
