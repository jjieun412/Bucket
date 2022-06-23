package com.example.bucket;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ServiceAsklistItem {

    String Date, Title, Pic, Content;
    Integer BK_id, State;

    public ServiceAsklistItem(JsonObject obj) {
        this.BK_id = obj.get("bucketId").getAsInt();
        this.Date = obj.get("requestDate").getAsString();
        this.Title = obj.get("title").getAsString();
        JsonArray temp = obj.get("verifyRequestPicts").isJsonNull()? null: obj.get("verifyRequestPicts").getAsJsonArray();
        System.out.println("temp");
        System.out.println(temp);
        this.Pic = temp == null ? null : temp.get(0).getAsString();

        this.Content = obj.get("content").getAsString();
    }


    public String toString() { return BK_id + " " + Date + " " + Title + " " + Pic + " " + Content + " " + State; }


    public Integer getBK_id() { return BK_id; }
    public void setBK_id(Integer BK_id) { this.BK_id = BK_id; }

    public String getDate() { return Date; }
    public void setDate(String Date) { this.Date = Date; }

    public String getTitle() { return Title; }
    public void setTitle(String Title) { this.Title = Title; }

    public String getPic() { return Pic; }
    public void setPic(String Pic) { this.Pic = Pic; }

    public String getContent() { return Content; }
    public void setContent(String Content) { this.Content = Content; }

    public Integer getState() { return State; }
    public void setState(Integer State) { this.State = State; }


    public String getFormat_Date() {
        String arr[] = Date.split("-");
        String temp = arr[0] + "년 " + arr[1] + "월 " + arr[2] +"일 ";
        return temp;
    }


}
