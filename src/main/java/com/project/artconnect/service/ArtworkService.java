package com.project.artconnect.service;

import java.util.List;
import java.util.Optional;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;

public interface ArtworkService {
    List<Artwork> getAllArtworks();

    Optional<Artwork> getArtworkByTitle(String title);

    List<Artwork> getArtworksByArtist(Artist artist);

    void createArtwork(Artwork artwork);

    void updateArtwork(Artwork artwork);

    void deleteArtwork(int artworkId);

    List<Artwork> searchArtworks(String query, String type, String status);
}
