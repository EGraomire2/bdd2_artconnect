package com.project.artconnect.util;

import com.project.artconnect.persistence.JdbcArtistDao;
import com.project.artconnect.persistence.JdbcArtworkDao;
import com.project.artconnect.persistence.JdbcBookingDao;
import com.project.artconnect.persistence.JdbcCommunityMemberDao;
import com.project.artconnect.persistence.JdbcExhibitionDao;
import com.project.artconnect.persistence.JdbcGalleryDao;
import com.project.artconnect.persistence.JdbcReviewDao;
import com.project.artconnect.persistence.JdbcWorkshopDao;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.service.ArtworkService;
import com.project.artconnect.service.CommunityService;
import com.project.artconnect.service.GalleryService;
import com.project.artconnect.service.WorkshopService;
import com.project.artconnect.service.impl.JdbcArtistService;
import com.project.artconnect.service.impl.JdbcArtworkService;
import com.project.artconnect.service.impl.JdbcCommunityService;
import com.project.artconnect.service.impl.JdbcGalleryService;
import com.project.artconnect.service.impl.JdbcWorkshopService;

/**
 * Service Provider to manage singleton instances of services backed by JDBC DAOs.
 */
public class ServiceProvider {
    private static final ArtistService artistService = new JdbcArtistService(new JdbcArtistDao());
    private static final ArtworkService artworkService = new JdbcArtworkService(new JdbcArtworkDao());
    private static final GalleryService galleryService = new JdbcGalleryService(new JdbcGalleryDao(), new JdbcExhibitionDao());
    private static final WorkshopService workshopService = new JdbcWorkshopService(new JdbcWorkshopDao(), new JdbcBookingDao());
    private static final CommunityService communityService = new JdbcCommunityService(new JdbcCommunityMemberDao(), new JdbcReviewDao());

    public static ArtistService getArtistService() {
        return artistService;
    }

    public static ArtworkService getArtworkService() {
        return artworkService;
    }

    public static GalleryService getGalleryService() {
        return galleryService;
    }

    public static WorkshopService getWorkshopService() {
        return workshopService;
    }

    public static CommunityService getCommunityService() {
        return communityService;
    }
}
