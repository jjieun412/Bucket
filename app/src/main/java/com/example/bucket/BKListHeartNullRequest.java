package com.example.bucket;


import com.google.gson.annotations.SerializedName;


public class BKListHeartNullRequest {

    @SerializedName("token")
    public String token;

    @SerializedName("bucketId")
    public Integer bucketId;


    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Integer getBucketId() { return bucketId; }


    public String toString() { return token + " " + bucketId; }


    public BKListHeartNullRequest(String token, Integer bucketId) {
        this.token = token;
        this.bucketId = bucketId;
    }
}