package com.example.socialmedia.Model;

public class UserModel {
   private String name;
    private String email;
    private String profession;
    private String pass;
    private String CoverPhoto;

    private String userID;
    private int followerCount;
    private int followingCount;
    private int postCount;

    public UserModel() {
    }

    public UserModel(String name, String email, String profession, String pass) {
        this.name = name;
        this.email = email;
        this.profession = profession;
        this.pass = pass;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }







    public String getCoverPhoto() {
        return CoverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        CoverPhoto = coverPhoto;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }


    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }
}
