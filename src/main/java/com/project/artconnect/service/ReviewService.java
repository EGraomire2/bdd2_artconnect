package com.project.artconnect.service;

import java.util.List;

import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Review;

public interface ReviewService {
    List<Review> getAllReviews();

    List<Review> getReviewsByArtwork(Artwork artwork);

    List<Review> getReviewsByMember(CommunityMember member);

    List<Review> searchReviews(String query, String artworkTitle, String reviewerName);

    void createReview(Review review);

    void updateReview(Review review);

    void deleteReview(int reviewId);
}