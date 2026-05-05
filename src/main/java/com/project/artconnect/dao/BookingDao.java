package com.project.artconnect.dao;

import com.project.artconnect.model.Booking;
import java.sql.SQLException;
import java.util.List;

public interface BookingDao {
    List<Booking> findAll();
    Booking save(Booking booking) throws SQLException;
    Booking update(Booking booking) throws SQLException;
    void delete(int bookingId) throws SQLException;
    List<Booking> findByWorkshopId(int workshopId);
    List<Booking> findByMemberId(int memberId);
    boolean existsBooking(int workshopId, int memberId);
}
