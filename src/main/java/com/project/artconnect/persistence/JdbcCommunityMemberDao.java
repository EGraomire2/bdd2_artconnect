package com.project.artconnect.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.project.artconnect.dao.CommunityMemberDao;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.util.ConnectionManager;

/**
 * JDBC implementation for CommunityMemberDao.
 * Uses PreparedStatement for secure SQL queries and try-with-resources for proper resource management.
 */
public class JdbcCommunityMemberDao implements CommunityMemberDao {

    /**
     * Maps a ResultSet row to a CommunityMember object.
     */
    private CommunityMember mapResultSetToCommunityMember(ResultSet rs) throws SQLException {
        CommunityMember member = new CommunityMember();
        member.setId(rs.getInt("member_id"));
        member.setName(rs.getString("name"));
        member.setEmail(rs.getString("email"));
        member.setBirthYear(rs.getObject("birth_year") != null ? rs.getInt("birth_year") : null);
        member.setPhone(rs.getString("phone"));
        member.setCity(rs.getString("city"));
        member.setMembershipType(rs.getString("membership_type"));
        return member;
    }

    @Override
    public List<CommunityMember> findAll() {
        List<CommunityMember> communityMembers = new ArrayList<>();
        String sql = "SELECT * FROM community_members";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                communityMembers.add(mapResultSetToCommunityMember(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all community members: " + e.getMessage());
            e.printStackTrace();
        }
        
        return communityMembers;
    }

    @Override
    public void save(CommunityMember communityMember) {
        String sql = "INSERT INTO community_members (name, email, birth_year, phone, city, membership_type) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, communityMember.getName());
            stmt.setString(2, communityMember.getEmail());
            if (communityMember.getBirthYear() != null) {
                stmt.setInt(3, communityMember.getBirthYear());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            stmt.setString(4, communityMember.getPhone());
            stmt.setString(5, communityMember.getCity());
            stmt.setString(6, communityMember.getMembershipType() != null ? communityMember.getMembershipType() : "free");
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Community member saved successfully: " + communityMember.getName());
            }
        } catch (SQLException e) {
            System.err.println("Error saving community member: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void update(CommunityMember communityMember) {
        String sql = "UPDATE community_members SET email = ?, birth_year = ?, phone = ?, city = ?, membership_type = ? "
                   + "WHERE name = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, communityMember.getEmail());
            if (communityMember.getBirthYear() != null) {
                stmt.setInt(2, communityMember.getBirthYear());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            stmt.setString(3, communityMember.getPhone());
            stmt.setString(4, communityMember.getCity());
            stmt.setString(5, communityMember.getMembershipType() != null ? communityMember.getMembershipType() : "free");
            stmt.setString(6, communityMember.getName());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Community member updated successfully: " + communityMember.getName());
            }
        } catch (SQLException e) {
            System.err.println("Error updating community member: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int memberId) {
        String sql = "DELETE FROM community_members WHERE member_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, memberId);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<CommunityMember> findByName(String name) {
        String sql = "SELECT * FROM community_members WHERE name = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCommunityMember(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching community member by name: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }
}