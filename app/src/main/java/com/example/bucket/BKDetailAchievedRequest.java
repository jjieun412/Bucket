package com.example.bucket;

import com.google.gson.annotations.SerializedName;

public class BKDetailAchievedRequest {

    @SerializedName("token")
    public String token;

    @SerializedName("bucketId")
    public Integer bucketId;

    @SerializedName("orderNumb")
    public Integer orderNumb;

    @SerializedName("achieved")
    public Integer achieved;



    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Integer getBucketId() { return bucketId; }

    public Integer getOrderNumb() { return orderNumb; }

    public Integer getAchieved() { return achieved; }


    public String toString() { return token + " " + bucketId + " " + orderNumb + " " + achieved; }


    public BKDetailAchievedRequest(String token, Integer bucketId, Integer orderNumb, Integer achieved) {
        this.token = token;
        this.bucketId = bucketId;
        this.orderNumb = orderNumb;
        this.achieved = achieved;
    }
}
