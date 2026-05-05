package com.project.artconnect.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.project.artconnect.dao.BookingDao;
import com.project.artconnect.model.Booking;
import com.project.artconnect.util.ConnectionManager;

public class JdbcBookingDao implements BookingDao {
    
    @Override
    public List<Booking> findAll() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bookings: " + e.getMessage());
            e.printStackTrace();
        }
        return bookings;
    }
    
    @Override
    public Booking save(Booking booking) {
        String sql = "INSERT INTO bookings (workshop_id, member_id, booking_date, payment_status) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, booking.getWorkshopId());
            stmt.setInt(2, booking.getMemberId());
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(booking.getBookingDate()));
            stmt.setString(4, booking.getPaymentStatus());
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving booking: " + e.getMessage());
            e.printStackTrace();
        }
        return booking;
    }
    
    @Override
    public Booking update(Booking booking) {
        String sql = "UPDATE bookings SET workshop_id = ?, member_id = ?, booking_date = ?, payment_status = ? " +
                     "WHERE booking_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, booking.getWorkshopId());
            stmt.setInt(2, booking.getMemberId());
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(booking.getBookingDate()));
            stmt.setString(4, booking.getPaymentStatus());
            stmt.setInt(5, booking.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating booking: " + e.getMessage());
            e.printStackTrace();
        }
        return booking;
    }
    
    @Override
    public void delete(int bookingId) {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookingId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting booking: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public List<Booking> findByWorkshopId(int workshopId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE workshop_id = ? ORDER BY booking_date DESC";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, workshopId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bookings by workshop: " + e.getMessage());
            e.printStackTrace();
        }
        return bookings;
    }
    
    @Override
    public List<Booking> findByMemberId(int memberId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE member_id = ? ORDER BY booking_date DESC";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bookings by member: " + e.getMessage());
            e.printStackTrace();
        }
        return bookings;
    }
    
    @Override
    public boolean existsBooking(int workshopId, int memberId) {
        String sql = "SELECT COUNT(*) FROM bookings WHERE workshop_id = ? AND member_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, workshopId);
            stmt.setInt(2, memberId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking booking existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("booking_id"));
        booking.setWorkshopId(rs.getInt("workshop_id"));
        booking.setMemberId(rs.getInt("member_id"));
        booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
        booking.setPaymentStatus(rs.getString("payment_status"));
        return booking;
    }
}
