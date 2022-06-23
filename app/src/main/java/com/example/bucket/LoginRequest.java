package com.example.bucket;


import com.google.gson.annotations.SerializedName;


public class LoginRequest {

    @SerializedName("email")
    public String email;

    @SerializedName("pswd")
    public String pswd;


    public String getEmail() { return email; }
    public String getPswd() { return pswd; }


    public String toString()
     {
         return email + " " + pswd;
     }


    public LoginRequest(String email, String pswd) {
        this.email = email;
        this.pswd = pswd;
    }

}
