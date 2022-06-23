package com.example.bucket;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UserItem_my {

    @SerializedName("email")
    public String email;

    @SerializedName("nickname")
    public String nickname;

    @SerializedName("profilePicturePath")
    public String picturePath;

    @SerializedName("tags")
    public List<String> tags;


    public UserItem_my(JsonObject obj) {
        this.email = obj.get("email").getAsString();
        this.nickname = obj.get("nickname").getAsString();
        this.picturePath = obj.get("profilePicturePath").isJsonNull()? null: obj.get("profilePicturePath").getAsString();

        setTags(obj.get("tags").getAsJsonArray());
        System.out.println(obj.get("tags").getAsJsonArray());
    }


    public String toString() { return  email + " " + nickname + " " + picturePath + " " + tags; }


    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getPicturePath() { return picturePath; }
    public void setPicturePath(String picturePath) { this.picturePath = picturePath; }

    public List<String> getTags() { return tags; }
    public void setTags(JsonArray tags) {
        List<String> listToAdd = new ArrayList<String>();
        for(JsonElement temp : tags) {
            listToAdd.add(temp.getAsString());
        }
        this.tags = listToAdd;
    }

}
