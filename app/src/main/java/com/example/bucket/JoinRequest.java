package com.example.bucket;
// 회원가입 화면에서 입력받은 값을 요청하는 페이지


import com.google.gson.annotations.SerializedName;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class JoinRequest {

    @SerializedName("nickname")
    public String nickname;

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("tags")
    public List<String> tags;

    @SerializedName("picture")
    public MultipartBody.Part picture;



    public String getNickname() { return nickname; }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public List<String> getTags() { return tags; }

    public void setTags(List<String> tags) { this.tags = tags; }

    public MultipartBody.Part getPicture() { return picture; }



    public String toString() { return nickname + " " + email + " " + password + " " + tags + " " + picture; }


    public JoinRequest(String nickname, String email, String password, List<String> tags, MultipartBody.Part picture) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.tags = tags;
        this.picture = picture;
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

