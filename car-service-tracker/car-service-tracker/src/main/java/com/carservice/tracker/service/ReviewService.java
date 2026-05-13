package com.carservice.tracker.service;

import com.carservice.tracker.model.Review;
import com.carservice.tracker.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository repository;

    public void addReview(Review review) {
        repository.save(review);
    }

    public List<Review> getAllReviews() {
        return repository.findAll();
    }

    public List<Review> filterByRating(int rating) {
        return repository.findByRating(rating);
    }

    public void deleteReview(String reviewId) {
        repository.deleteById(reviewId);
    }

    public Map<String, Object> getReviewStats() {
        List<Review> reviews = repository.findAll();
        Map<String, Object> stats = new HashMap<>();
        
        double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        long total = reviews.size();
        
        stats.put("averageRating", avg);
        stats.put("totalReviews", total);
        return stats;
    }
}
