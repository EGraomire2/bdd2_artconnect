package com.project.artconnect.persistence;

import com.project.artconnect.dao.ExhibitionDao;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.util.ConnectionManager;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        exhibition.setTitle(rs.getString("title"));
        exhibition.setStartDate(rs.getObject("start_date") != null ? rs.getDate("start_date").toLocalDate() : null);
        exhibition.setEndDate(rs.getObject("end_date") != null ? rs.getDate("end_date").toLocalDate() : null);
        exhibition.setDescription(rs.getString("description"));
        exhibition.setCuratorName(rs.getString("curator_name"));
        exhibition.setTheme(rs.getString("theme"));
        
        // Note: gallery_id is in the result set but we'd need GalleryDao to load the full Gallery object
        // For now, just note the gallery_id could be retrieved if needed
        
        return exhibition;
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
            
            // Note: You'll need to handle gallery_id - this is simplified
            if (exhibition.getGallery() != null) {
                // For now, we assume gallery exists - you may need to query it by name
                stmt.setNull(5, java.sql.Types.INTEGER);
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
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
    public void delete(String title) {
        String sql = "DELETE FROM exhibitions WHERE title = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, title);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Exhibition deleted successfully: " + title);
            } else {
                System.out.println("No exhibition found with title: " + title);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting exhibition: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
