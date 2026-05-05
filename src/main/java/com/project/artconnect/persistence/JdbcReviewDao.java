package com.project.artconnect.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.project.artconnect.dao.ReviewDao;
import com.project.artconnect.model.Review;
import com.project.artconnect.util.ConnectionManager;

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
    public Review save(Review review) {
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
        } catch (SQLException e) {
            System.err.println("Error saving review: " + e.getMessage());
            e.printStackTrace();
        }
        return review;
    }
    
    @Override
    public Review update(Review review) {
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
        } catch (SQLException e) {
            System.err.println("Error updating review: " + e.getMessage());
            e.printStackTrace();
        }
        return review;
    }
    
    @Override
    public void delete(int reviewId) {
        String sql = "DELETE FROM reviews WHERE review_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reviewId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting review: " + e.getMessage());
            e.printStackTrace();
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

    // reviewExists() removed - let DB handle UNIQUE constraint
}
