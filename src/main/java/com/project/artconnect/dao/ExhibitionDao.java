package com.project.artconnect.dao;

import java.util.List;

import com.project.artconnect.model.Exhibition;

public interface ExhibitionDao {
    List<Exhibition> findAll();

    void save(Exhibition exhibition);

    void update(Exhibition exhibition);

    void delete(int exhibitionId);
}
