package com.example.bucket;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;



public class PointListAdapter extends RecyclerView.Adapter<PointListAdapter.ViewHolder> {

    private ArrayList<PointListItem> pointItem;


    @NonNull
    @Override
    public PointListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.pointlist_add, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PointListAdapter.ViewHolder holder, int position) {
        holder.onBind(pointItem.get(position));

        Log.d("message", "PointList : " + pointItem);
    }

    public PointListAdapter(ArrayList<PointListItem> list) { pointItem = list; }

    @Override
    public int getItemCount() { return pointItem.size(); }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView point;
        TextView date;
        TextView title;
        Context context;

        public ViewHolder(@NonNull View itemView , Context context) {
            super(itemView);
            this.context = context;

            point = (TextView) itemView.findViewById(R.id.getpoint);
            date = (TextView) itemView.findViewById(R.id.getpoint_date);
            title = (TextView) itemView.findViewById(R.id.bk_title);
        }

        void onBind(PointListItem item) {
            System.out.println("PointList: " + item);
            point.setText("+ " + item.getPoint() + " P");
            point.setTextColor(Color.parseColor("#0000FF"));
            date.setText(item.getDate_format());
            title.setText("[" + item.getBkTitle() + "]");
        }

    }
}
