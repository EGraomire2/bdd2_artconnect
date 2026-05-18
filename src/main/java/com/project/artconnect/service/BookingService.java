package com.project.artconnect.service;

import java.util.List;

import com.project.artconnect.model.Booking;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Workshop;

public interface BookingService {
    List<Booking> getAllBookings();

    List<Booking> getBookingsByWorkshop(Workshop workshop);

    List<Booking> getBookingsByMember(CommunityMember member);

    List<Booking> searchBookings(String query, String paymentStatus);

    boolean bookingExists(int workshopId, int memberId);

    void createBooking(Booking booking);

    void updateBooking(Booking booking);

    void deleteBooking(int bookingId);
}