package com.example.bucket;


import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;


public class BKListDetailResponse {

    @SerializedName("status")
    public String status;

    @SerializedName("msg")
    public String msg;

    @SerializedName("bucketList")
    public JsonObject bucketList;


    public String toString() {
        return status + " " + msg + " " + bucketList;
    }


    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() { return msg; }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JsonObject getBucketList() { return bucketList; }
    public void setBucketlist(JsonObject bucketList) { this.bucketList = bucketList; }


    public BKListDetailResponse(JsonObject obj)
    {
        this.status = obj.get("status").toString();
        this.msg = obj.get("msg").toString();
        this.bucketList = obj.get("bucketList").getAsJsonObject();
    }


    public class detailPlans {
        @SerializedName("orderNumb")
        private Integer orderNumb;

        @SerializedName("content")
        private String content;

        @SerializedName("planDate")
        private String planDate;

        @SerializedName("achieved")
        private String achieved;


        public Integer getOrderNumb() { return orderNumb; }
        public void setOrderNumb(Integer orderNumb) { this.orderNumb = orderNumb; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }

        public String getPlanDate() { return planDate; }
        public void setPlanDate(String planDate) { this.planDate = planDate; }

        public String getAchieved() { return achieved; }
        public void setAchieved(String achieved) { this.achieved = achieved; }
    }

    public class Tags {
        @SerializedName("여행")
        private String travel;

        @SerializedName("맛집")
        private String food;

        @SerializedName("도전")
        private String challenge;

        @SerializedName("운동")
        private String exercise;

        @SerializedName("자기계발")
        private String develop;

        @SerializedName("자기관리")
        private String diet;

        @SerializedName("취미")
        private String hobby;

        @SerializedName("입시/취업")
        private String work;

        @SerializedName("외국어")
        private String language;

        @SerializedName("재테크")
        private String finance;

        @SerializedName("자격증")
        private String certificate;

        @SerializedName("건강")
        private String health;

        @SerializedName("독서")
        private String book;

        @SerializedName("기타")
        private String anything;
    }
}