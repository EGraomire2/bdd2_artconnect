package com.project.artconnect.dao;

import java.sql.SQLException;
import java.util.List;

public interface ExhibitionArtworkDao {
    void addArtworkToExhibition(int exhibitionId, int artworkId) throws SQLException;
    void removeArtworkFromExhibition(int exhibitionId, int artworkId) throws SQLException;
    List<Integer> getArtworkIdsByExhibitionId(int exhibitionId);
    List<Integer> getExhibitionIdsByArtworkId(int artworkId);
    boolean exists(int exhibitionId, int artworkId);
}
