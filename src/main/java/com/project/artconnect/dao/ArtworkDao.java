package com.project.artconnect.dao;

import java.sql.SQLException;
import java.util.List;

import com.project.artconnect.model.Artwork;

public interface ArtworkDao {
    List<Artwork> findAll();

    void save(Artwork artwork);

    void update(Artwork artwork);

    void delete(int artworkId) throws SQLException;

    List<Artwork> findByArtistName(String artistName);
}
