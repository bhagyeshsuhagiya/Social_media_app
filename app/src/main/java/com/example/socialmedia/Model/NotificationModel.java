package com.example.socialmedia.Model;

public class NotificationModel {
   private String notificationBy;
   private long notificationAt;
   private String type;
   private String postID;
   private String notificationID;
   private String postedBy;

   public String getNotificationBy() {
      return notificationBy;
   }

   public void setNotificationBy(String notificationBy) {
      this.notificationBy = notificationBy;
   }

   public long getNotificationAt() {
      return notificationAt;
   }

   public void setNotificationAt(long notificationAt) {
      this.notificationAt = notificationAt;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getPostID() {
      return postID;
   }

   public void setPostID(String postID) {
      this.postID = postID;
   }

   public String getPostedBy() {
      return postedBy;
   }

   public void setPostedBy(String postedBy) {
      this.postedBy = postedBy;
   }

   public String getNotificationID() {
      return notificationID;
   }

   public void setNotificationID(String notificationID) {
      this.notificationID = notificationID;
   }
}