package com.project.artconnect.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.project.artconnect.dao.ExhibitionArtworkDao;
import com.project.artconnect.util.ConnectionManager;

public class JdbcExhibitionArtworkDao implements ExhibitionArtworkDao {
    
    @Override
    public void addArtworkToExhibition(int exhibitionId, int artworkId) {
        String sql = "INSERT INTO exhibition_artworks (exhibition_id, artwork_id) VALUES (?, ?)";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, exhibitionId);
            stmt.setInt(2, artworkId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding artwork to exhibition: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void removeArtworkFromExhibition(int exhibitionId, int artworkId) {
        String sql = "DELETE FROM exhibition_artworks WHERE exhibition_id = ? AND artwork_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, exhibitionId);
            stmt.setInt(2, artworkId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error removing artwork from exhibition: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public List<Integer> getArtworkIdsByExhibitionId(int exhibitionId) {
        List<Integer> artworkIds = new ArrayList<>();
        String sql = "SELECT artwork_id FROM exhibition_artworks WHERE exhibition_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, exhibitionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    artworkIds.add(rs.getInt("artwork_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching artwork IDs by exhibition: " + e.getMessage());
            e.printStackTrace();
        }
        return artworkIds;
    }
    
    @Override
    public List<Integer> getExhibitionIdsByArtworkId(int artworkId) {
        List<Integer> exhibitionIds = new ArrayList<>();
        String sql = "SELECT exhibition_id FROM exhibition_artworks WHERE artwork_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, artworkId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    exhibitionIds.add(rs.getInt("exhibition_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching exhibition IDs by artwork: " + e.getMessage());
            e.printStackTrace();
        }
        return exhibitionIds;
    }
    
    @Override
    public boolean exists(int exhibitionId, int artworkId) {
        String sql = "SELECT COUNT(*) FROM exhibition_artworks WHERE exhibition_id = ? AND artwork_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, exhibitionId);
            stmt.setInt(2, artworkId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking exhibition-artwork existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
