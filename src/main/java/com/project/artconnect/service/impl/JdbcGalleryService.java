package com.project.artconnect.service.impl;

import com.project.artconnect.model.Gallery;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.service.GalleryService;
import com.project.artconnect.persistence.JdbcGalleryDao;
import java.util.*;

/**
 * Gallery Service implementation using JDBC DAO for database access.
 */
public class JdbcGalleryService implements GalleryService {
    private final JdbcGalleryDao galleryDao;

    public JdbcGalleryService(JdbcGalleryDao galleryDao) {
        this.galleryDao = galleryDao;
    }

    @Override
    public List<Gallery> getAllGalleries() {
        return galleryDao.findAll();
    }

    @Override
    public Optional<Gallery> getGalleryByName(String name) {
        return galleryDao.findAll().stream()
                .filter(g -> g.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public List<Exhibition> getExhibitionsByGallery(Gallery gallery) {
        if (gallery == null || gallery.getExhibitions() == null) {
            return Collections.emptyList();
        }
        return gallery.getExhibitions();
    }
}
