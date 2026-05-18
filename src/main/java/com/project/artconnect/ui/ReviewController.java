package com.project.artconnect.ui;

import java.time.LocalDate;

import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Review;
import com.project.artconnect.service.ArtworkService;
import com.project.artconnect.service.CommunityService;
import com.project.artconnect.service.ReviewService;
import com.project.artconnect.util.ServiceProvider;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ReviewController {
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Review> reviewTable;
    @FXML
    private TableColumn<Review, Integer> idColumn;
    @FXML
    private TableColumn<Review, String> artworkColumn;
    @FXML
    private TableColumn<Review, String> reviewerColumn;
    @FXML
    private TableColumn<Review, Integer> ratingColumn;
    @FXML
    private TableColumn<Review, String> commentColumn;
    @FXML
    private TableColumn<Review, LocalDate> dateColumn;

    private final ReviewService reviewService = ServiceProvider.getReviewService();
    private final ArtworkService artworkService = ServiceProvider.getArtworkService();
    private final CommunityService communityService = ServiceProvider.getCommunityService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        artworkColumn.setCellValueFactory(cellData -> new SimpleStringProperty(resolveArtworkLabel(cellData.getValue())));
        reviewerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(resolveReviewerLabel(cellData.getValue())));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("reviewDate"));

        refreshTable();
    }

    @FXML
    private void handleAddReview() {
        Review review = promptReview(null);
        if (review == null) {
            return;
        }
        reviewService.createReview(review);
        refreshTable();
    }

    @FXML
    private void handleEditReview() {
        Review selected = reviewTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiDialogUtils.warn("Edit Review", "Please select a review to edit.");
            return;
        }

        Review updated = promptReview(selected);
        if (updated == null) {
            return;
        }
        updated.setId(selected.getId());
        reviewService.updateReview(updated);
        refreshTable();
    }

    @FXML
    private void handleDeleteReview() {
        Review selected = reviewTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiDialogUtils.warn("Delete Review", "Please select a review to delete.");
            return;
        }
        if (!UiDialogUtils.confirm("Delete Review", "Delete selected review?")) {
            return;
        }
        reviewService.deleteReview(selected.getId());
        refreshTable();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        reviewTable.setItems(FXCollections.observableArrayList(reviewService.searchReviews(query, null, null)));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        refreshTable();
    }

    private void refreshTable() {
        reviewTable.setItems(FXCollections.observableArrayList(reviewService.getAllReviews()));
    }

    private Review promptReview(Review seed) {
        String currentArtwork = seed != null ? resolveArtworkLabel(seed) : "";
        String currentReviewer = seed != null ? resolveReviewerLabel(seed) : "";
        int currentRating = seed != null ? seed.getRating() : 5;
        String currentComment = seed != null && seed.getComment() != null ? seed.getComment() : "";
        String currentDate = seed != null && seed.getReviewDate() != null ? seed.getReviewDate().toString() : LocalDate.now().toString();

        String artworkTitle = UiDialogUtils.promptText(seed == null ? "Add Review" : "Edit Review",
                "Enter artwork title:", currentArtwork);
        if (artworkTitle == null) {
            return null;
        }
        Artwork artwork = resolveArtwork(artworkTitle);
        if (artwork == null) {
            return null;
        }

        String reviewerName = UiDialogUtils.promptText(seed == null ? "Add Review" : "Edit Review",
                "Enter reviewer name:", currentReviewer);
        if (reviewerName == null) {
            return null;
        }
        CommunityMember reviewer = resolveReviewer(reviewerName);
        if (reviewer == null) {
            return null;
        }

        Integer rating = UiDialogUtils.promptInt(seed == null ? "Add Review" : "Edit Review",
                "Enter rating (1-5):", currentRating);
        if (rating == null) {
            return null;
        }

        String comment = UiDialogUtils.promptText(seed == null ? "Add Review" : "Edit Review",
                "Enter review comment:", currentComment);
        if (comment == null) {
            return null;
        }

        String dateValue = UiDialogUtils.promptText(seed == null ? "Add Review" : "Edit Review",
                "Enter review date (YYYY-MM-DD):", currentDate);
        if (dateValue == null) {
            return null;
        }

        Review review = new Review();
        review.setArtwork(artwork);
        review.setArtworkId(artwork.getId());
        review.setReviewer(reviewer);
        review.setReviewerId(reviewer.getId());
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewDate(parseDate(dateValue, seed != null ? seed.getReviewDate() : LocalDate.now()));
        return review;
    }

    private Artwork resolveArtwork(String artworkTitle) {
        if (artworkTitle == null || artworkTitle.isBlank()) {
            UiDialogUtils.warn("Review", "Artwork title is required.");
            return null;
        }
        return artworkService.getArtworkByTitle(artworkTitle.trim()).orElseGet(() -> {
            UiDialogUtils.warn("Review", "Artwork not found: " + artworkTitle);
            return null;
        });
    }

    private CommunityMember resolveReviewer(String reviewerName) {
        if (reviewerName == null || reviewerName.isBlank()) {
            UiDialogUtils.warn("Review", "Reviewer name is required.");
            return null;
        }
        return communityService.getMemberByName(reviewerName.trim()).orElseGet(() -> {
            UiDialogUtils.warn("Review", "Member not found: " + reviewerName);
            return null;
        });
    }

    private String resolveArtworkLabel(Review review) {
        if (review == null) {
            return "";
        }
        if (review.getArtwork() != null && review.getArtwork().getTitle() != null) {
            return review.getArtwork().getTitle();
        }
        return review.getArtworkId() != null ? "Artwork #" + review.getArtworkId() : "Unknown";
    }

    private String resolveReviewerLabel(Review review) {
        if (review == null) {
            return "";
        }
        if (review.getReviewer() != null && review.getReviewer().getName() != null) {
            return review.getReviewer().getName();
        }
        return review.getReviewerId() != null ? "Member #" + review.getReviewerId() : "Unknown";
    }

    private LocalDate parseDate(String value, LocalDate fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        try {
            return LocalDate.parse(value.trim());
        } catch (Exception exception) {
            return fallback;
        }
    }
}