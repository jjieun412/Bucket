package com.example.bucket;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class PointRankAdapter extends RecyclerView.Adapter<PointRankAdapter.ViewHolder> {

    private ArrayList<PointRankItem> rankItem;


    @NonNull
    @Override
    public PointRankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.point_rank_add, parent, false);
        PointRankAdapter.ViewHolder viewHolder = new PointRankAdapter.ViewHolder(view, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PointRankAdapter.ViewHolder holder, int position) {
        holder.onBind(rankItem.get(position), position);

        Log.d("message", "PointRank : " + rankItem);
    }

    public PointRankAdapter(ArrayList<PointRankItem> list) { rankItem = list; }

    @Override
    public int getItemCount() { return rankItem.size(); }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView rankNum;
        CircleImageView profile;
        TextView nick;
        TextView point;
        Context context;

        public ViewHolder(@NonNull View itemView , Context context) {
            super(itemView);
            this.context = context;

            rankNum = (TextView) itemView.findViewById(R.id.user_rankNum);
            profile = (CircleImageView) itemView.findViewById(R.id.user_profile);
            nick = (TextView) itemView.findViewById(R.id.user_nickname);
            point = (TextView) itemView.findViewById(R.id.user_point);
        }

        void onBind(PointRankItem item, int position) {
            System.out.println("PointRank :  " + item);

            // 4등 부터 출력
            rankNum.setText(String.valueOf(position + 4));
            Glide.with(context).load(rankItem.get(position+4).getPic()).into(profile);
            nick.setText(" " + rankItem.get(position+4).getNick());
            point.setText(" " + rankItem.get(position+4).getPoint() + " P");
            //point.setTextColor(Color.parseColor("#0000FF"));
        }

    }
}
