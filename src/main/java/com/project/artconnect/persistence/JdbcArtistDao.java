package com.project.artconnect.persistence;

import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.util.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation for ArtistDao.
 * Uses PreparedStatement for secure SQL queries and try-with-resources for proper resource management.
 */
public class JdbcArtistDao implements ArtistDao {

    /**
     * Maps a ResultSet row to an Artist object.
     */
    private Artist mapResultSetToArtist(ResultSet rs) throws SQLException {
        Artist artist = new Artist();
        artist.setId(rs.getInt("artist_id"));
        artist.setName(rs.getString("name"));
        artist.setBio(rs.getString("bio"));
        artist.setBirthYear(rs.getObject("birth_year") != null ? rs.getInt("birth_year") : null);
        artist.setContactEmail(rs.getString("contact_email"));
        artist.setPhone(rs.getString("phone"));
        artist.setCity(rs.getString("city"));
        artist.setWebsite(rs.getString("website"));
        artist.setSocialMedia(rs.getString("social_media"));
        artist.setActive(rs.getBoolean("is_active"));
        return artist;
    }

    /**
     * Maps a ResultSet row to a Discipline object.
     */
    private Discipline mapResultSetToDiscipline(ResultSet rs) throws SQLException {
        Discipline discipline = new Discipline();
        discipline.setId(rs.getInt("discipline_id"));
        discipline.setName(rs.getString("discipline_name"));
        return discipline;
    }

    @Override
    public List<Artist> findAll() {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT * FROM artists";
        
        // try-with-resources ensures resources are automatically closed
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                artists.add(mapResultSetToArtist(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all artists: " + e.getMessage());
            e.printStackTrace();
        }
        
        return artists;
    }

    @Override
    public void save(Artist artist) {
        String sql = "INSERT INTO artists (name, bio, birth_year, contact_email, phone, city, website, social_media, is_active) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, artist.getName());
            stmt.setString(2, artist.getBio());
            stmt.setObject(3, artist.getBirthYear());
            stmt.setString(4, artist.getContactEmail());
            stmt.setString(5, artist.getPhone());
            stmt.setString(6, artist.getCity());
            stmt.setString(7, artist.getWebsite());
            stmt.setString(8, artist.getSocialMedia());
            stmt.setBoolean(9, artist.isActive());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Artist saved successfully: " + artist.getName());
            }
        } catch (SQLException e) {
            System.err.println("Error saving artist: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(Artist artist) {
        String sql = "UPDATE artists SET bio = ?, birth_year = ?, contact_email = ?, phone = ?, city = ?, website = ?, social_media = ?, is_active = ? "
                   + "WHERE name = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, artist.getBio());
            stmt.setObject(2, artist.getBirthYear());
            stmt.setString(3, artist.getContactEmail());
            stmt.setString(4, artist.getPhone());
            stmt.setString(5, artist.getCity());
            stmt.setString(6, artist.getWebsite());
            stmt.setString(7, artist.getSocialMedia());
            stmt.setBoolean(8, artist.isActive());
            stmt.setString(9, artist.getName());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Artist updated successfully: " + artist.getName());
            }
        } catch (SQLException e) {
            System.err.println("Error updating artist: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String artistName) {
        String sql = "DELETE FROM artists WHERE name = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, artistName);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Artist deleted successfully: " + artistName);
            } else {
                System.out.println("No artist found with name: " + artistName);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting artist: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Artist> findByCity(String city) {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT * FROM artists WHERE city = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, city);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    artists.add(mapResultSetToArtist(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching artists by city: " + e.getMessage());
            e.printStackTrace();
        }
        
        return artists;
    }

    @Override
    public List<Artist> findByName(String name) {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT * FROM artists WHERE name = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    artists.add(mapResultSetToArtist(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching artists by name: " + e.getMessage());
            e.printStackTrace();
        }
        
        return artists;
    }

    @Override
    public List<Discipline> getAllDisciplines() {
        List<Discipline> disciplines = new ArrayList<>();
        String sql = "SELECT * FROM disciplines";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Discipline discipline = new Discipline();
                discipline.setId(rs.getInt("discipline_id"));
                discipline.setName(rs.getString("name"));
                disciplines.add(discipline);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching disciplines: " + e.getMessage());
            e.printStackTrace();
        }
        
        return disciplines;
    }
}