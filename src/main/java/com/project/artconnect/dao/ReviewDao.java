package com.project.artconnect.dao;

import com.project.artconnect.model.Review;
import java.sql.SQLException;
import java.util.List;

public interface ReviewDao {
    List<Review> findAll();
    Review save(Review review) throws SQLException;
    Review update(Review review) throws SQLException;
    void delete(int reviewId) throws SQLException;
    List<Review> findByArtworkId(int artworkId);
    List<Review> findByMemberId(int memberId);
}
