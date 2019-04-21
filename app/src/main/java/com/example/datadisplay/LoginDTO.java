package com.example.datadisplay;

import com.google.gson.annotations.SerializedName;

public class LoginDTO {

    @SerializedName("username")
    String username;

    @SerializedName("password")
    String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
