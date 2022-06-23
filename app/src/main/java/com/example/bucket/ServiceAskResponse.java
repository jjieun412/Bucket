package com.example.bucket;


import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;


public class ServiceAskResponse {

    @SerializedName("status")
    public String status;

    @SerializedName("msg")
    public String msg;

    @SerializedName("verification_request")
    public JsonObject requestlist;


    public String toString() {
        return status + " " + msg + " " + requestlist;
    }


    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() { return msg; }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JsonObject getRequestlist() { return requestlist; }
    public void setRequestlist(JsonObject requestlist) { this.requestlist = requestlist; }


    public ServiceAskResponse(JsonObject obj)
    {
        this.status = obj.get("status").toString();
        this.msg = obj.get("msg").toString();
        this.requestlist = obj.get("verification_request").getAsJsonObject();
    }

    public class requestlist {
        @SerializedName("bucketId")
        private String bucketId;

        @SerializedName("content")
        private String content;

        @SerializedName("picPath")
        public String picPath;


        public String getBucketId() { return bucketId; }
        public void setBucketId(String bucketId) { this.bucketId = bucketId; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }

        public String getPicPath() { return picPath; }
        public void setPicPath(String picPath) { this.picPath = picPath; }
    }
}
