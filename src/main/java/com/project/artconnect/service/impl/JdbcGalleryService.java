package com.project.artconnect.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.persistence.JdbcExhibitionDao;
import com.project.artconnect.persistence.JdbcGalleryDao;
import com.project.artconnect.service.GalleryService;

/**
 * Gallery Service implementation using JDBC DAO for database access.
 */
public class JdbcGalleryService implements GalleryService {
    private final JdbcGalleryDao galleryDao;
    private final JdbcExhibitionDao exhibitionDao;

    public JdbcGalleryService(JdbcGalleryDao galleryDao, JdbcExhibitionDao exhibitionDao) {
        this.galleryDao = galleryDao;
        this.exhibitionDao = exhibitionDao;
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
        if (gallery == null || gallery.getId() == null) {
            return Collections.emptyList();
        }
        return exhibitionDao.findByGalleryId(gallery.getId());
    }
}
