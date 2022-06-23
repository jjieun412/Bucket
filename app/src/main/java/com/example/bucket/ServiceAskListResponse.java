package com.example.bucket;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ServiceAskListResponse {

    @SerializedName("status")
    public String status;

    @SerializedName("msg")
    public String msg;

    @SerializedName("verifyRequests")
    public JsonArray verifyrequests;


    public String toString() {
        return status + " " + msg + " " + verifyrequests;
    }


    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() { return msg; }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JsonArray getVerifyrequests() { return verifyrequests; }
    public void setVerifyrequests(JsonArray verifyrequests) { this.verifyrequests = verifyrequests; }


    public ServiceAskListResponse(JsonObject obj)
    {
        this.status = obj.get("status").toString();
        this.msg = obj.get("msg").toString();
        this.verifyrequests = obj.get("verifyRequests").getAsJsonArray();
    }

    public class verifyRequests {

        @SerializedName("bucketId")
        private Integer bucketId;

        @SerializedName("requestDate")
        private String requestDate;

        @SerializedName("title")
        private String title;

        @SerializedName("verifyRequestPicts")
        public String verifyPic;

        @SerializedName("content")
        private String content;

        @SerializedName("state")
        private Integer state;


        public String toString() { return bucketId + " " + requestDate + " " + title + " " + verifyPic + " " + content + " " + state; }


        public Integer getBucketId() { return bucketId; }
        public void setBucketId(Integer bucketId) { this.bucketId = bucketId; }

        public String getRequestDate() { return requestDate; }
        public void setRequestDate(String requestDate) { this.requestDate = requestDate; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getVerifyPic() { return verifyPic; }
        public void setVerifyPic(String verifyPic) { this.verifyPic = verifyPic; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }

        public Integer getState() { return state; }
        public void setState(Integer state) { this.state = state; }
    }
}
