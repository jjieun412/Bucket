package com.example.bucket;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;



public class BKDetailImgAdapter extends RecyclerView.Adapter<BKDetailImgAdapter.ViewHolder> {

    private ArrayList<BKDetailImgItem> mimgs;

    @NonNull
    @Override
    public BKDetailImgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.bkdetail_image, parent, false);
        BKDetailImgAdapter.ViewHolder viewHolder = new BKDetailImgAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BKDetailImgAdapter.ViewHolder holder, int position) {

        holder.onBind(mimgs.get(position));
    }

/*
    public BKDetailTagsAdapter(ArrayList<BKDetailTagsItem> list) {
        mtags = list;
    }
 */

    @Override
    public int getItemCount() {
        return mimgs.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.mbklist_img);
        }

        void onBind(BKDetailImgItem item) {
            System.out.println("BKDetailImgs " + item);
            Glide.with(context).load(item.getPic_bk()).into(img);  // 버킷 이미지
            Glide.with(context).load(item.getPic_achieve()).into(img);  // 달성 이미지
            Glide.with(context).load(item.getPic_review()).into(img);   // 후기 이미지
        }

    }
}

