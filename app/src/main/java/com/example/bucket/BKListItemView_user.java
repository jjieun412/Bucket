package com.example.bucket;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.UUID;


public class BKListItemView_user extends LinearLayout {
    ImageView picture;
    TextView list_title;
    ImageView heart;
    ImageView complete_img;
    TextView complete_text;
    bucketLists_user bk;
    Integer BK_ID;

    public BKListItemView_user(Context context, bucketLists_user bk) {
        super(context);
        this.bk = bk;
        init(context);
    }

    public BKListItemView_user(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_add, this, true);

        Log.d("message", "inflater : " + inflater );
        //View view = LayoutInflater.from(context).inflate(R.layout.layout_add, this);
        picture = (ImageView) findViewById(R.id.list_image);
        list_title = (TextView) findViewById(R.id.list_title);
        heart = (ImageView) findViewById(R.id.favorite_mark);
        complete_text = (TextView) findViewById(R.id.complete_mark_text_list);
        complete_img = (ImageView) findViewById(R.id.complete_mark_img_list);

        picture.setOnClickListener(v -> {
            BK_ID = bk.getBucketId();
            Intent intent = new Intent(context, UserBKListDetailActivity.class);
            intent.putExtra("BK_ID", String.valueOf(BK_ID));
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } );

        list_title.setOnClickListener(v -> {
            BK_ID = bk.getBucketId();
            Intent intent = new Intent(context, UserBKListDetailActivity.class);
            intent.putExtra("BK_ID", String.valueOf(BK_ID));
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        });

        if(bk.getAchieved() == 1) {
            complete_text.setVisibility(View.VISIBLE);
            complete_img.setVisibility(View.VISIBLE);
        } else {
            complete_text.setVisibility(View.GONE);
            complete_img.setVisibility(View.GONE);
        }
    }

    public void setPicture(String pic) {
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();

        System.out.println("pictureUrl is :" + pic);
        Glide.with(getContext())
                .load(pic)
                .signature(new StringSignature(uuidAsString))
                .transform(new CenterCrop(), new RoundedCorners(40))
                .into(picture);
    }

    public void setList_title(String title) {
        list_title.setText(title);
    }

    public void setHeart(Integer heart) {
        if(heart == 1) {
            this.heart.setImageResource(R.drawable.favorite_full);
        } else {
            this.heart.setImageResource(R.drawable.favorite_null);
        }
    }

    public void setComplete_text() { complete_text.setVisibility(View.VISIBLE); }

    public void setComplete_img() {
        complete_img.setVisibility(View.VISIBLE);
        list_title.setGravity(getLeft());
    }

}