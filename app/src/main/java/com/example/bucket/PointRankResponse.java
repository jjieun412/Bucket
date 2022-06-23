package com.example.bucket;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;


public class PointRankResponse {

    @SerializedName("status")
    public String status;

    @SerializedName("msg")
    public String msg;

    @SerializedName("ranking")
    public JsonArray ranking;

    @SerializedName("myinfo")
    public JsonObject myinfo;


    public String toString() {
        return status + " " + msg + " " + ranking;
    }


    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() { return msg; }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JsonArray getRanking() { return ranking; }
    public void setRanking(JsonArray ranking) { this.ranking = ranking; }

    public JsonObject getMyinfo() { return myinfo; }
    public void setMyinfo(JsonObject myinfo) { this.myinfo = myinfo; }


    public PointRankResponse(JsonObject obj)
    {
        this.status = obj.get("status").toString();
        this.msg = obj.get("msg").toString();
        this.ranking = obj.get("ranking").getAsJsonArray();
        this.myinfo = obj.get("myinfo").getAsJsonObject();
    }


    public class ranking {

        @SerializedName("email")
        public String email;

        @SerializedName("point")
        public Integer point;

        @SerializedName("nickname")
        public String nickname;

        @SerializedName("profilePicturePath")
        public String picture;


        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public Integer getPoint() { return point; }
        public void setPoint(Integer point) { this.point = point; }

        public String getPicture() { return picture; }
        public void setPicture(String picture) { this.picture = picture; }

        public String getNickname() { return nickname; }
        public void setNickname(String nickname) { this.nickname = nickname; }


        public String toString() { return  email + " " + point + " " + nickname + " " + picture; }
    }


    public class myinfo {

        @SerializedName("nickname")
        public String nickname;

        @SerializedName("profilePicturePath")
        public String picture;


        public String getPicture() { return picture; }
        public void setPicture(String picture) { this.picture = picture; }

        public String getNickname() { return nickname; }
        public void setNickname(String nickname) { this.nickname = nickname; }


        public String toString() { return  nickname + " " + picture; }
    }

}
