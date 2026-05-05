package com.project.artconnect.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.project.artconnect.dao.GalleryDao;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.util.ConnectionManager;

/**
 * JDBC implementation for GalleryDao.
 * Uses PreparedStatement for secure SQL queries and try-with-resources for proper resource management.
 */
public class JdbcGalleryDao implements GalleryDao {

    /**
     * Maps a ResultSet row to a Gallery object.
     */
    private Gallery mapResultSetToGallery(ResultSet rs) throws SQLException {
        Gallery gallery = new Gallery();
        gallery.setId(rs.getInt("gallery_id"));
        gallery.setName(rs.getString("name"));
        gallery.setAddress(rs.getString("address"));
        gallery.setOwnerName(rs.getString("owner_name"));
        gallery.setOpeningHours(rs.getString("opening_hours"));
        gallery.setContactPhone(rs.getString("contact_phone"));
        gallery.setRating(rs.getDouble("rating"));
        gallery.setWebsite(rs.getString("website"));
        return gallery;
    }

    @Override
    public Optional<Gallery> findById(Long id) {
        String sql = "SELECT * FROM galleries WHERE gallery_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToGallery(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching gallery by id: " + e.getMessage());
            e.printStackTrace();
        }
        
        return Optional.empty();
    }

    @Override
    public List<Gallery> findAll() {
        List<Gallery> galleries = new ArrayList<>();
        String sql = "SELECT * FROM galleries";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                galleries.add(mapResultSetToGallery(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all galleries: " + e.getMessage());
            e.printStackTrace();
        }
        
        return galleries;
    }

    @Override
    public Optional<Gallery> findByName(String name) {
        String sql = "SELECT * FROM galleries WHERE name = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToGallery(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching gallery by name: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }
    @Override
    public void save(Gallery gallery) {
        String sql = "INSERT INTO galleries (name, address, owner_name, opening_hours, contact_phone, rating, website) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, gallery.getName());
            stmt.setString(2, gallery.getAddress());
            stmt.setString(3, gallery.getOwnerName());
            stmt.setString(4, gallery.getOpeningHours());
            stmt.setString(5, gallery.getContactPhone());
            stmt.setDouble(6, gallery.getRating());
            stmt.setString(7, gallery.getWebsite());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving gallery: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(Gallery gallery) {
        String sql = "UPDATE galleries SET address = ?, owner_name = ?, opening_hours = ?, contact_phone = ?, rating = ?, website = ? "
                   + "WHERE name = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, gallery.getAddress());
            stmt.setString(2, gallery.getOwnerName());
            stmt.setString(3, gallery.getOpeningHours());
            stmt.setString(4, gallery.getContactPhone());
            stmt.setDouble(5, gallery.getRating());
            stmt.setString(6, gallery.getWebsite());
            stmt.setString(7, gallery.getName());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating gallery: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int galleryId) {
        String sql = "DELETE FROM galleries WHERE gallery_id = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, galleryId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting gallery: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
}