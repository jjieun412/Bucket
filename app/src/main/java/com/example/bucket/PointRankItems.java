package com.example.bucket;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;



public class PointRankItems {

    ArrayList<PointRankItem> list = new ArrayList<>();

    public PointRankItems(JsonArray jsonArr)
    {
        for(int i =0; i< jsonArr.size(); i++) {
            JsonObject temp = jsonArr.get(i).getAsJsonObject();
            list.add(new PointRankItem(temp));
        }
    }

    public String toString() {
        String A = "";
        for(PointRankItem item: list)
        {
            A += item.toString() + " \n";
        }
        return A;
    }
}
