package com.example.bucket;


import com.google.gson.annotations.SerializedName;


public class MyBKListDeleteRequest {

    @SerializedName("token")
    public String token;

    @SerializedName("bucketId")
    public Integer bucketId;


    public String toString() {
        return String.valueOf(bucketId);
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Integer getBucketId() { return bucketId; }
    public void setId(Integer bucketId) { this.bucketId = bucketId; }


    public MyBKListDeleteRequest(String token, Integer bucketId) {
        this.token = token;
        this.bucketId = bucketId;
    }
}
