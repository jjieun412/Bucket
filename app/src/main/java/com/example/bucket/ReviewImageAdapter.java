package com.example.bucket;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class ReviewImageAdapter extends RecyclerView.Adapter<ReviewImageAdapter.ViewHolder> {

    private ArrayList<Uri> mData = null;
    private Context mContext = null;

    ReviewImageAdapter(ArrayList<Uri> list, Context context) {
        mData = list;
        mContext = context;
    }

    @NonNull
    @Override
    public ReviewImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.mybklist_review_addimg, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewImageAdapter.ViewHolder holder, int position) {

        ImageView setImg = holder.reviewPic;
        Uri image_uri = mData.get(position);
        Glide.with(mContext).load(image_uri).into(holder.reviewPic);

        ImageButton delete = holder.remove_img;
        delete.setOnClickListener(view -> {
            setImg.setImageResource(0);
            setImg.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        });
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView reviewPic;
        ImageButton remove_img;
        Context context;

        public ViewHolder(@NonNull View itemView , Context context) {
            super(itemView);
            this.context = context;

            reviewPic = (ImageView) itemView.findViewById(R.id.review_img);
            remove_img = (ImageButton) itemView.findViewById(R.id.btn_imgDelete);
        }
    }
}