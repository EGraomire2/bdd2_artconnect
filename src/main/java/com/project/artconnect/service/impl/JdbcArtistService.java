package com.project.artconnect.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.persistence.JdbcArtistDao;
import com.project.artconnect.service.ArtistService;

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
                .filter(a -> disciplineName == null || a.getDisciplines().stream()
                        .anyMatch(d -> d.getName().equals(disciplineName)))
                .collect(Collectors.toList());
    }
}
