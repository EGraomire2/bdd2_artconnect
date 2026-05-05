package com.project.artconnect.dao;

import java.util.List;

public interface ArtworkTagMapDao {
    void addTagToArtwork(int artworkId, int tagId);
    
    void removeTagFromArtwork(int artworkId, int tagId);
    
    List<Integer> getTagIdsByArtworkId(int artworkId);
    
    List<Integer> getArtworkIdsByTagId(int tagId);
    
    boolean exists(int artworkId, int tagId);
}
