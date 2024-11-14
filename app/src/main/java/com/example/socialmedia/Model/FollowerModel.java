package com.example.socialmedia.Model;

public class FollowerModel {
    private String followedAt;
    private String followedBy;

    public FollowerModel() {
    }

    public String getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(String followedAt) {
        this.followedAt = followedAt;
    }

    public String getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(String followedBy) {
        this.followedBy = followedBy;
    }
}
