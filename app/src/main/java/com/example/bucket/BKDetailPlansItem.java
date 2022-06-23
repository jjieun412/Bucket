package com.example.bucket;


public class BKDetailPlansItem {

    String date;
    String content;

    public BKDetailPlansItem(String date, String content) {
        this.date = date;
        this.content = content;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }


    public String toString(){ return date + " " + content; }
}
