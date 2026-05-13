package com.carservice.tracker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "review_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Review {
    @Id
    private String reviewId;
    private String serviceId;
    private String customerName;
    private int rating;
    private String reviewMessage;
    private String date;

    public Review() {}

    public Review(String reviewId, String serviceId, String customerName, int rating, String reviewMessage, String date) {
        this.reviewId = reviewId;
        this.serviceId = serviceId;
        this.customerName = customerName;
        this.rating = rating;
        this.reviewMessage = reviewMessage;
        this.date = date;
    }

    // Getters and Setters
    public String getReviewId() { return reviewId; }
    public void setReviewId(String reviewId) { this.reviewId = reviewId; }
    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getReviewMessage() { return reviewMessage; }
    public void setReviewMessage(String reviewMessage) { this.reviewMessage = reviewMessage; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    @Transient
    public abstract String getReviewType();
}
