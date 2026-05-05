package com.project.artconnect.persistence;

import com.project.artconnect.dao.WorkshopDao;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.util.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation for WorkshopDao.
 * Uses PreparedStatement for secure SQL queries and try-with-resources for proper resource management.
 */
public class JdbcWorkshopDao implements WorkshopDao {

    /**
     * Maps a ResultSet row to a Workshop object.
     */
    private Workshop mapResultSetToWorkshop(ResultSet rs) throws SQLException {
        Workshop workshop = new Workshop();
        workshop.setTitle(rs.getString("title"));
        workshop.setDate(rs.getObject("workshop_date") != null ? rs.getTimestamp("workshop_date").toLocalDateTime() : null);
        workshop.setDurationMinutes(rs.getInt("duration_minutes"));
        workshop.setMaxParticipants(rs.getInt("max_participants"));
        workshop.setPrice(rs.getDouble("price"));
        workshop.setLocation(rs.getString("location"));
        workshop.setDescription(rs.getString("description"));
        workshop.setLevel(rs.getString("level"));
        
        // Note: instructor_id is in result set but would need ArtistDao to load full Artist object
        
        return workshop;
    }

    @Override
    public Optional<Workshop> findById(Long id) {
        String sql = "SELECT * FROM workshops WHERE workshop_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToWorkshop(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching workshop by id: " + e.getMessage());
            e.printStackTrace();
        }
        
        return Optional.empty();
    }

    @Override
    public List<Workshop> findAll() {
        List<Workshop> workshops = new ArrayList<>();
        String sql = "SELECT * FROM workshops";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                workshops.add(mapResultSetToWorkshop(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all workshops: " + e.getMessage());
            e.printStackTrace();
        }
        
        return workshops;
    }
}
