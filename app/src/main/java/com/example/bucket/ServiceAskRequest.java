package com.example.bucket;


import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;


public class ServiceAskRequest {

    @SerializedName("bucketId")
    public Integer bucketId;

    @SerializedName("content")
    public String content;

    @SerializedName("picture")
    public MultipartBody.Part picture;


    public Integer getBucketId() { return bucketId; }
    public String getContent() { return content; }
    public MultipartBody.Part getPicture() { return picture; }


    public String toString()
    {
        return bucketId + " " + content + " " + picture;
    }


    public ServiceAskRequest(Integer bucketId, String content, MultipartBody.Part picture) {
        this.bucketId = bucketId;
        this.content = content;
        this.picture = picture;
    }
}
