package com.project.artconnect.dao;

import java.util.List;

import com.project.artconnect.model.Review;

public interface ReviewDao {
    List<Review> findAll();
    Review save(Review review);
    Review update(Review review);
    void delete(int reviewId);
    List<Review> findByArtworkId(int artworkId);
    List<Review> findByMemberId(int memberId);
}
