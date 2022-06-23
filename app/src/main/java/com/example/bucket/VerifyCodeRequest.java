package com.example.bucket;

import com.google.gson.annotations.SerializedName;



public class VerifyCodeRequest {

    @SerializedName("email")
    public String email;

    @SerializedName("code")
    public Integer code;


    public String getEmail() { return email; }

    public Integer getCode() { return code; }


    public String toString()
    {
        return email + " " + code;
    }


    public VerifyCodeRequest(String email, Integer code) {

        this.email = email;
        this.code = code;
    }
}
