package com.project.artconnect.dao;

import com.project.artconnect.model.Gallery;
import java.util.List;
import java.util.Optional;

public interface GalleryDao {
    Optional<Gallery> findById(Long id);
    Optional<Gallery> findByName(String name);

    void save(Gallery gallery);
    void update(Gallery gallery);
    void delete(Long id);  
    
    List<Gallery> findAll();
}
