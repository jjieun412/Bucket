package com.example.bucket;


import com.google.gson.annotations.SerializedName;



public class SendCodeRequest {

    @SerializedName("email")
    public String email;


    public String getEmail() { return email; }



    public String toString()
    {
        return email;
    }


    public SendCodeRequest(String email) {
        this.email = email;
    }
}
