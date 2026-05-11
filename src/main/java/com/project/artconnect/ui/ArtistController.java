package com.project.artconnect.ui;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.util.ServiceProvider;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ArtistController {
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<Discipline> disciplineFilter;
    @FXML
    private TableView<Artist> artistTable;
    @FXML
    private TableColumn<Artist, String> nameColumn;
    @FXML
    private TableColumn<Artist, String> cityColumn;
    @FXML
    private TableColumn<Artist, String> emailColumn;
    @FXML
    private TableColumn<Artist, Integer> yearColumn;

    private final ArtistService artistService = ServiceProvider.getArtistService();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("contactEmail"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("birthYear"));

        disciplineFilter.setItems(FXCollections.observableArrayList(artistService.getAllDisciplines()));
        refreshTable();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        Discipline d = disciplineFilter.getValue();
        String dName = (d != null) ? d.getName() : null;
        artistTable.setItems(FXCollections.observableArrayList(artistService.searchArtists(query, dName, null)));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        disciplineFilter.setValue(null);
        refreshTable();
    }

    @FXML
    private void handleAddArtist() {
        String name = UiDialogUtils.promptText("Add New Artist", "Enter artist name:", "");
        if (name == null) {
            return;
        }
        String bio = UiDialogUtils.promptText("Add New Artist", "Enter artist bio:", "");
        if (bio == null) {
            return;
        }
        Integer birthYear = UiDialogUtils.promptInt("Add New Artist", "Enter artist birth year:", 0);
        if (birthYear == null) {
            return;
        }
        String email = UiDialogUtils.promptText("Add New Artist", "Enter artist contact email:", "");
        if (email == null) {
            return;
        }
        String city = UiDialogUtils.promptText("Add New Artist", "Enter artist city:", "");
        if (city == null) {
            return;
        }
        Artist newArtist = new Artist(name, bio, birthYear, email, city);
        artistService.createArtist(newArtist);
        refreshTable();
    }

    @FXML
    private void handleEditArtist() {
        Artist selected = artistTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Edit Artist", "Please select an artist to edit.");
            return;
        }

        String bio = UiDialogUtils.promptText("Edit Artist", "Enter artist bio:", selected.getBio());
        if (bio == null) {
            return;
        }
        Integer birthYear = UiDialogUtils.promptInt("Edit Artist", "Enter artist birth year:",
                selected.getBirthYear() != null ? selected.getBirthYear() : 0);
        if (birthYear == null) {
            return;
        }
        String email = UiDialogUtils.promptText("Edit Artist", "Enter artist contact email:", selected.getContactEmail());
        if (email == null) {
            return;
        }
        String city = UiDialogUtils.promptText("Edit Artist", "Enter artist city:", selected.getCity());
        if (city == null) {
            return;
        }
        String phone = UiDialogUtils.promptText("Edit Artist", "Enter artist phone:", selected.getPhone());
        if (phone == null) {
            return;
        }
        String website = UiDialogUtils.promptText("Edit Artist", "Enter artist website:", selected.getWebsite());
        if (website == null) {
            return;
        }
        String socialMedia = UiDialogUtils.promptText("Edit Artist", "Enter artist social media:", selected.getSocialMedia());
        if (socialMedia == null) {
            return;
        }

        Artist updated = new Artist(selected.getId(), selected.getName(), bio, birthYear, email, city, phone, website,
                socialMedia);
        updated.setActive(selected.isActive());
        updated.setDisciplines(selected.getDisciplines());
        updated.setArtworks(selected.getArtworks());
        artistService.updateArtist(updated);
        refreshTable();
    }

    @FXML
    private void handleDeleteArtist() {
        Artist selected = artistTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Delete Artist", "Please select an artist to delete.");
            return;
        }
        artistService.deleteArtist(selected.getId());
        refreshTable();
    }

    private void refreshTable() {
        artistTable.setItems(FXCollections.observableArrayList(artistService.getAllArtists()));
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
