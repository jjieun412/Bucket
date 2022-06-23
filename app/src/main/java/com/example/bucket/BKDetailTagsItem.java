package com.example.bucket;

public class BKDetailTagsItem {

    String Tag;

    public BKDetailTagsItem(String Tag) {
        this.Tag = Tag;
    }

    public String getTag() { return Tag; }
    public void setTag(String Tag) { this.Tag = Tag; }


    public String toString(){
        String temp = "tags: " + Tag + "\n";
        return temp;
    }

}
