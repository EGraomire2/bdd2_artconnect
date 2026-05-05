package com.project.artconnect.service.impl;

import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Review;
import com.project.artconnect.service.CommunityService;
import com.project.artconnect.persistence.JdbcCommunityMemberDao;
import java.util.*;

/**
 * Community Service implementation using JDBC DAO for database access.
 */
public class JdbcCommunityService implements CommunityService {
    private final JdbcCommunityMemberDao communityMemberDao;

    public JdbcCommunityService(JdbcCommunityMemberDao communityMemberDao) {
        this.communityMemberDao = communityMemberDao;
    }

    @Override
    public List<CommunityMember> getAllMembers() {
        return communityMemberDao.findAll();
    }

    @Override
    public Optional<CommunityMember> getMemberByName(String name) {
        return communityMemberDao.findAll().stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public List<Review> getReviewsByMember(CommunityMember member) {
        if (member == null || member.getReviews() == null) {
            return Collections.emptyList();
        }
        return member.getReviews();
    }
}
