package com.project.artconnect.persistence;

import com.project.artconnect.dao.ExhibitionDao;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.util.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class jdbcExhibitionDao implements ExhibitionDao {
    
    private Exhibition mapResultSetToExhibition(ResultSet rs) throws SQLException {
        Exhibition exhibition = new Exhibition();
        exhibition.setTitle(rs.getString("title"));
        exhibition.setStartDate(rs.getDate("start_date").toLocalDate());
        exhibition.setEndDate(rs.getDate("end_date").toLocalDate());
        exhibition.setDescription(rs.getString("description"));
        // Assuming gallery and curatorName are stored as strings for simplicity
        // In a real application, you would likely have separate tables and DAOs for these
        return exhibition;
    }

    @Override
    public List<Exhibition> findAll() {
        // Implementation to fetch all exhibitions from the database
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
        String sql = "INSERT INTO exhibitions (title, start_date, end_date, description, curator_name, theme) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, exhibition.getTitle());
            stmt.setDate(2, java.sql.Date.valueOf(exhibition.getStartDate()));
            stmt.setDate(3, java.sql.Date.valueOf(exhibition.getEndDate()));
            stmt.setString(4, exhibition.getDescription());
            stmt.setString(5, exhibition.getCuratorName());
            stmt.setString(6, exhibition.getTheme());

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

            stmt.setDate(1, java.sql.Date.valueOf(exhibition.getStartDate()));
            stmt.setDate(2, java.sql.Date.valueOf(exhibition.getEndDate()));
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
