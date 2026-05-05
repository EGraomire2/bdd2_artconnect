package com.project.artconnect.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.project.artconnect.dao.ArtworkTagMapDao;
import com.project.artconnect.util.ConnectionManager;

public class JdbcArtworkTagMapDao implements ArtworkTagMapDao {
    
    @Override
    public void addTagToArtwork(int artworkId, int tagId) {
        String sql = "INSERT INTO artwork_tag_map (artwork_id, tag_id) VALUES (?, ?)";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, artworkId);
            stmt.setInt(2, tagId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding tag to artwork: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void removeTagFromArtwork(int artworkId, int tagId) {
        String sql = "DELETE FROM artwork_tag_map WHERE artwork_id = ? AND tag_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, artworkId);
            stmt.setInt(2, tagId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error removing tag from artwork: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public List<Integer> getTagIdsByArtworkId(int artworkId) {
        List<Integer> tagIds = new ArrayList<>();
        String sql = "SELECT tag_id FROM artwork_tag_map WHERE artwork_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, artworkId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tagIds.add(rs.getInt("tag_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching tag IDs by artwork: " + e.getMessage());
            e.printStackTrace();
        }
        return tagIds;
    }
    
    @Override
    public List<Integer> getArtworkIdsByTagId(int tagId) {
        List<Integer> artworkIds = new ArrayList<>();
        String sql = "SELECT artwork_id FROM artwork_tag_map WHERE tag_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, tagId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    artworkIds.add(rs.getInt("artwork_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching artwork IDs by tag: " + e.getMessage());
            e.printStackTrace();
        }
        return artworkIds;
    }
    
    @Override
    public boolean exists(int artworkId, int tagId) {
        String sql = "SELECT COUNT(*) FROM artwork_tag_map WHERE artwork_id = ? AND tag_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, artworkId);
            stmt.setInt(2, tagId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking artwork-tag existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
