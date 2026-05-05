package com.project.artconnect.dao;

import com.project.artconnect.model.Discipline;
import java.sql.SQLException;
import java.util.List;

public interface DisciplineDao {
    List<Discipline> findAll();
    Discipline save(Discipline discipline) throws SQLException;
    Discipline update(Discipline discipline) throws SQLException;
    void delete(int disciplineId) throws SQLException;
    Discipline findByName(String name);
    List<Discipline> findByArtistId(int artistId);
}
