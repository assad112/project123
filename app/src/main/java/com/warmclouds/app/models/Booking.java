package com.warmclouds.app.models;

public class Booking {
    private String id;
    private String userId;
    private String nurseryId;
    private String nurseryName;
    private String childName;
    private String childAge;
    private String ageGroup;
    private String parentName;
    private String parentPhone;
    private String parentEmail;
    private double registrationFee;
    private String status; // "pending", "confirmed", "cancelled"
    private long bookingDate;
    private String bookingCode;

    public Booking() {
        // Default constructor required for Firestore
    }

    public Booking(String userId, String nurseryId, String nurseryName) {
        this.userId = userId;
        this.nurseryId = nurseryId;
        this.nurseryName = nurseryName;
        this.status = "pending";
        this.bookingDate = System.currentTimeMillis();
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

    public String getNurseryId() {
        return nurseryId;
    }

    public void setNurseryId(String nurseryId) {
        this.nurseryId = nurseryId;
    }

    public String getNurseryName() {
        return nurseryName;
    }

    public void setNurseryName(String nurseryName) {
        this.nurseryName = nurseryName;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildAge() {
        return childAge;
    }

    public void setChildAge(String childAge) {
        this.childAge = childAge;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    public double getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(double registrationFee) {
        this.registrationFee = registrationFee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(long bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }
}


