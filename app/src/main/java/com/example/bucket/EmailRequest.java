package com.example.bucket;

import com.google.gson.annotations.SerializedName;

public class EmailRequest {

    @SerializedName("email")
    public String email;

    public String toString()
    {
        return email;
    }

    public String getEmail() { return email; }

    public EmailRequest(String email) {
        this.email = email;
    }


}
