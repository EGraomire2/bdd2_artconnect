package com.project.artconnect.dao;

import java.sql.SQLException;
import java.util.List;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;

/**
 * Data Access Object for Artist entity.
 */
public interface ArtistDao {
    List<Artist> findAll();

    void save(Artist artist);

    void update(Artist artist);

    void delete(int artistId) throws SQLException;

    List<Artist> findByCity(String city);

    List<Artist> findByName(String name);

    List<Discipline> getAllDisciplines();

    List<Discipline> getArtistDisciplines(int artistId);

    void addDisciplineToArtist(int artistId, int disciplineId) throws SQLException;

    void removeDisciplineFromArtist(int artistId, int disciplineId) throws SQLException;

    boolean artistHasDiscipline(int artistId, int disciplineId);
}
