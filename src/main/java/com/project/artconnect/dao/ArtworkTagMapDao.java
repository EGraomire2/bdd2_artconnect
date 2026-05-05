package com.project.artconnect.dao;

import java.sql.SQLException;
import java.util.List;

public interface ArtworkTagMapDao {
    void addTagToArtwork(int artworkId, int tagId) throws SQLException;
    void removeTagFromArtwork(int artworkId, int tagId) throws SQLException;
    List<Integer> getTagIdsByArtworkId(int artworkId);
    List<Integer> getArtworkIdsByTagId(int tagId);
    boolean exists(int artworkId, int tagId);
}
