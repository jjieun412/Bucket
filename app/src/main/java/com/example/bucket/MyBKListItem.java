package com.example.bucket;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class MyBKListItem {
    @SerializedName("bucketId")
    public Integer bucketId;

    @SerializedName("isVisible")
    public Integer isVisible;

    @SerializedName("title")
    public String title;

    @SerializedName("nickname")
    public String nickname;

    @SerializedName("startDate")
    public String startDate;

    @SerializedName("endDate")
    public String endDate;

    @SerializedName("heart")
    public Integer heart;

    @SerializedName("tags")
    public List<String> tags;

    @SerializedName("profilePict")
    public String picture;

    @SerializedName("achievedPict")
    public String achievedPicture;

    @SerializedName("reviewPicts")
    public List<String> reviewPicture;

    @SerializedName("detailPlans")
    public List<detailPlan> detailplans;

    @SerializedName("reviewContent")
    public String reviewContent;

    @SerializedName("bucketContent")
    public String bucketContent;

    @SerializedName("userProfilePict")
    public String userProfile;


    public MyBKListItem(JsonObject objj) {
        this.bucketId = objj.get("bucketId").getAsInt();
        System.out.println("bucketId");

        this.isVisible = objj.get("isVisible").getAsInt();
        System.out.println("isVisible");

        this.title = objj.get("title").getAsString();
        System.out.println("title");

        this.nickname = objj.get("nickname").getAsString();
        System.out.println("nickname");

        this.startDate = objj.get("startDate").getAsString().substring(0, 10);
        System.out.println("startDate");

        this.endDate = objj.get("endDate").getAsString().substring(0, 10);
        System.out.println("endDate");

        this.heart = objj.get("heart").getAsInt();
        System.out.println("heart");

        setTags(objj.get("tags").getAsJsonArray());
        System.out.println("tags");

        setDetailplans(objj.get("detailPlans") == null? new JsonArray(): objj.get("detailPlans").getAsJsonArray());
        System.out.println("detailPlans");

        this.picture = objj.get("profilePict").isJsonNull()? null: objj.get("profilePict").getAsString();
        System.out.println("profilePict");


        if(objj.get("achievedPict").isJsonNull())
        {
            this.achievedPicture = null;
        }
        else{
            System.out.println("achievedPict is not null");
            System.out.println(objj.get("achievedPict").getClass());
            this.achievedPicture = objj.get("achievedPict").getAsString();
        }
        System.out.println("achievedPict");


        setReviewPicture(objj.get("reviewPicts").isJsonNull()? new JsonArray(): objj.get("reviewPicts").getAsJsonArray());
        System.out.println("reviewPicts");

        this.userProfile = objj.get("userProfilePict").isJsonNull()? null: objj.get("userProfilePict").getAsString();
        System.out.println("userProfilePict");

        this.reviewContent = objj.get("reviewContent").isJsonNull()? null: objj.get("reviewContent").getAsString();
        System.out.println("reviewContent");

        this.bucketContent = objj.get("bucketContent").isJsonNull()? null: objj.get("bucketContent").getAsString();
        System.out.println("bucketContent");

    }

    public String toString() { return bucketId + " " + isVisible + " " + title + " " + nickname + " " + startDate + " " +
            endDate + " " + heart + " " + tags + " " + picture + " " + achievedPicture + " "
            + reviewPicture + " " + detailplans + " " + reviewContent + " " + bucketContent + " " + userProfile; }


    public Integer getBucketId() { return bucketId; }
    public void setBucketId(Integer bucketId) { this.bucketId = bucketId; }

    public Integer getIsVisible() { return isVisible; }
    public void setIsVisible(Integer isVisible) { this.isVisible = isVisible; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getStartDate() {
        String arr [] = startDate.split("-");
        return arr[0] + "년 " + arr[1] + "월 " + (arr[2]) + "일 ";
    }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() {
        String arr [] = endDate.split("-");
        return arr[0] + "년 " + arr[1] + "월 " + arr[2] + "일 ";
    }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public Integer getHeart() { return heart; }
    public void setHeart(Integer heart) { this.heart = heart; }

    public List<String> getTags() { return tags; }
    public void setTags(JsonArray tags) {
        List<String> listToAdd = new ArrayList<String>();

        for(JsonElement temp : tags)
        {
            listToAdd.add(temp.getAsString());
        }

        this.tags = listToAdd;
    }

    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }

    public String getAchievedPicture() { return achievedPicture; }
    public void setAchievedPicture(String achievedPicture) { this.achievedPicture = achievedPicture; }

    public List<String> getReviewPicture() { return reviewPicture; }
    public void setReviewPicture(JsonArray reviewPicture) {
        List<String> listToAdd = new ArrayList<String>();

        for(JsonElement temp : reviewPicture)
        {
            listToAdd.add(temp.getAsString());
        }
        this.reviewPicture = listToAdd;
    }

    public List<detailPlan> getDetailplans() { return detailplans; }
    public void setDetailplans(JsonArray detailplans) {
        List<detailPlan> listToAdd = new ArrayList<detailPlan>();

        for(JsonElement temp : detailplans)
        {
            listToAdd.add(new detailPlan(temp.getAsJsonObject()));
        }

        this.detailplans = listToAdd;
    }

    public String getReviewContent() { return reviewContent; }
    public void setReviewContent(String reviewContent) { this.reviewContent = reviewContent;}

    public String getBucketContent() { return bucketContent; }
    public void setBucketContent(String bucketContent) { this.bucketContent = bucketContent; }

    public String getUserProfile() { return userProfile; }
    public void setUserProfile(String userProfile) { this.userProfile = userProfile; }


    public class detailPlan {
        @SerializedName("orderNumb")
        private Integer orderNumb;

        @SerializedName("content")
        private String content;

        @SerializedName("planDate")
        private String planDate;

        @SerializedName("achieved")
        private Integer achieved;


        public detailPlan(JsonObject obj) {
            this.orderNumb = obj.get("orderNumb").getAsInt();
            this.content = obj.get("content").getAsString();
            this.planDate = obj.get("planDate").getAsString().substring(0, 10);
            this.achieved = Integer.parseInt(obj.get("achieved").getAsString());
        }

        public Integer getOrderNumb() { return orderNumb; }
        public void setOrderNumb(Integer orderNumb) { this.orderNumb = orderNumb; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }

        public String getPlanDate() {
            String arr [] = planDate.split("-");
            return arr[0] + "년 " + arr[1] + "월 " + arr[2] + "일 ";
        }
        public void setPlanDate(String planDate) { this.planDate = planDate; }

        public Integer getAchieved() { return achieved; }
        public void setAchieved(Integer achieved) { this.achieved = achieved; }
    }
}
