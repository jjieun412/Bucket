package com.example.bucket;


import com.google.gson.annotations.SerializedName;


public class ServiceAskListDeleteRequest {

    @SerializedName("token")
    public String token;

    @SerializedName("bucketId")
    public Integer bucketId;

    @SerializedName("requestDate")
    public String requestDate;



    public String toString() {
        return bucketId + " " + requestDate;
    }


    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Integer getBucketId() { return bucketId; }
    public void setId(Integer bucketId) { this.bucketId = bucketId; }

    public String getRequestDate() { return requestDate; }
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }


    public ServiceAskListDeleteRequest(String token, Integer bucketId, String requestDate) {
        this.token = token;
        this.bucketId = bucketId;
        this.requestDate = requestDate;
    }
}
