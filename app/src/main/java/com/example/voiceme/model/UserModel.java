package com.example.voiceme.model;

public class UserModel {
    private String id;
    private String username;
    private String phoneNumber;
    private String imageURL;

    public UserModel(String id, String username, String phoneNumber){
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
    }

    public UserModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

