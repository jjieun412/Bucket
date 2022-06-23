package com.example.bucket;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import okhttp3.MultipartBody;


public class WriteBucketRequest{

    @SerializedName("token")
    public String token;

    @SerializedName("startDate")
    public String startDate;

    @SerializedName("endDate")
    public String endDate;

    @SerializedName("title")
    public String title;

    @SerializedName("content")
    public String content;

    @SerializedName("isVisible")
    public Integer isVisible;

    @SerializedName("detailPlans")
    public List<String> detailplans;

    @SerializedName("picture")
    public MultipartBody.Part picture;

    @SerializedName("tags")
    public List<String> tags;


    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getStartDate() { return startDate; }

    public String getEndDate() { return endDate; }

    public String getTitle() { return title; }

    public String getContent() { return content; }

    public Integer getIsVisible() { return isVisible; }

    public List<String> getDetailplans() { return detailplans; }
    public void setDetailplans(List<String> detailplans) { this.detailplans = detailplans; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public MultipartBody.Part getPicture() { return picture; }


    public String toString() { return startDate + " " + endDate + " " + title + " " + content + " " + isVisible + " " + detailplans + " " + tags + " " + picture; }


    public WriteBucketRequest(String token, String startDate, String endDate, String title, String content, Integer isVisible, List<String> detailplans, MultipartBody.Part picture, List<String> tags) {
        this.token = token;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.content = content;
        this.isVisible = isVisible;
        this.detailplans = detailplans;
        this.tags = tags;
        this.picture = picture;
    }

    public class detailPlans {
        @SerializedName("orderNumb")
        private String orderNumb;

        @SerializedName("content")
        private String content;

        @SerializedName("planDate")
        private String planDate;


        public String getOrderNumb() { return orderNumb; }

        public String getContent() { return content; }

        public String getPlanDate() { return planDate; }

        public void setOrderNumb(String orderNumb) { this.orderNumb = orderNumb; }

        public void setContent(String content) { this.content = content; }

        public void setPlanDate(String planDate) { this.planDate = planDate; }

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