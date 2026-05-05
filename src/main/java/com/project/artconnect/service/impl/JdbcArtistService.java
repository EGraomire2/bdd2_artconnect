package com.project.artconnect.service.impl;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.persistence.JdbcArtistDao;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Artist Service implementation using JDBC DAO for database access.
 */
public class JdbcArtistService implements ArtistService {
    private final JdbcArtistDao artistDao;

    public JdbcArtistService(JdbcArtistDao artistDao) {
        this.artistDao = artistDao;
    }

    @Override
    public List<Artist> getAllArtists() {
        return artistDao.findAll();
    }

    @Override
    public Optional<Artist> getArtistByName(String name) {
        return artistDao.findAll().stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public void createArtist(Artist artist) {
        artistDao.save(artist);
    }

    @Override
    public void updateArtist(Artist artist) {
        artistDao.update(artist);
    }

    @Override
    public void deleteArtist(int artistId) {
        artistDao.delete(artistId);
    }

    @Override
    public List<Discipline> getAllDisciplines() {
        return artistDao.getAllDisciplines();
    }

    @Override
    public List<Artist> searchArtists(String query, String disciplineName, String city) {
        List<Artist> artists = artistDao.findAll();
        
        return artists.stream()
                .filter(a -> query == null || a.getName().toLowerCase().contains(query.toLowerCase()))
                .filter(a -> city == null || city.isEmpty() || a.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }
}
