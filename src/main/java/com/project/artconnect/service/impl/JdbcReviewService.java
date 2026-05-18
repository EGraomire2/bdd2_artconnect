package com.project.artconnect.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Review;
import com.project.artconnect.persistence.JdbcReviewDao;
import com.project.artconnect.service.ArtworkService;
import com.project.artconnect.service.CommunityService;
import com.project.artconnect.service.ReviewService;

public class JdbcReviewService implements ReviewService {
    private final JdbcReviewDao reviewDao;
    private final ArtworkService artworkService;
    private final CommunityService communityService;

    public JdbcReviewService(JdbcReviewDao reviewDao, ArtworkService artworkService,
            CommunityService communityService) {
        this.reviewDao = reviewDao;
        this.artworkService = artworkService;
        this.communityService = communityService;
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewDao.findAll().stream().map(this::enrichReview).collect(Collectors.toList());
    }

    @Override
    public List<Review> getReviewsByArtwork(Artwork artwork) {
        if (artwork == null || artwork.getId() == null) {
            return List.of();
        }
        return reviewDao.findByArtworkId(artwork.getId()).stream().map(this::enrichReview).collect(Collectors.toList());
    }

    @Override
    public List<Review> getReviewsByMember(CommunityMember member) {
        if (member == null || member.getId() == null) {
            return List.of();
        }
        return reviewDao.findByMemberId(member.getId()).stream().map(this::enrichReview).collect(Collectors.toList());
    }

    @Override
    public List<Review> searchReviews(String query, String artworkTitle, String reviewerName) {
        String q = query == null ? "" : query.toLowerCase();
        return getAllReviews().stream()
                .filter(review -> q.isEmpty() || matchesReview(review, q))
                .filter(review -> artworkTitle == null || artworkTitle.isBlank()
                        || (review.getArtwork() != null && review.getArtwork().getTitle() != null
                                && review.getArtwork().getTitle().equalsIgnoreCase(artworkTitle)))
                .filter(review -> reviewerName == null || reviewerName.isBlank()
                        || (review.getReviewer() != null && review.getReviewer().getName() != null
                                && review.getReviewer().getName().equalsIgnoreCase(reviewerName)))
                .collect(Collectors.toList());
    }

    @Override
    public void createReview(Review review) {
        normalizeReview(review);
        reviewDao.save(review);
    }

    @Override
    public void updateReview(Review review) {
        normalizeReview(review);
        reviewDao.update(review);
    }

    @Override
    public void deleteReview(int reviewId) {
        reviewDao.delete(reviewId);
    }

    private Review enrichReview(Review review) {
        if (review == null) {
            return null;
        }
        if (review.getArtwork() == null && review.getArtworkId() != null) {
            review.setArtwork(findArtworkById(review.getArtworkId()));
        }
        if (review.getReviewer() == null && review.getReviewerId() != null) {
            review.setReviewer(findMemberById(review.getReviewerId()));
        }
        return review;
    }

    private Artwork findArtworkById(Integer artworkId) {
        if (artworkId == null) {
            return null;
        }
        return artworkService.getAllArtworks().stream()
                .filter(artwork -> artworkId.equals(artwork.getId()))
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

    private void normalizeReview(Review review) {
        if (review == null) {
            return;
        }
        if (review.getArtworkId() == null && review.getArtwork() != null) {
            review.setArtworkId(review.getArtwork().getId());
        }
        if (review.getReviewerId() == null && review.getReviewer() != null) {
            review.setReviewerId(review.getReviewer().getId());
        }
    }

    private boolean matchesReview(Review review, String query) {
        return String.valueOf(review.getId()).contains(query)
                || String.valueOf(review.getRating()).contains(query)
                || (review.getComment() != null && review.getComment().toLowerCase().contains(query))
                || (review.getArtwork() != null && review.getArtwork().getTitle() != null
                        && review.getArtwork().getTitle().toLowerCase().contains(query))
                || (review.getReviewer() != null && review.getReviewer().getName() != null
                        && review.getReviewer().getName().toLowerCase().contains(query));
    }
}