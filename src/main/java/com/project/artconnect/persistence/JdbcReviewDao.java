package com.project.artconnect.persistence;

import com.project.artconnect.dao.ReviewDao;
import com.project.artconnect.model.Review;
import com.project.artconnect.util.ConnectionManager;
import java.sql.*;
import java.util.*;

public class JdbcReviewDao implements ReviewDao {
    
    @Override
    public List<Review> findAll() {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                reviews.add(mapResultSetToReview(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching reviews: " + e.getMessage());
            e.printStackTrace();
        }
        return reviews;
    }
    
    @Override
    public Review save(Review review) throws SQLException {
        // Check if review already exists (UNIQUE constraint on reviewer_id, artwork_id)
        if (reviewExists(review.getReviewerId(), review.getArtworkId())) {
            throw new SQLException("Ce membre a déjà écrit une review pour cette artwork");
        }
        
        String sql = "INSERT INTO reviews (reviewer_id, artwork_id, rating, comment, review_date) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, review.getReviewerId());
            stmt.setInt(2, review.getArtworkId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());
            stmt.setDate(5, java.sql.Date.valueOf(review.getReviewDate()));
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    review.setId(generatedKeys.getInt(1));
                }
            }
        }
        return review;
    }
    
    @Override
    public Review update(Review review) throws SQLException {
        String sql = "UPDATE reviews SET reviewer_id = ?, artwork_id = ?, rating = ?, comment = ?, review_date = ? " +
                     "WHERE review_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, review.getReviewerId());
            stmt.setInt(2, review.getArtworkId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());
            stmt.setDate(5, java.sql.Date.valueOf(review.getReviewDate()));
            stmt.setInt(6, review.getId());
            
            stmt.executeUpdate();
        }
        return review;
    }
    
    @Override
    public void delete(int reviewId) throws SQLException {
        String sql = "DELETE FROM reviews WHERE review_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reviewId);
            stmt.executeUpdate();
        }
    }
    
    @Override
    public List<Review> findByArtworkId(int artworkId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE artwork_id = ? ORDER BY review_date DESC";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, artworkId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching reviews by artwork: " + e.getMessage());
            e.printStackTrace();
        }
        return reviews;
    }
    
    @Override
    public List<Review> findByMemberId(int memberId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE reviewer_id = ? ORDER BY review_date DESC";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching reviews by member: " + e.getMessage());
            e.printStackTrace();
        }
        return reviews;
    }
    
    private Review mapResultSetToReview(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setId(rs.getInt("review_id"));
        review.setReviewerId(rs.getInt("reviewer_id"));
        review.setArtworkId(rs.getInt("artwork_id"));
        review.setRating(rs.getInt("rating"));
        review.setComment(rs.getString("comment"));
        review.setReviewDate(rs.getDate("review_date").toLocalDate());
        return review;
    }

    private boolean reviewExists(int reviewerId, int artworkId) {
        String sql = "SELECT COUNT(*) FROM reviews WHERE reviewer_id = ? AND artwork_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reviewerId);
            stmt.setInt(2, artworkId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking review existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
