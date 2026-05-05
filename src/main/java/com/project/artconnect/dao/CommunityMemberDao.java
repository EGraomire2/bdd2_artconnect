package com.project.artconnect.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.project.artconnect.model.CommunityMember;

public interface CommunityMemberDao {
    Optional<CommunityMember> findByName(String name);

    List<CommunityMember> findAll();

    void save(CommunityMember member);

    void update(CommunityMember member);

    void delete(int memberId) throws SQLException;
}
