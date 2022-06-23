package com.example.bucket;


import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;


public class MySetPageResponse {

    @SerializedName("status")
    public String status;

    @SerializedName("msg")
    public String msg;

    @SerializedName("user")
    public JsonObject user;


    public String toString() {
        return status + " " + msg + " " + user;
    }


    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() { return msg; }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JsonObject getUser() { return user; }
    public void setUser(JsonObject user) { this.user = user; }

    public MySetPageResponse(JsonObject obj)
    {
        this.status = obj.get("status").toString();
        this.msg = obj.get("msg").toString();
        this.user = obj.get("user").getAsJsonObject();
    }
}
