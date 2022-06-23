package com.example.bucket;


import com.google.gson.annotations.SerializedName;


public class NewPWsetRequest {

    @SerializedName("email")
    public String email;

    @SerializedName("code")
    public Integer code;

    @SerializedName("pswd")
    public String pswd;


    public String getEmail() { return email; }

    public Integer getCode() { return code; }

    public String getPswd() { return pswd; }


    public String toString()
    {
        return email + " " + code + " " + pswd;
    }

    public NewPWsetRequest(String email, Integer code, String pswd) {

        this.email = email;
        this.code = code;
        this.pswd = pswd;
    }
}
