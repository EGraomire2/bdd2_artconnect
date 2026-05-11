package com.project.artconnect.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.project.artconnect.model.Booking;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.persistence.JdbcBookingDao;
import com.project.artconnect.service.BookingService;
import com.project.artconnect.service.CommunityService;
import com.project.artconnect.service.WorkshopService;

public class JdbcBookingService implements BookingService {
    private final JdbcBookingDao bookingDao;
    private final WorkshopService workshopService;
    private final CommunityService communityService;

    public JdbcBookingService(JdbcBookingDao bookingDao, WorkshopService workshopService,
            CommunityService communityService) {
        this.bookingDao = bookingDao;
        this.workshopService = workshopService;
        this.communityService = communityService;
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingDao.findAll().stream().map(this::enrichBooking).collect(Collectors.toList());
    }

    @Override
    public List<Booking> getBookingsByWorkshop(Workshop workshop) {
        if (workshop == null || workshop.getId() == null) {
            return List.of();
        }
        return bookingDao.findByWorkshopId(workshop.getId()).stream().map(this::enrichBooking).collect(Collectors.toList());
    }

    @Override
    public List<Booking> getBookingsByMember(CommunityMember member) {
        if (member == null || member.getId() == null) {
            return List.of();
        }
        return bookingDao.findByMemberId(member.getId()).stream().map(this::enrichBooking).collect(Collectors.toList());
    }

    @Override
    public List<Booking> searchBookings(String query, String paymentStatus) {
        String q = query == null ? "" : query.toLowerCase();
        return getAllBookings().stream()
                .filter(booking -> q.isEmpty() || matchesBooking(booking, q))
                .filter(booking -> paymentStatus == null || paymentStatus.isBlank()
                        || (booking.getPaymentStatus() != null
                                && booking.getPaymentStatus().equalsIgnoreCase(paymentStatus)))
                .collect(Collectors.toList());
    }

    @Override
    public boolean bookingExists(int workshopId, int memberId) {
        return bookingDao.existsBooking(workshopId, memberId);
    }

    @Override
    public void createBooking(Booking booking) {
        normalizeBooking(booking);
        bookingDao.save(booking);
    }

    @Override
    public void updateBooking(Booking booking) {
        normalizeBooking(booking);
        bookingDao.update(booking);
    }

    @Override
    public void deleteBooking(int bookingId) {
        bookingDao.delete(bookingId);
    }

    private Booking enrichBooking(Booking booking) {
        if (booking == null) {
            return null;
        }
        if (booking.getWorkshop() == null && booking.getWorkshopId() != null) {
            booking.setWorkshop(findWorkshopById(booking.getWorkshopId()));
        }
        if (booking.getMember() == null && booking.getMemberId() != null) {
            booking.setMember(findMemberById(booking.getMemberId()));
        }
        return booking;
    }

    private Workshop findWorkshopById(Integer workshopId) {
        if (workshopId == null) {
            return null;
        }
        return workshopService.getAllWorkshops().stream()
                .filter(workshop -> workshopId.equals(workshop.getId()))
                .findFirst()
                .orElse(null);
    }

    private CommunityMember findMemberById(Integer memberId) {
        if (memberId == null) {
            return null;
        }
        return communityService.getAllMembers().stream()
                .filter(member -> memberId.equals(member.getId()))
                .findFirst()
                .orElse(null);
    }

    private void normalizeBooking(Booking booking) {
        if (booking == null) {
            return;
        }
        if (booking.getWorkshopId() == null && booking.getWorkshop() != null) {
            booking.setWorkshopId(booking.getWorkshop().getId());
        }
        if (booking.getMemberId() == null && booking.getMember() != null) {
            booking.setMemberId(booking.getMember().getId());
        }
        if (booking.getBookingDate() == null) {
            booking.setBookingDate(LocalDateTime.now());
        }
        if (booking.getPaymentStatus() == null || booking.getPaymentStatus().isBlank()) {
            booking.setPaymentStatus("PENDING");
        }
    }

    private boolean matchesBooking(Booking booking, String query) {
        return String.valueOf(booking.getId()).contains(query)
                || (booking.getPaymentStatus() != null && booking.getPaymentStatus().toLowerCase().contains(query))
                || (booking.getWorkshop() != null && booking.getWorkshop().getTitle() != null
                        && booking.getWorkshop().getTitle().toLowerCase().contains(query))
                || (booking.getMember() != null && booking.getMember().getName() != null
                        && booking.getMember().getName().toLowerCase().contains(query));
    }

    public Optional<Booking> findBookingById(int bookingId) {
        return getAllBookings().stream().filter(booking -> booking.getId() != null && booking.getId() == bookingId)
                .findFirst();
    }
}