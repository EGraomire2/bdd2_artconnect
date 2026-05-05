package com.project.artconnect.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.project.artconnect.dao.DisciplineDao;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.util.ConnectionManager;

public class JdbcDisciplineDao implements DisciplineDao {
    
    @Override
    public List<Discipline> findAll() {
        List<Discipline> disciplines = new ArrayList<>();
        String sql = "SELECT * FROM disciplines";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                disciplines.add(mapResultSetToDiscipline(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching disciplines: " + e.getMessage());
            e.printStackTrace();
        }
        return disciplines;
    }
    
    @Override
    public Discipline save(Discipline discipline) {
        String sql = "INSERT INTO disciplines (name) VALUES (?)";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, discipline.getName());
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    discipline.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving discipline: " + e.getMessage());
            e.printStackTrace();
        }
        return discipline;
    }
    
    @Override
    public Discipline update(Discipline discipline) {
        String sql = "UPDATE disciplines SET name = ? WHERE discipline_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, discipline.getName());
            stmt.setInt(2, discipline.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating discipline: " + e.getMessage());
            e.printStackTrace();
        }
        return discipline;
    }
    
    @Override
    public void delete(int disciplineId) {
        String sql = "DELETE FROM disciplines WHERE discipline_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, disciplineId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting discipline: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public Discipline findByName(String name) {
        String sql = "SELECT * FROM disciplines WHERE name = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDiscipline(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching discipline by name: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Discipline> findByArtistId(int artistId) {
        List<Discipline> disciplines = new ArrayList<>();
        String sql = "SELECT d.* FROM disciplines d " +
                     "JOIN artist_disciplines ad ON d.discipline_id = ad.discipline_id " +
                     "WHERE ad.artist_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, artistId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    disciplines.add(mapResultSetToDiscipline(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching disciplines by artist: " + e.getMessage());
            e.printStackTrace();
        }
        return disciplines;
    }
    
    private Discipline mapResultSetToDiscipline(ResultSet rs) throws SQLException {
        Discipline discipline = new Discipline();
        discipline.setId(rs.getInt("discipline_id"));
        discipline.setName(rs.getString("name"));
        return discipline;
    }
}
