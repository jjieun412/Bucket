package com.example.bucket;


import java.util.HashMap;

public class DatePlanItem {

    int set_Num;
    String set_Date;
    String content;

    public DatePlanItem(int set_Num, String content) {
        this.set_Num = set_Num;
        this.content = content;
    }

    public int getSet_Num() { return set_Num; }
    public void setSet_Num(int set_Num) { this.set_Num = set_Num; }

    public String getSet_Date() { return set_Date; }

    public String getSet_Format_Date() {
        if(set_Date == null)
            return null;

        String arr[] = set_Date.split("-");
        String temp = arr[0] + "년 " + arr[1] + "월 " + arr[2] +"일 ";

        return temp;
    }
    public void setSet_Date(String set_Date) { this.set_Date = set_Date; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String toString(){
        String temp = "setNum: " + set_Num + "\n";
        temp += "set_Date: " + set_Date + "\n";
        temp += "content: " + content + "\n";

        return temp;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("orderNumb", set_Num);
        temp.put("content", content);
        temp.put("planDate", set_Date);

        return temp;
    }

}
