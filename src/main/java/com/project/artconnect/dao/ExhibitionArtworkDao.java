package com.project.artconnect.dao;

import java.util.List;

public interface ExhibitionArtworkDao {
    void addArtworkToExhibition(int exhibitionId, int artworkId);
    
    void removeArtworkFromExhibition(int exhibitionId, int artworkId);
    
    List<Integer> getArtworkIdsByExhibitionId(int exhibitionId);
    
    List<Integer> getExhibitionIdsByArtworkId(int artworkId);
    
    boolean exists(int exhibitionId, int artworkId);
}
