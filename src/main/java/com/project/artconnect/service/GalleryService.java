package com.project.artconnect.service;

import java.util.List;
import java.util.Optional;

import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;

public interface GalleryService {
    List<Gallery> getAllGalleries();

    Optional<Gallery> getGalleryByName(String name);

    List<Exhibition> getExhibitionsByGallery(Gallery gallery);

    List<Gallery> searchGalleries(String query, String address, String unused);

    void createGallery(Gallery gallery);

    void updateGallery(Gallery gallery);

    void deleteGallery(int galleryId);

}
