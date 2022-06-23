package com.example.bucket;

public class BKDetailImgItem {

    String Pic_bk;
    String Pic_achieve;
    String Pic_review;

    public BKDetailImgItem(String Pic_bk, String Pic_achieve, String Pic_review) {
        this.Pic_bk = Pic_bk;
        this.Pic_achieve = Pic_achieve;
        this.Pic_review = Pic_review;
    }

    public String getPic_bk() { return Pic_bk; }
    public void setPic_bk(String Pic_bk) { this.Pic_bk = Pic_bk; }

    public String getPic_achieve() { return Pic_achieve; }
    public void setPic_achieve(String Pic_achieve) { this.Pic_achieve = Pic_achieve; }

    public String getPic_review() { return Pic_review; }
    public void setPic_review(String Pic_review) { this.Pic_review = Pic_review; }


    public String toString(){
        String temp = "pic_bk: " + Pic_bk + "\n" +
                    "pic_achieve: " + Pic_achieve + "\n" +
                    "pic_review: " + Pic_review;
        return temp;
    }
}
