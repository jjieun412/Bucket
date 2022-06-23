package com.example.bucket;

import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;

public class MyBKListAchievedRequest {

    @SerializedName("token")
    public String token;

    @SerializedName("bucketId")
    public Integer bucketId;

    @SerializedName("picture")
    public MultipartBody.Part picture;


    public String toString() { return bucketId + " " + picture; }


    public Integer getBucketId() { return bucketId; }
    public void setId(Integer bucketId) { this.bucketId = bucketId; }

    public MultipartBody.Part getPicture() { return picture; }
    public void setPicture(MultipartBody.Part picture) {
        this.picture = picture;
    }


    public MyBKListAchievedRequest(Integer bucketId, MultipartBody.Part picture) {
        this.bucketId = bucketId;
        this.picture = picture;
    }
}
