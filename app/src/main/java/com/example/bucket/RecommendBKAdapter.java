package com.example.bucket;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;


public class RecommendBKAdapter extends BaseAdapter {

    Context context;
    ArrayList<bucketLists_user> items = new ArrayList<>();

    public RecommendBKAdapter(Context mContext, ArrayList<bucketLists_user> list) {
        this.context = mContext;
        this.items = list;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //화면에 보여질 각각의 item을 위한 뷰를 생성
        //BKListItemView view = new BKListItemView(getApplicationContext());

        BKListItemView_user view = null;

        view = new BKListItemView_user(context.getApplicationContext(), (bucketLists_user) getItem(position));

        bucketLists_user bklist = items.get(position);
        view.setPicture(bklist.getPicture());
        view.setList_title(bklist.getTitle());
        view.setHeart(bklist.getHeart());
        if(bklist.getAchieved() == 1) {
            view.setComplete_img();
            view.setComplete_text();
        }

        return view;
    }
}

