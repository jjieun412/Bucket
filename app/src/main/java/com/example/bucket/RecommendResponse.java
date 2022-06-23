package com.example.bucket;


import android.net.Uri;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;


public class RecommendResponse {

    @SerializedName("status")
    public String status;

    @SerializedName("msg")
    public String msg;

    @SerializedName("bucketLists")
    public JsonArray bucketLists;


    public String toString() {
        return status + " " + msg + " " + bucketLists;
    }


    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() { return msg; }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JsonArray getBucketLists() { return bucketLists; }
    public void setBucketlist(JsonArray bucketLists) { this.bucketLists = bucketLists; }


    public RecommendResponse(JsonObject obj)
    {
        this.status = obj.get("status").toString();
        this.msg = obj.get("msg").toString();
        this.bucketLists = obj.get("bucketLists").getAsJsonArray();
    }

    public class bucketLists {

        public bucketLists(JsonElement anObj)
        {
            JsonObject anObject = anObj.getAsJsonObject();

            this.bucketId = Integer.parseInt(anObject.get("bucketId").toString());
            this.heart = Integer.parseInt(anObject.get("heart").toString());
            this.title = anObject.get("title").toString();
            this.picture = Uri.parse(anObject.get("profilePict").toString());
        }


        @SerializedName("bucketId")
        public Integer bucketId;

        @SerializedName("title")
        public String title;

        @SerializedName("profilePict")
        public Uri picture;

        @SerializedName("heart")
        public Integer heart;    // 0이면 하트X, 1이면 하트O

        public Uri getPicture() { return picture; }
        public void setPicture(Uri picture) { this.picture = picture; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public Integer getHeart() { return heart; }
        public void setHeart(Integer heart) {
            this.heart = heart;
        }
    }
}