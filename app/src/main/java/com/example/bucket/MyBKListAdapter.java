package com.example.bucket;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;


public class MyBKListAdapter extends BaseAdapter {

    Context context;
    ArrayList<bucketList_my> items = new ArrayList<>();

    public MyBKListAdapter(Context mContext, ArrayList<bucketList_my> list) {
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
        BKListItemView_my view = null;

        /*
        if(convertView == null) {
            view = new BKListItemView_my(context.getApplicationContext(), (bucketList_my) getItem(position));
        } else {
            view = (BKListItemView_my) convertView;
        }

         */
        view = new BKListItemView_my(context.getApplicationContext(), (bucketList_my) getItem(position));

        bucketList_my bklist = items.get(position);
        view.setPicture(bklist.getPicture());
        view.setList_title(bklist.getTitle());
        if(bklist.getAchieved() == 1) {
            view.setComplete_img();
            view.setComplete_text();
        }

        return view;
    }
}