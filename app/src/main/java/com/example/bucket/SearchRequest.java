package com.example.bucket;


import com.google.gson.annotations.SerializedName;


public class SearchRequest {

    @SerializedName("token")
    public String token;

    @SerializedName("keyword")
    public String keyword;


    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getKeyword() { return keyword; }


    public String toString() { return token + " " + keyword; }


    public SearchRequest(String token, String keyword) {
        this.token = token;
        this.keyword = keyword;
    }
}
