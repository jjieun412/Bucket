package com.example.bucket;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import okhttp3.MultipartBody;



public class MyBKListEditResponse {

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

    public JsonObject getBucketlist() { return bucketList; }
    public void setBucketlist(JsonObject bucketlist) { this.bucketList = bucketlist; }



    public MyBKListEditResponse(JsonObject obj)
    {
        this.status = obj.get("status").toString();
        this.msg = obj.get("msg").toString();
        this.bucketList = obj.get("bucketList").getAsJsonObject();
    }


    public class bucketList {
        @SerializedName("bucketId")
        private String bucketId;

        @SerializedName("title")
        private String title;

        @SerializedName("content")
        private String content;

        @SerializedName("isVisible")
        private Integer isVisible;

        @SerializedName("startDate")
        private String startDate;

        @SerializedName("endDate")
        private String endDate;

        @SerializedName("tags")
        private List<String> tags;

        @SerializedName("detailPlans")
        private List<String> detailplans;

        @SerializedName("profilePict")
        public MultipartBody.Part picture;


        public String getBucketId() { return bucketId; }
        public void setBucketId(String bucketId) { this.bucketId = bucketId; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }

        public Integer getIsVisible() { return isVisible; }
        public void setIsVisible(Integer isVisible) { this.isVisible = isVisible; }

        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }

        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }

        public List<String> getTags() { return tags; }
        public void setTags(List<String> tags) { this.tags = tags; }

        public List<String> getDetailplans() { return detailplans; }
        public void setDetailplans(List<String> detailplans) { this.detailplans = detailplans; }

        public MultipartBody.Part getPicture() { return picture; }
        public void setPicture(MultipartBody.Part picture) { this.picture = picture; }
    }

    public class detailPlans {
        @SerializedName("orderNumb")
        private Integer orderNumb;

        @SerializedName("content")
        private String content;

        @SerializedName("planDate")
        private String planDate;
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