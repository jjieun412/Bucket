package com.example.bucket;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;


public class bucketList_my {

    public bucketList_my(JsonElement anObj)
    {
        JsonObject anObject = anObj.getAsJsonObject();

        this.bucketId = Integer.parseInt(anObject.get("bucketId").toString());
        this.title = anObject.get("title").toString();
        this.picture = anObject.get("profilePict").toString();
        this.achieved = anObject.get("achieved").getAsInt();

        title = title.replaceAll("\"", "");
        picture = picture.replaceAll("\"", "");
    }

    public String toString()
    {
        String temp = "bucketId: " + bucketId + "\n";
        temp += "title: " + title + "\n";
        temp += "picture " + picture + "\n";
        temp += "achieved " + achieved + "\n";

        return temp;
    }

    @SerializedName("bucketId")
    public Integer bucketId;

    @SerializedName("title")
    public String title;

    @SerializedName("profilePict")
    public String picture;

    @SerializedName("achieved")
    public Integer achieved;


    public Integer getBucketId() { return bucketId; }
    public void setBucketId(Integer bucketId) { this.bucketId = bucketId; }

    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getAchieved() { return achieved; }
    public void setAchieved(Integer achieved) { this.achieved = achieved; }
}