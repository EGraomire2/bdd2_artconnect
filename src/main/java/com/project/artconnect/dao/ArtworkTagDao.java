package com.project.artconnect.dao;

import java.util.List;

import com.project.artconnect.model.ArtworkTag;

public interface ArtworkTagDao {
    List<ArtworkTag> findAll();

    ArtworkTag save(ArtworkTag tag);
    
    ArtworkTag update(ArtworkTag tag);
    
    void delete(int tagId);
    
    ArtworkTag findByName(String name);
    
    List<ArtworkTag> findByArtworkId(int artworkId);
}
