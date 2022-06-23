package com.example.bucket;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;


public class MyBKListReviewResponse {

    @SerializedName("status")
    public String status;

    @SerializedName("msg")
    public String msg;

    @SerializedName("review")
    public JsonObject review;


    public String toString() {
        return status + " " + msg + " " + review;
    }


    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() { return msg; }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JsonObject getReview() { return review; }
    public void setReview(JsonObject review) {
        this.review = review;
    }


    public MyBKListReviewResponse(JsonObject obj)
    {
        this.status = obj.get("status").toString();
        this.msg = obj.get("msg").toString();
        this.review = obj.get("review").getAsJsonObject();
    }

    public class review {
        @SerializedName("picts")
        private String picture;

        @SerializedName("content")
        private String content;

        @SerializedName("bucketId")
        private Integer bucketId;
    }
}