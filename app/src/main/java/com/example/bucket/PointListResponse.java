package com.example.bucket;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;


public class PointListResponse {

    @SerializedName("msg")
    public String msg;

    @SerializedName("status")
    public String status;

    @SerializedName("details")
    public JsonArray details;


    public String toString() {
        return status + " " + msg + " " + details;
    }


    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() { return msg; }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JsonArray getDetails() { return details; }
    public void setDetails(JsonArray details) { this.details = details; }


    public PointListResponse(JsonObject obj)
    {
        this.status = obj.get("status").toString();
        this.msg = obj.get("msg").toString();
        this.details = obj.get("details").getAsJsonArray();
    }


    public class details {

        public details(JsonElement anObj)
        {
            JsonObject anObject = anObj.getAsJsonObject();

            this.source = anObject.get("source").toString();
            this.formatTime = anObject.get("formatTime").toString();
            this.point = anObject.get("point").getAsInt();
            this.bucketId = anObject.get("bucketId").getAsInt();
            this.title = anObject.get("title").getAsString();
        }

        public String toString() { return source + " " + formatTime + " " + point + " " + bucketId + " " + title; }

        @SerializedName("source")
        public String source;

        @SerializedName("formatTime")
        public String formatTime;

        @SerializedName("point")
        public Integer point;

        @SerializedName("bucketId")
        public Integer bucketId;

        @SerializedName("title")
        public String title;


        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }

        public String getFormatTime() { return formatTime; }
        public void setFormatTime(String formatTime) { this.formatTime = formatTime; }

        public Integer getPoint() { return point; }
        public void setPoint(Integer point) {
            this.point = point;
        }

        public Integer getBucketId() { return bucketId; }
        public void setBucketId(Integer bucketId) {
            this.bucketId = bucketId;
        }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
    }
}
