package com.example.bucket;


import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;


public class PointResponse {

    @SerializedName("status")
    public String status;

    @SerializedName("msg")
    public String msg;

    @SerializedName("point")
    public Integer point;


    public String toString() {
        return status + " " + msg + " " + point;
    }


    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() { return msg; }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getPoint() { return point; }
    public void setPoint(Integer point) { this.point = point; }


    public PointResponse(JsonObject obj)
    {
        this.status = obj.get("status").toString();
        this.msg = obj.get("msg").toString();
        this.point = obj.get("point").getAsInt();
    }
}
