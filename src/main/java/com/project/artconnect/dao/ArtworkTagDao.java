package com.project.artconnect.dao;

import com.project.artconnect.model.ArtworkTag;
import java.sql.SQLException;
import java.util.List;

public interface ArtworkTagDao {
    List<ArtworkTag> findAll();

    ArtworkTag save(ArtworkTag tag);
    
    ArtworkTag update(ArtworkTag tag);
    
    void delete(int tagId);
    
    ArtworkTag findByName(String name);
    
    List<ArtworkTag> findByArtworkId(int artworkId);
}
