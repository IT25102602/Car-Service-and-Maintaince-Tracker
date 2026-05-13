package com.carservice.tracker.controller;

import com.carservice.tracker.model.PublicReview;
import com.carservice.tracker.model.Review;
import com.carservice.tracker.model.VerifiedReview;
import com.carservice.tracker.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    @Autowired
    private ReviewService service;

    @GetMapping
    public List<Review> getAll() {
        return service.getAllReviews();
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return service.getReviewStats();
    }

    @PostMapping
    public void add(@RequestBody Map<String, Object> payload) {
        String type = (String) payload.get("reviewType");
        String id = (String) payload.get("reviewId");
        String serviceId = (String) payload.get("serviceId");
        String customerName = (String) payload.get("customerName");
        int rating = (int) payload.get("rating");
        String message = (String) payload.get("reviewMessage");
        String date = (String) payload.get("date");

        Review review;
        if ("Verified".equalsIgnoreCase(type)) {
            review = new VerifiedReview(id, serviceId, customerName, rating, message, date);
        } else {
            review = new PublicReview(id, serviceId, customerName, rating, message, date);
        }
        service.addReview(review);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteReview(id);
    }
}
