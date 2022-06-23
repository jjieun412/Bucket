package com.example.bucket;

import com.google.gson.JsonObject;

public class PointRankItem {

    Integer Num;
    String Pic;
    String Nick;
    Integer Point;


    public PointRankItem(JsonObject objj) {
        this.Pic = objj.get("profilePicturePath").isJsonNull()? null: objj.get("profilePicturePath").getAsString();
        this.Nick = objj.get("nickname").getAsString();
        this.Point = objj.get("point") == null || objj.get("point").isJsonNull() ? null : objj.get("point").getAsInt();
        System.out.println(this);
    }


    public String getPic() { return Pic; }
    public void setPic(String Pic) { this.Pic = Pic; }

    public String getNick() { return Nick; }
    public void setNum(String Nick) { this.Nick = Nick;}

    public Integer getPoint() { return Point; }
    public void setPoint(Integer Point) { this.Point = Point; }


    public String toString(){
        String temp = "Pic: " + Pic + "\n";
        temp += "Nick: " + Nick + "\n";
        temp += "Point: " + Point + "\n";
        return temp;
    }
}
