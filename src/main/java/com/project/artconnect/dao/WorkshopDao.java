package com.project.artconnect.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.project.artconnect.model.Workshop;

public interface WorkshopDao {
    Optional<Workshop> findById(Long id);
    Optional<Workshop> findByTitle(String title);

    List<Workshop> findAll();

    void save(Workshop workshop);

    void update(Workshop workshop);

    void delete(int workshopId) throws SQLException;
}
