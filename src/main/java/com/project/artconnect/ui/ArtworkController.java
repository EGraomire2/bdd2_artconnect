package com.project.artconnect.ui;

import java.util.Locale;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.Artwork.Status;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.service.ArtworkService;
import com.project.artconnect.util.ServiceProvider;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ArtworkController {
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Artwork> artworkTable;
    @FXML
    private TableColumn<Artwork, String> titleColumn;
    @FXML
    private TableColumn<Artwork, String> typeColumn;
    @FXML
    private TableColumn<Artwork, Integer> creationYearColumn;
    @FXML
    private TableColumn<Artwork, String> mediumColumn;
    @FXML
    private TableColumn<Artwork, String> dimensionsColumn;
    @FXML
    private TableColumn<Artwork, Double> priceColumn;
    @FXML
    private TableColumn<Artwork, String> statusColumn;
    @FXML
    private TableColumn<Artwork, String> descriptionColumn;
    @FXML
    private TableColumn<Artwork, String> artistColumn;

    private final ArtworkService artworkService = ServiceProvider.getArtworkService();
    private final ArtistService artistService = ServiceProvider.getArtistService();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        creationYearColumn.setCellValueFactory(new PropertyValueFactory<>("creationYear"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        mediumColumn.setCellValueFactory(new PropertyValueFactory<>("medium"));
        dimensionsColumn.setCellValueFactory(new PropertyValueFactory<>("dimensions"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        artistColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getArtist() != null ? cellData.getValue().getArtist().getName() : "Unknown"));

        refreshTable();
    }

    @FXML
    private void handleAddArtwork() {
        String title = UiDialogUtils.promptText("Add Artwork", "Enter artwork title:", "");
        if (title == null) {
            return;
        }
        Integer creationYear = UiDialogUtils.promptInt("Add Artwork", "Enter artwork creation year:", 0);
        if (creationYear == null) {
            return;
        }
        String type = UiDialogUtils.promptText("Add Artwork", "Enter artwork type:", "");
        if (type == null) {
            return;
        }
        Double price = UiDialogUtils.promptDouble("Add Artwork", "Enter artwork price:", 0.0);
        if (price == null) {
            return;
        }
        String artistName = UiDialogUtils.promptText("Add Artwork", "Enter artist name:", "");
        if (artistName == null) {
            return;
        }
        String medium = UiDialogUtils.promptText("Add Artwork", "Enter artwork medium:", "");
        if (medium == null) {
            return;
        }
        String dimensions = UiDialogUtils.promptText("Add Artwork", "Enter artwork dimensions:", "");
        if (dimensions == null) {
            return;
        }
        String description = UiDialogUtils.promptText("Add Artwork", "Enter artwork description:", "");
        if (description == null) {
            return;
        }
        Artist artist = resolveArtist(artistName);
        if (artist == null) {
            return;
        }

        Artwork newArtwork = new Artwork(title, creationYear, type, medium, dimensions, description, price, artist);
        artworkService.createArtwork(newArtwork);
        refreshTable();
    }

    @FXML
    private void handleEditArtwork() {
        Artwork selected = artworkTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiDialogUtils.warn("Edit Artwork", "Please select an artwork to edit.");
            return;
        }

        Integer creationYear = UiDialogUtils.promptInt("Edit Artwork", "Enter artwork creation year:",
            selected.getCreationYear() != null ? selected.getCreationYear() : Integer.valueOf(0));
        if (creationYear == null) {
            return;
        }
        String type = UiDialogUtils.promptText("Edit Artwork", "Enter artwork type:", selected.getType());
        if (type == null) {
            return;
        }
        String medium = UiDialogUtils.promptText("Edit Artwork", "Enter artwork medium:", selected.getMedium());
        if (medium == null) {
            return;
        }
        String dimensions = UiDialogUtils.promptText("Edit Artwork", "Enter artwork dimensions:", selected.getDimensions());
        if (dimensions == null) {
            return;
        }
        String description = UiDialogUtils.promptText("Edit Artwork", "Enter artwork description:", selected.getDescription());
        if (description == null) {
            return;
        }
        Double price = UiDialogUtils.promptDouble("Edit Artwork", "Enter artwork price:", selected.getPrice());
        if (price == null) {
            return;
        }
        String statusValue = UiDialogUtils.promptText("Edit Artwork", "Enter artwork status (FOR_SALE, SOLD, EXHIBITED):",
                selected.getStatus() != null ? selected.getStatus().name() : Status.FOR_SALE.name());
        if (statusValue == null) {
            return;
        }

        Artwork updated = new Artwork(selected.getTitle(), creationYear, type, medium, dimensions, description, price, selected.getArtist());
        updated.setId(selected.getId());
        updated.setStatus(parseStatus(statusValue, selected.getStatus()));
        updated.setTags(selected.getTags());

        artworkService.updateArtwork(updated);
        refreshTable();
    }

    @FXML
    private void handleDeleteArtwork() {
        Artwork selected = artworkTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiDialogUtils.warn("Delete Artwork", "Please select an artwork to delete.");
            return;
        }
        if (!UiDialogUtils.confirm("Delete Artwork", "Delete selected artwork?")) {
            return;
        }
        artworkService.deleteArtwork(selected.getId());
        refreshTable();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        artworkTable.setItems(FXCollections.observableArrayList(artworkService.searchArtworks(query, null, null)));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        refreshTable();
    }

    private void refreshTable() {
        artworkTable.setItems(FXCollections.observableArrayList(artworkService.getAllArtworks()));
    }

    private Artist resolveArtist(String artistName) {
        if (artistName == null || artistName.isBlank()) {
            UiDialogUtils.warn("Add Artwork", "Artist name is required.");
            return null;
        }
        return artistService.getArtistByName(artistName.trim()).orElseGet(() -> {
            UiDialogUtils.warn("Add Artwork", "Artist not found: " + artistName);
            return null;
        });
    }

    private Status parseStatus(String value, Status fallback) {
        if (value == null || value.isBlank()) {
            return fallback == null ? Status.FOR_SALE : fallback;
        }
        try {
            return Status.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            return fallback == null ? Status.FOR_SALE : fallback;
        }
    }
}
