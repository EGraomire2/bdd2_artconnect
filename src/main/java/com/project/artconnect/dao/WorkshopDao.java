package com.project.artconnect.dao;

import com.project.artconnect.model.Workshop;
import java.util.List;
import java.util.Optional;

public interface WorkshopDao {
    Optional<Workshop> findById(Long id);
    Optional<Workshop> findByTitle(String title);

    void save(Workshop workshop);
    void update(Workshop workshop);
    void delete(Long id);
    
    List<Workshop> findAll();
}
