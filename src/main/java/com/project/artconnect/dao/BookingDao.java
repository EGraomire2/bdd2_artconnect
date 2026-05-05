package com.project.artconnect.dao;

import java.util.List;

import com.project.artconnect.model.Booking;

public interface BookingDao {
    List<Booking> findAll();
    Booking save(Booking booking);
    Booking update(Booking booking);
    void delete(int bookingId);
    List<Booking> findByWorkshopId(int workshopId);
    List<Booking> findByMemberId(int memberId);
    boolean existsBooking(int workshopId, int memberId);
}
