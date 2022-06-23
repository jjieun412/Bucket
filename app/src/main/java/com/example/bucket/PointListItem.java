package com.example.bucket;


import com.google.gson.JsonObject;

public class PointListItem {

    String bkTitle;
    Integer point;
    String date;

    public PointListItem(String bkTitle, Integer point, String date) {
        this.bkTitle = bkTitle;
        this.point = point;
        this.date = date;
    }

    public PointListItem(JsonObject objj) {
        this.bkTitle = objj.get("title").getAsString();
        this.point = objj.get("point").getAsInt();
        this.date = objj.get("formatTime").getAsString();
    }


    public String getBkTitle() { return bkTitle; }
    public void setBkTitle(String bkTitle) { this.bkTitle = bkTitle; }

    public String getDate() { return date; }

    public String getDate_format() {
        if(date == null)
            return null;

        String temp = date.substring(0, 9);
        String arr[] = temp.split("/");
        String temp2 = arr[2] + "년 " + arr[0] + "월 " + arr[1] +"일 ";
        return temp2;
    }
    public void setDate(String date) { this.date = date; }

    public Integer getPoint() { return point; }
    public void setPoint(Integer point) { this.point = point; }

    public String toString(){
        String temp = "bkTitle: " + bkTitle + "\n";
        temp += "date: " + date + "\n";
        temp += "point: " + point + "\n";
        return temp;
    }
}
