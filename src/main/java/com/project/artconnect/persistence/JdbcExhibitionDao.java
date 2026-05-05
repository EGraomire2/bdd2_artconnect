package com.project.artconnect.persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.project.artconnect.dao.ExhibitionDao;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.util.ConnectionManager;

/**
 * JDBC implementation for ExhibitionDao.
 * Uses PreparedStatement for secure SQL queries and try-with-resources for proper resource management.
 */
public class JdbcExhibitionDao implements ExhibitionDao {

    /**
     * Maps a ResultSet row to an Exhibition object.
     */
    private Exhibition mapResultSetToExhibition(ResultSet rs) throws SQLException {
        Exhibition exhibition = new Exhibition();
        exhibition.setId(rs.getInt("exhibition_id"));
        exhibition.setTitle(rs.getString("title"));
        exhibition.setStartDate(rs.getObject("start_date") != null ? rs.getDate("start_date").toLocalDate() : null);
        exhibition.setEndDate(rs.getObject("end_date") != null ? rs.getDate("end_date").toLocalDate() : null);
        exhibition.setDescription(rs.getString("description"));
        exhibition.setCuratorName(rs.getString("curator_name"));
        exhibition.setTheme(rs.getString("theme"));
        
        return exhibition;
    }

    /**
     * Retrieve gallery ID by gallery name from database.
     */
    private Integer getGalleryIdByName(Connection conn, String galleryName) throws SQLException {
        String sql = "SELECT gallery_id FROM galleries WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, galleryName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("gallery_id");
                }
            }
        }
        return null;
    }

    @Override
    public List<Exhibition> findAll() {
        List<Exhibition> exhibitions = new ArrayList<>();
        String sql = "SELECT * FROM exhibitions";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                exhibitions.add(mapResultSetToExhibition(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all exhibitions: " + e.getMessage());
            e.printStackTrace();
        }
        
        return exhibitions;
    }

    @Override
    public void save(Exhibition exhibition) {
        String sql = "INSERT INTO exhibitions (title, start_date, end_date, description, gallery_id, curator_name, theme) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, exhibition.getTitle());
            stmt.setDate(2, exhibition.getStartDate() != null ? Date.valueOf(exhibition.getStartDate()) : null);
            stmt.setDate(3, exhibition.getEndDate() != null ? Date.valueOf(exhibition.getEndDate()) : null);
            stmt.setString(4, exhibition.getDescription());
            
            // Get gallery ID - either from object or search by name
            Integer galleryId = null;
            if (exhibition.getGallery() != null) {
                if (exhibition.getGallery().getId() != null) {
                    galleryId = exhibition.getGallery().getId();
                } else if (exhibition.getGallery().getName() != null) {
                    galleryId = getGalleryIdByName(conn, exhibition.getGallery().getName());
                }
            }
            
            if (galleryId != null) {
                stmt.setInt(5, galleryId);
            } else {
                throw new SQLException("Gallery not found - cannot insert exhibition without valid gallery_id");
            }
            
            stmt.setString(6, exhibition.getCuratorName());
            stmt.setString(7, exhibition.getTheme());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Exhibition saved successfully: " + exhibition.getTitle());
            }
        } catch (SQLException e) {
            System.err.println("Error saving exhibition: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(Exhibition exhibition) {
        String sql = "UPDATE exhibitions SET start_date = ?, end_date = ?, description = ?, curator_name = ?, theme = ? "
                   + "WHERE title = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, exhibition.getStartDate() != null ? Date.valueOf(exhibition.getStartDate()) : null);
            stmt.setDate(2, exhibition.getEndDate() != null ? Date.valueOf(exhibition.getEndDate()) : null);
            stmt.setString(3, exhibition.getDescription());
            stmt.setString(4, exhibition.getCuratorName());
            stmt.setString(5, exhibition.getTheme());
            stmt.setString(6, exhibition.getTitle());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Exhibition updated successfully: " + exhibition.getTitle());
            }
        } catch (SQLException e) {
            System.err.println("Error updating exhibition: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int exhibitionId) {
        String sql = "DELETE FROM exhibitions WHERE exhibition_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, exhibitionId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting exhibition: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Exhibition> findByGalleryId(int galleryId) {
        List<Exhibition> exhibitions = new ArrayList<>();
        String sql = "SELECT * FROM exhibitions WHERE gallery_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, galleryId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    exhibitions.add(mapResultSetToExhibition(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching exhibitions by gallery: " + e.getMessage());
            e.printStackTrace();
        }
        
        return exhibitions;
    }
}
