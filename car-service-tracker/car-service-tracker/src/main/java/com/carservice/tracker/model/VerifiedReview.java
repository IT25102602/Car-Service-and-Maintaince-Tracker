package com.carservice.tracker.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("VERIFIED")
public class VerifiedReview extends Review {

    public VerifiedReview() {}

    public VerifiedReview(String reviewId, String serviceId, String customerName, int rating, String reviewMessage, String date) {
        super(reviewId, serviceId, customerName, rating, reviewMessage, date);
    }

    @Override
    public String getReviewType() {
        return "Verified";
    }
}
