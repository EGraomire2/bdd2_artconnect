package com.project.artconnect.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Review;
import com.project.artconnect.persistence.JdbcCommunityMemberDao;
import com.project.artconnect.persistence.JdbcReviewDao;
import com.project.artconnect.service.CommunityService;

/**
 * Community Service implementation using JDBC DAO for database access.
 */
public class JdbcCommunityService implements CommunityService {
    private final JdbcCommunityMemberDao communityMemberDao;
    private final JdbcReviewDao reviewDao;

    public JdbcCommunityService(JdbcCommunityMemberDao communityMemberDao, JdbcReviewDao reviewDao) {
        this.communityMemberDao = communityMemberDao;
        this.reviewDao = reviewDao;
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
        if (member == null || member.getId() == null) {
            return Collections.emptyList();
        }
        return reviewDao.findByMemberId(member.getId());
    }

    @Override
    public List<CommunityMember> searchMembers(String query, String city, String email) {
        String q = (query == null) ? "" : query.toLowerCase();
        return communityMemberDao.findAll().stream()
                .filter(m -> q.isEmpty()
                        || (m.getName() != null && m.getName().toLowerCase().contains(q))
                        || (m.getEmail() != null && m.getEmail().toLowerCase().contains(q)))
                .filter(m -> city == null || city.isEmpty() || (m.getCity() != null && m.getCity().equalsIgnoreCase(city)))
                .filter(m -> email == null || email.isEmpty() || (m.getEmail() != null && m.getEmail().equalsIgnoreCase(email)))
                .collect(Collectors.toList());
    }

    @Override
    public void createMember(CommunityMember member) {
        communityMemberDao.save(member);
    }

    @Override
    public void updateMember(CommunityMember member) {
        communityMemberDao.update(member);
    }

    @Override
    public void deleteMember(int memberId) {
        communityMemberDao.delete(memberId);
    }
}
