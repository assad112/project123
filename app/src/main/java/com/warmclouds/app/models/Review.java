package com.warmclouds.app.models;

public class Review {
    private String id;
    private String userId;
    private String userName;
    private String nurseryId;
    private double rating;
    private String comment;
    private long timestamp;

    public Review() {
        // Default constructor required for Firestore
    }

    public Review(String userId, String userName, String nurseryId, double rating, String comment) {
        this.userId = userId;
        this.userName = userName;
        this.nurseryId = nurseryId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNurseryId() {
        return nurseryId;
    }

    public void setNurseryId(String nurseryId) {
        this.nurseryId = nurseryId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}


