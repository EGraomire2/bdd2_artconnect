package com.project.artconnect.persistence;

import com.project.artconnect.dao.ArtworkDao;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.ArtworkTag;
import com.project.artconnect.util.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation for ArtworkDao.
 */
public class JdbcArtworkDao implements ArtworkDao {

    /**
     * Maps a ResultSet row to an Artwork object.
     */
    private Artwork mapResultSetToArtwork(ResultSet rs) throws SQLException {
        Artwork artwork = new Artwork();
        artwork.setTitle(rs.getString("title"));
        artwork.setCreationYear(rs.getObject("creation_year") != null ? rs.getInt("creation_year") : null);
        artwork.setType(rs.getString("type"));
        artwork.setMedium(rs.getString("medium"));
        artwork.setDimensions(rs.getString("dimensions"));
        artwork.setDescription(rs.getString("description"));
        artwork.setPrice(rs.getDouble("price"));
        artwork.setStatus(Artwork.Status.valueOf(rs.getString("status")));
        
        // Load artist
        Artist artist = new Artist();
        artist.setName(rs.getString("artist_name"));
        artwork.setArtist(artist);
        
        return artwork;
    }

    @Override
    public List<Artwork> findAll() {
        List<Artwork> artworks = new ArrayList<>();
        String sql = "SELECT a.*, art.name as artist_name FROM artworks a JOIN artists art ON a.artist_id = art.artist_id";
        
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
        
        // Load tags for all artworks
        loadTagsForArtworks(artworks);
        
        return artworks;
    }

    @Override
    public void save(Artwork artwork) {
        String sql = "INSERT INTO artworks (title, creation_year, type, medium, dimensions, description, price, status, artist_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, (SELECT artist_id FROM artists WHERE name = ?))";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, artwork.getTitle());
            stmt.setObject(2, artwork.getCreationYear());
            stmt.setString(3, artwork.getType());
            stmt.setString(4, artwork.getMedium());
            stmt.setString(5, artwork.getDimensions());
            stmt.setString(6, artwork.getDescription());
            stmt.setDouble(7, artwork.getPrice());
            stmt.setString(8, artwork.getStatus().name());
            stmt.setString(9, artwork.getArtist().getName());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Artwork saved successfully: " + artwork.getTitle());
                // Save tags if any
                saveArtworkTags(artwork);
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
            
            stmt.setObject(1, artwork.getCreationYear());
            stmt.setString(2, artwork.getType());
            stmt.setString(3, artwork.getMedium());
            stmt.setString(4, artwork.getDimensions());
            stmt.setString(5, artwork.getDescription());
            stmt.setDouble(6, artwork.getPrice());
            stmt.setString(7, artwork.getStatus().name());
            stmt.setString(8, artwork.getTitle());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Artwork updated successfully: " + artwork.getTitle());
                // Update tags
                updateArtworkTags(artwork);
            }
        } catch (SQLException e) {
            System.err.println("Error updating artwork: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String title) {
        // First delete tags
        deleteArtworkTags(title);
        
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
        String sql = "SELECT a.*, art.name as artist_name FROM artworks a JOIN artists art ON a.artist_id = art.artist_id WHERE art.name = ?";
        
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
        
        // Load tags for artworks
        loadTagsForArtworks(artworks);
        
        return artworks;
    }

    private void saveArtworkTags(Artwork artwork) {
        if (artwork.getTags() == null || artwork.getTags().isEmpty()) {
            return;
        }
        
        String sql = "INSERT INTO artwork_tag_map (artwork_id, tag_id) "
                   + "VALUES ((SELECT artwork_id FROM artworks WHERE title = ?), (SELECT tag_id FROM artwork_tags WHERE name = ?))";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (ArtworkTag tag : artwork.getTags()) {
                stmt.setString(1, artwork.getTitle());
                stmt.setString(2, tag.getName());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error saving artwork tags: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateArtworkTags(Artwork artwork) {
        // Delete existing tags
        deleteArtworkTags(artwork.getTitle());
        // Save new tags
        saveArtworkTags(artwork);
    }

    private void deleteArtworkTags(String title) {
        String sql = "DELETE FROM artwork_tag_map WHERE artwork_id = (SELECT artwork_id FROM artworks WHERE title = ?)";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, title);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting artwork tags: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadTagsForArtworks(List<Artwork> artworks) {
        for (Artwork artwork : artworks) {
            loadTagsForArtwork(artwork);
        }
    }
    
    private void loadTagsForArtwork(Artwork artwork) {
        String sql = "SELECT at.name FROM artwork_tags at JOIN artwork_tag_map atm ON at.tag_id = atm.tag_id "
                   + "JOIN artworks a ON atm.artwork_id = a.artwork_id WHERE a.title = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, artwork.getTitle());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ArtworkTag tag = new ArtworkTag(rs.getString("name"));
                    artwork.getTags().add(tag);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading tags for artwork: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
