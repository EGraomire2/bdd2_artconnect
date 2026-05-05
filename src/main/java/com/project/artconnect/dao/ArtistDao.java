package com.project.artconnect.dao;

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

    void delete(int artistId);

    List<Artist> findByCity(String city);

    List<Artist> findByName(String name);

    List<Discipline> getAllDisciplines();

    List<Discipline> getArtistDisciplines(int artistId);

    void addDisciplineToArtist(int artistId, int disciplineId);

    void removeDisciplineFromArtist(int artistId, int disciplineId);

    boolean artistHasDiscipline(int artistId, int disciplineId);
}
