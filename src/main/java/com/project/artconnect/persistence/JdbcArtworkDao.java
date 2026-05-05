package com.project.artconnect.persistence;

import com.project.artconnect.dao.ArtworkDao;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.Artist;
import com.project.artconnect.util.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation for ArtworkDao.
 * Uses PreparedStatement for secure SQL queries and try-with-resources for proper resource management.
 */
public class JdbcArtworkDao implements ArtworkDao {

    /**
     * Maps a ResultSet row to an Artwork object.
     */
    private Artwork mapResultSetToArtwork(ResultSet rs) throws SQLException {
        Artwork artwork = new Artwork();
        artwork.setId(rs.getInt("artwork_id"));
        artwork.setTitle(rs.getString("title"));
        artwork.setCreationYear(rs.getObject("creation_year") != null ? rs.getInt("creation_year") : null);
        artwork.setType(rs.getString("type"));
        artwork.setMedium(rs.getString("medium"));
        artwork.setDimensions(rs.getString("dimensions"));
        artwork.setDescription(rs.getString("description"));
        artwork.setPrice(rs.getDouble("price"));
        
        String statusStr = rs.getString("status");
        if (statusStr != null) {
            artwork.setStatus(Artwork.Status.valueOf(statusStr));
        }
        
        return artwork;
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
    public List<Artwork> findAll() {
        List<Artwork> artworks = new ArrayList<>();
        String sql = "SELECT * FROM artworks";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                artworks.add(mapResultSetToArtwork(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all artworks: " + e.getMessage());
            e.printStackTrace();
        }
        
        return artworks;
    }

    @Override
    public void save(Artwork artwork) {
        String sql = "INSERT INTO artworks (title, creation_year, type, medium, dimensions, description, price, status, artist_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, artwork.getTitle());
            if (artwork.getCreationYear() != null) {
                stmt.setInt(2, artwork.getCreationYear());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            stmt.setString(3, artwork.getType());
            stmt.setString(4, artwork.getMedium());
            stmt.setString(5, artwork.getDimensions());
            stmt.setString(6, artwork.getDescription());
            stmt.setDouble(7, artwork.getPrice());
            stmt.setString(8, artwork.getStatus() != null ? artwork.getStatus().toString() : "FOR_SALE");
            
            // Get artist ID - either from object or search by name
            Integer artistId = null;
            if (artwork.getArtist() != null) {
                if (artwork.getArtist().getId() != null) {
                    artistId = artwork.getArtist().getId();
                } else if (artwork.getArtist().getName() != null) {
                    artistId = getArtistIdByName(conn, artwork.getArtist().getName());
                }
            }
            
            if (artistId != null) {
                stmt.setInt(9, artistId);
            } else {
                throw new SQLException("Artist not found - cannot insert artwork without valid artist_id");
            }
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Artwork saved successfully: " + artwork.getTitle());
            }
        } catch (SQLException e) {
            System.err.println("Error saving artwork: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(Artwork artwork) {
        String sql = "UPDATE artworks SET creation_year = ?, type = ?, medium = ?, dimensions = ?, description = ?, price = ?, status = ? "
                   + "WHERE title = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (artwork.getCreationYear() != null) {
                stmt.setInt(1, artwork.getCreationYear());
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }
            stmt.setString(2, artwork.getType());
            stmt.setString(3, artwork.getMedium());
            stmt.setString(4, artwork.getDimensions());
            stmt.setString(5, artwork.getDescription());
            stmt.setDouble(6, artwork.getPrice());
            stmt.setString(7, artwork.getStatus() != null ? artwork.getStatus().toString() : "FOR_SALE");
            stmt.setString(8, artwork.getTitle());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Artwork updated successfully: " + artwork.getTitle());
            }
        } catch (SQLException e) {
            System.err.println("Error updating artwork: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String title) {
        String sql = "DELETE FROM artworks WHERE title = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, title);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Artwork deleted successfully: " + title);
            } else {
                System.out.println("No artwork found with title: " + title);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting artwork: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Artwork> findByArtistName(String artistName) {
        List<Artwork> artworks = new ArrayList<>();
        String sql = "SELECT a.* FROM artworks a "
                   + "JOIN artists ar ON a.artist_id = ar.artist_id "
                   + "WHERE ar.name = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, artistName);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    artworks.add(mapResultSetToArtwork(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching artworks by artist name: " + e.getMessage());
            e.printStackTrace();
        }
        
        return artworks;
    }
}
