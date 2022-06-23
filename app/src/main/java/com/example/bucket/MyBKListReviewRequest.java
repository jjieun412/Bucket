package com.example.bucket;


import com.google.gson.annotations.SerializedName;
import okhttp3.MultipartBody;


public class MyBKListReviewRequest {

    @SerializedName("token")
    public String token;

    @SerializedName("bucketId")
    public Integer bucketId;

    @SerializedName("content")
    public String content;

    @SerializedName("picture")
    public MultipartBody.Part picture;


    public String toString() {
        return token + " " + bucketId + " " + content + " " + picture;
    }


    public String getToken() { return token; }
    public void setToken(String token) {
        this.token = token;
    }

    public Integer getBucketId() { return bucketId; }
    public void setId(Integer bucketId) { this.bucketId = bucketId; }

    public String getContent() { return content; }
    public void setContent(String content) {
        this.content = content;
    }

    public MultipartBody.Part getPicture() { return picture; }
    public void setPicture(MultipartBody.Part picture) {
        this.picture = picture;
    }


    public MyBKListReviewRequest(Integer bucketId, String content, MultipartBody.Part picture) {
        this.bucketId = bucketId;
        this.content = content;
        this.picture = picture;
    }
}
