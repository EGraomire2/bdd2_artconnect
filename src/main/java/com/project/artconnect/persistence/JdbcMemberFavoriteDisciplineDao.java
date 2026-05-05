package com.project.artconnect.persistence;

import com.project.artconnect.dao.MemberFavoriteDisciplineDao;
import com.project.artconnect.util.ConnectionManager;
import java.sql.*;
import java.util.*;

public class JdbcMemberFavoriteDisciplineDao implements MemberFavoriteDisciplineDao {
    
    @Override
    public void addFavoriteDiscipline(int memberId, int disciplineId) throws SQLException {
        if (exists(memberId, disciplineId)) {
            throw new SQLException("Cette discipline favorite existe déjà pour ce membre");
        }
        
        String sql = "INSERT INTO member_favorite_disciplines (member_id, discipline_id) VALUES (?, ?)";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, memberId);
            stmt.setInt(2, disciplineId);
            stmt.executeUpdate();
        }
    }
    
    @Override
    public void removeFavoriteDiscipline(int memberId, int disciplineId) throws SQLException {
        String sql = "DELETE FROM member_favorite_disciplines WHERE member_id = ? AND discipline_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, memberId);
            stmt.setInt(2, disciplineId);
            stmt.executeUpdate();
        }
    }
    
    @Override
    public List<Integer> getFavoriteDisciplineIdsByMemberId(int memberId) {
        List<Integer> disciplineIds = new ArrayList<>();
        String sql = "SELECT discipline_id FROM member_favorite_disciplines WHERE member_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    disciplineIds.add(rs.getInt("discipline_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching favorite disciplines by member: " + e.getMessage());
            e.printStackTrace();
        }
        return disciplineIds;
    }
    
    @Override
    public List<Integer> getMemberIdsByDisciplineId(int disciplineId) {
        List<Integer> memberIds = new ArrayList<>();
        String sql = "SELECT member_id FROM member_favorite_disciplines WHERE discipline_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, disciplineId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    memberIds.add(rs.getInt("member_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching members by favorite discipline: " + e.getMessage());
            e.printStackTrace();
        }
        return memberIds;
    }
    
    @Override
    public boolean exists(int memberId, int disciplineId) {
        String sql = "SELECT COUNT(*) FROM member_favorite_disciplines WHERE member_id = ? AND discipline_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, memberId);
            stmt.setInt(2, disciplineId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking favorite discipline existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
