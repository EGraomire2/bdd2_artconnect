package com.project.artconnect.dao;

import java.util.List;

import com.project.artconnect.model.Discipline;

public interface DisciplineDao {
    List<Discipline> findAll();
    
    Discipline save(Discipline discipline);
    
    Discipline update(Discipline discipline);
    
    void delete(int disciplineId);
    
    Discipline findByName(String name);
    
    List<Discipline> findByArtistId(int artistId);
}
