package com.example.bucket;


import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;



public class LoginResponse {


    @SerializedName("status")
    public String status;

    @SerializedName("token")
    public String token;

    @SerializedName("msg")
    public String msg;


    public String toString() {
        return status + " " + msg + " " + token;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() { return token; }

    public String getMsg() { return msg; }



    public LoginResponse(JsonObject obj)
    {
        this.status = obj.get("status").toString();
        this.token = obj.get("token").toString();
        this.msg = obj.get("msg").toString();
    }

}
