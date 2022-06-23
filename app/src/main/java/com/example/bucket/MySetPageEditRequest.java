package com.example.bucket;


import com.google.gson.annotations.SerializedName;

import java.util.List;
import okhttp3.MultipartBody;


public class MySetPageEditRequest {

    @SerializedName("token")
    public String token;

    @SerializedName("tags")
    public List<String> tags;

    @SerializedName("nickname")
    public String nickname;

    @SerializedName("picture")
    public MultipartBody.Part picture;


    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public List<String> getTags() { return tags; }
    public String getNickname() { return nickname; }
    public MultipartBody.Part getPicture() { return picture; }


    public String toString() { return token + " " + tags + " " + nickname + " " + picture; }


    public MySetPageEditRequest(String token, List<String> tags, String nickname, MultipartBody.Part picture) {
        this.token = token;
        this.tags = tags;
        this.nickname = nickname;
        this.picture = picture;
    }

}
