package com.model;

import java.io.Serializable;

public class AdminModel implements Serializable {
    private String email;
    private String username;
    private String password;
    private String profile_image;

//    for JSON parsing with Gson, there's no need for a separate constructor — Gson uses the default no-arg constructor and setters.
    public AdminModel(){

    }
    public AdminModel(
            String email,
            String username,
            String password,
            String profile_image)
    {
        this.email = email;
        this.username = username;
        this.password = password;
        this.profile_image = profile_image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

}
