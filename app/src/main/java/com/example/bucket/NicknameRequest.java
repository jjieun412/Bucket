package com.example.bucket;


import com.google.gson.annotations.SerializedName;


public class NicknameRequest {

    @SerializedName("nickname")
    public String nickName;


    public String toString()
    {
        return nickName;
    }


    public String getNickName() {
        return nickName;
    }


    public NicknameRequest(String nickName) {
        this.nickName = nickName;
    }


}
