package com.project.artconnect.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        if (title == null) {
            return Optional.empty();
        }
        return workshopDao.findAll().stream()
                .filter(w -> w.getTitle() != null && w.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }

    @Override
    public void bookWorkshop(Workshop workshop, CommunityMember member) {
        if (workshop == null || member == null) {
            return;
        }
        Booking booking = new Booking(workshop, member);
        bookingDao.save(booking);
        member.addBooking(booking);
    }

    @Override
    public List<Booking> getBookingsByMember(CommunityMember member) {
        if (member == null || member.getId() == null) {
            return Collections.emptyList();
        }
        return bookingDao.findByMemberId(member.getId());
    }

    @Override
    public List<Workshop> searchWorkshops(String query, String instructor, String level) {
        String q = (query == null) ? "" : query.toLowerCase();
        return workshopDao.findAll().stream()
                .filter(w -> q.isEmpty()
                        || (w.getTitle() != null && w.getTitle().toLowerCase().contains(q))
                        || (w.getInstructor() != null && w.getInstructor().getName() != null
                                && w.getInstructor().getName().toLowerCase().contains(q)))
                .filter(w -> instructor == null || instructor.isEmpty()
                        || (w.getInstructor() != null && w.getInstructor().getName().equalsIgnoreCase(instructor)))
                .filter(w -> level == null || level.isEmpty()
                        || (w.getLevel() != null && w.getLevel().equalsIgnoreCase(level)))
                .collect(Collectors.toList());
    }

    @Override
    public void createWorkshop(Workshop workshop) {
        workshopDao.save(workshop);
    }

    @Override
    public void updateWorkshop(Workshop workshop) {
        workshopDao.update(workshop);
    }

    @Override
    public void deleteWorkshop(int workshopId) {
        workshopDao.delete(workshopId);
    }
}
