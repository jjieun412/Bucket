package com.example.bucket;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;


public class SearchResponse {

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

    public JsonArray getBucketlists() { return bucketLists; }
    public void setBucketlist(JsonArray bucketLists) { this.bucketLists = bucketLists; }


    public SearchResponse(JsonObject obj)
    {
        this.status = obj.get("status").toString();
        this.msg = obj.get("msg").toString();
        this.bucketLists = obj.get("bucketLists").getAsJsonArray();
    }

}
