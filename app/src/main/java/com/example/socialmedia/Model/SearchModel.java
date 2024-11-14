package com.example.socialmedia.Model;

public class SearchModel {
    private int profileImg;
    private String name, profession, button;
    public SearchModel(int profileImg, String name, String profession, String button) {

        this.profileImg = profileImg;
        this.name = name;
        this.profession = profession;
        this.button = button;

    }
    public int getProfileImg() {
        return profileImg;
    }
    public void setProfileImg(int profileImg) {
        this.profileImg = profileImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }
}

