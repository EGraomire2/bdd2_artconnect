package com.project.artconnect.persistence;

import com.project.artconnect.dao.ArtworkTagDao;
import com.project.artconnect.model.ArtworkTag;
import com.project.artconnect.util.ConnectionManager;
import java.sql.*;
import java.util.*;

public class JdbcArtworkTagDao implements ArtworkTagDao {
    
    @Override
    public List<ArtworkTag> findAll() {
        List<ArtworkTag> tags = new ArrayList<>();
        String sql = "SELECT * FROM artwork_tags";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                tags.add(mapResultSetToTag(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching tags: " + e.getMessage());
            e.printStackTrace();
        }
        return tags;
    }
    
    @Override
    public ArtworkTag save(ArtworkTag tag) throws SQLException {
        String sql = "INSERT INTO artwork_tags (name) VALUES (?)";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, tag.getName());
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    tag.setId(generatedKeys.getInt(1));
                }
            }
        }
        return tag;
    }
    
    @Override
    public ArtworkTag update(ArtworkTag tag) throws SQLException {
        String sql = "UPDATE artwork_tags SET name = ? WHERE tag_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tag.getName());
            stmt.setInt(2, tag.getId());
            stmt.executeUpdate();
        }
        return tag;
    }
    
    @Override
    public void delete(int tagId) throws SQLException {
        String sql = "DELETE FROM artwork_tags WHERE tag_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, tagId);
            stmt.executeUpdate();
        }
    }
    
    @Override
    public ArtworkTag findByName(String name) {
        String sql = "SELECT * FROM artwork_tags WHERE name = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTag(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching tag by name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<ArtworkTag> findByArtworkId(int artworkId) {
        List<ArtworkTag> tags = new ArrayList<>();
        String sql = "SELECT t.* FROM artwork_tags t " +
                     "JOIN artwork_tag_map atm ON t.tag_id = atm.tag_id " +
                     "WHERE atm.artwork_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, artworkId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tags.add(mapResultSetToTag(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching tags by artwork: " + e.getMessage());
            e.printStackTrace();
        }
        return tags;
    }
    
    private ArtworkTag mapResultSetToTag(ResultSet rs) throws SQLException {
        ArtworkTag tag = new ArtworkTag();
        tag.setId(rs.getInt("tag_id"));
        tag.setName(rs.getString("name"));
        return tag;
    }
}
