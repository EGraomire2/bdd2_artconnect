package com.project.artconnect.dao;

import com.project.artconnect.model.ArtworkTag;
import java.sql.SQLException;
import java.util.List;

public interface ArtworkTagDao {
    List<ArtworkTag> findAll();
    ArtworkTag save(ArtworkTag tag) throws SQLException;
    ArtworkTag update(ArtworkTag tag) throws SQLException;
    void delete(int tagId) throws SQLException;
    ArtworkTag findByName(String name);
    List<ArtworkTag> findByArtworkId(int artworkId);
}
