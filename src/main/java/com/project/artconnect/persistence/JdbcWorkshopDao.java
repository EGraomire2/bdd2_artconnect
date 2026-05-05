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
        workshop.setId(rs.getInt("workshop_id"));
        workshop.setTitle(rs.getString("title"));
        workshop.setDate(rs.getObject("workshop_date") != null ? rs.getTimestamp("workshop_date").toLocalDateTime() : null);
        workshop.setDurationMinutes(rs.getInt("duration_minutes"));
        workshop.setMaxParticipants(rs.getInt("max_participants"));
        workshop.setPrice(rs.getDouble("price"));
        workshop.setLocation(rs.getString("location"));
        workshop.setDescription(rs.getString("description"));
        workshop.setLevel(rs.getString("level"));
        
        return workshop;
    }

    /**
     * Retrieve artist ID by artist name from database.
     */
    private Integer getArtistIdByName(Connection conn, String artistName) throws SQLException {
        String sql = "SELECT artist_id FROM artists WHERE name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, artistName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("artist_id");
                }
            }
        }
        return null;
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

    /**
     * Save a workshop to the database with proper foreign key handling.
     */
    public void save(Workshop workshop) {
        String sql = "INSERT INTO workshops (title, workshop_date, duration_minutes, max_participants, price, instructor_id, location, description, level) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, workshop.getTitle());
            stmt.setTimestamp(2, workshop.getDate() != null ? Timestamp.valueOf(workshop.getDate()) : null);
            stmt.setInt(3, workshop.getDurationMinutes());
            stmt.setInt(4, workshop.getMaxParticipants());
            stmt.setDouble(5, workshop.getPrice());
            
            // Get instructor (artist) ID - either from object or search by name
            Integer instructorId = null;
            if (workshop.getInstructor() != null) {
                if (workshop.getInstructor().getId() != null) {
                    instructorId = workshop.getInstructor().getId();
                } else if (workshop.getInstructor().getName() != null) {
                    instructorId = getArtistIdByName(conn, workshop.getInstructor().getName());
                }
            }
            
            if (instructorId != null) {
                stmt.setInt(6, instructorId);
            } else {
                throw new SQLException("Instructor artist not found - cannot insert workshop without valid instructor_id");
            }
            
            stmt.setString(7, workshop.getLocation());
            stmt.setString(8, workshop.getDescription());
            stmt.setString(9, workshop.getLevel());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Workshop saved successfully: " + workshop.getTitle());
            }
        } catch (SQLException e) {
            System.err.println("Error saving workshop: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Update a workshop in the database.
     */
    public void update(Workshop workshop) {
        String sql = "UPDATE workshops SET workshop_date = ?, duration_minutes = ?, max_participants = ?, price = ?, location = ?, description = ?, level = ? "
                   + "WHERE workshop_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, workshop.getDate() != null ? Timestamp.valueOf(workshop.getDate()) : null);
            stmt.setInt(2, workshop.getDurationMinutes());
            stmt.setInt(3, workshop.getMaxParticipants());
            stmt.setDouble(4, workshop.getPrice());
            stmt.setString(5, workshop.getLocation());
            stmt.setString(6, workshop.getDescription());
            stmt.setString(7, workshop.getLevel());
            stmt.setInt(8, workshop.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Workshop updated successfully: " + workshop.getTitle());
            }
        } catch (SQLException e) {
            System.err.println("Error updating workshop: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Delete a workshop from the database.
     */
    public void delete(Integer workshopId) {
        String sql = "DELETE FROM workshops WHERE workshop_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, workshopId);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Workshop deleted successfully");
            } else {
                System.out.println("No workshop found with id: " + workshopId);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting workshop: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
