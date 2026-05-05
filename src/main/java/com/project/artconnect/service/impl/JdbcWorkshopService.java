package com.project.artconnect.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.project.artconnect.model.Booking;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.persistence.JdbcBookingDao;
import com.project.artconnect.persistence.JdbcWorkshopDao;
import com.project.artconnect.service.WorkshopService;

/**
 * Workshop Service implementation using JDBC DAO for database access.
 */
public class JdbcWorkshopService implements WorkshopService {
    private final JdbcWorkshopDao workshopDao;
    private final JdbcBookingDao bookingDao;

    public JdbcWorkshopService(JdbcWorkshopDao workshopDao, JdbcBookingDao bookingDao) {
        this.workshopDao = workshopDao;
        this.bookingDao = bookingDao;
    }

    @Override
    public List<Workshop> getAllWorkshops() {
        return workshopDao.findAll();
    }

    @Override
    public Optional<Workshop> getWorkshopByTitle(String title) {
        return workshopDao.findAll().stream()
                .filter(w -> w.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }

    @Override
    public void bookWorkshop(Workshop workshop, CommunityMember member) {
        if (workshop == null || member == null) {
            return;
        }
        Booking b = new Booking(workshop, member);
        bookingDao.save(b);
        member.addBooking(b);
    }

    @Override
    public List<Booking> getBookingsByMember(CommunityMember member) {
        if (member == null || member.getId() == null) {
            return Collections.emptyList();
        }
        return bookingDao.findByMemberId(member.getId());
    }
}
