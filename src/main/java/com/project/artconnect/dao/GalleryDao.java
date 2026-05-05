package com.project.artconnect.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.project.artconnect.model.Gallery;


public interface GalleryDao {
    Optional<Gallery> findById(Long id);
    Optional<Gallery> findByName(String name);

    List<Gallery> findAll();

    void save(Gallery gallery);

    void update(Gallery gallery);

    void delete(int galleryId) throws SQLException;
}
