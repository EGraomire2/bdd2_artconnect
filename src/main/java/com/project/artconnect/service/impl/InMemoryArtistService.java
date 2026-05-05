package com.project.artconnect.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.persistence.JdbcArtistDao;
import com.project.artconnect.persistence.JdbcDisciplineDao;
import com.project.artconnect.service.ArtistService;

public class InMemoryArtistService implements ArtistService {
    private final Map<String, Artist> artists = new LinkedHashMap<>();
    private final Map<String, Discipline> disciplines = new LinkedHashMap<>();
    private JdbcArtistDao jdbcArtistDao = new JdbcArtistDao();
    private JdbcDisciplineDao jdbcDisciplineDao = new JdbcDisciplineDao();

    public InMemoryArtistService() {
        initData();
    }

    private void initData() {
        // Disciplines
        addDiscipline("Painting");
        addDiscipline("Sculpture");
        addDiscipline("Photography");
        addDiscipline("Digital Art");
        addDiscipline("Music");

        // Artists
        addArtist("Leonardo Vinci", "Renaissance master and polymath.", 1452, "leo@vincistudio.it", "Florence",
                "Painting", "Sculpture");
        addArtist("Claude Monet", "Founder of French Impressionist painting.", 1840, "claude@monet.fr", "Giverny",
                "Painting");
        addArtist("Ansel Adams", "American landscape photographer and environmentalist.", 1902, "ansel@adams.co",
                "San Francisco", "Photography");
        addArtist("Frida Kahlo", "Mexican painter known for her many portraits and self-portraits.", 1907,
                "frida@kahlo.mx", "Mexico City", "Painting");
        addArtist("Auguste Rodin", "French sculptor, generally considered the founder of modern sculpture.", 1840,
                "auguste@rodin.fr", "Paris", "Sculpture");
    }

    private void addDiscipline(String name) {
        Discipline d = new Discipline();
        d.setName(name);
        jdbcDisciplineDao.save(d); // Sauvegarde de la discipline dans la base de données
    }

    private void addArtist(String name, String bio, int year, String email, String city, String... disciplineNames) {
        Artist a = new Artist(name, bio, year, email, city);
        for (String dName : disciplineNames) {
            if (disciplines.containsKey(dName)) {
                a.getDisciplines().add(disciplines.get(dName));
            }
        }
        artists.put(name, a);
        jdbcArtistDao.save(a); // Sauvegarde de l'artiste dans la base de données
    }

    @Override
    public List<Artist> getAllArtists() {
        return jdbcArtistDao.findAll(); // Récupération de tous les artistes depuis la base de données
    }

    @Override
    public Optional<Artist> getArtistByName(String name) {
        return jdbcArtistDao.findByName(name).stream().findFirst(); // Récupération de l'artiste par nom depuis la base de données
    }

    @Override
    public void createArtist(Artist artist) {
        artists.put(artist.getName(), artist);
        jdbcArtistDao.save(artist); // Sauvegarde de l'artiste dans la base de données
    }

    @Override
    public void updateArtist(Artist artist) {
        artists.put(artist.getName(), artist);
        jdbcArtistDao.update(artist); // Mise à jour de l'artiste dans la base de données
    }

    @Override
    public void deleteArtist(int artistId) {
        // Find and remove artist by ID
        Artist artistToDelete = null;
        for (Artist artist : artists.values()) {
            if (artist.getId() != null && artist.getId() == artistId) {
                artistToDelete = artist;
                break;
            }
        }
        if (artistToDelete != null) {
            artists.remove(artistToDelete.getName());
        }
        
        // Delete from database
        jdbcArtistDao.delete(artistId);
    }

    @Override
    public List<Discipline> getAllDisciplines() {
        return new ArrayList<>(disciplines.values());
    }

    @Override
    public List<Artist> searchArtists(String query, String disciplineName, String city) {
        return artists.values().stream()
                .filter(a -> query == null || a.getName().toLowerCase().contains(query.toLowerCase()))
                .filter(a -> city == null || city.isEmpty() || a.getCity().equalsIgnoreCase(city))
                .filter(a -> disciplineName == null
                        || a.getDisciplines().stream().anyMatch(d -> d.getName().equals(disciplineName)))
                .collect(Collectors.toList());
    }
}
