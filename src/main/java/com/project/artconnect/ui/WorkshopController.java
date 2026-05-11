package com.project.artconnect.ui;

import java.time.LocalDateTime;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.service.WorkshopService;
import com.project.artconnect.util.ServiceProvider;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class WorkshopController {
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Workshop> workshopTable;
    @FXML
    private TableColumn<Workshop, String> titleColumn;
    @FXML
    private TableColumn<Workshop, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<Workshop, String> instructorColumn;
    @FXML
    private TableColumn<Workshop, Double> priceColumn;
    @FXML
    private TableColumn<Workshop, String> levelColumn;

    private final WorkshopService workshopService = ServiceProvider.getWorkshopService();
    private final ArtistService artistService = ServiceProvider.getArtistService();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));

        instructorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getInstructor() != null ? cellData.getValue().getInstructor().getName()
                        : "Unknown"));

        refreshTable();
    }

    @FXML
    private void handleAddWorkshop() {
        String title = UiDialogUtils.promptText("Add Workshop", "Enter workshop title:", "");
        if (title == null) {
            return;
        }
        String dateValue = UiDialogUtils.promptText("Add Workshop", "Enter workshop date (ISO format):", LocalDateTime.now().toString());
        if (dateValue == null) {
            return;
        }
        Integer durationMinutes = UiDialogUtils.promptInt("Add Workshop", "Enter duration in minutes:", 0);
        if (durationMinutes == null) {
            return;
        }
        Integer maxParticipants = UiDialogUtils.promptInt("Add Workshop", "Enter max participants:", 0);
        if (maxParticipants == null) {
            return;
        }
        Double price = UiDialogUtils.promptDouble("Add Workshop", "Enter workshop price:", 0.0);
        if (price == null) {
            return;
        }
        String instructorName = UiDialogUtils.promptText("Add Workshop", "Enter instructor name:", "");
        if (instructorName == null) {
            return;
        }
        String location = UiDialogUtils.promptText("Add Workshop", "Enter workshop location:", "");
        if (location == null) {
            return;
        }
        String description = UiDialogUtils.promptText("Add Workshop", "Enter workshop description:", "");
        if (description == null) {
            return;
        }
        String level = UiDialogUtils.promptText("Add Workshop", "Enter workshop level:", "beginner");
        if (level == null) {
            return;
        }

        Artist instructor = resolveInstructor(instructorName);
        if (instructor == null) {
            return;
        }

        Workshop newWorkshop = new Workshop(title, parseDateTime(dateValue), instructor, price);
        newWorkshop.setDurationMinutes(durationMinutes);
        newWorkshop.setMaxParticipants(maxParticipants);
        newWorkshop.setLocation(location);
        newWorkshop.setDescription(description);
        newWorkshop.setLevel(level);
        workshopService.createWorkshop(newWorkshop);
        refreshTable();
    }

    @FXML
    private void handleEditWorkshop() {
        Workshop selected = workshopTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiDialogUtils.warn("Edit Workshop", "Please select a workshop to edit.");
            return;
        }

        String dateValue = UiDialogUtils.promptText("Edit Workshop", "Enter workshop date (ISO format):",
                selected.getDate() != null ? selected.getDate().toString() : LocalDateTime.now().toString());
        if (dateValue == null) {
            return;
        }
        Integer durationMinutes = UiDialogUtils.promptInt("Edit Workshop", "Enter duration in minutes:", selected.getDurationMinutes());
        if (durationMinutes == null) {
            return;
        }
        Integer maxParticipants = UiDialogUtils.promptInt("Edit Workshop", "Enter max participants:", selected.getMaxParticipants());
        if (maxParticipants == null) {
            return;
        }
        Double price = UiDialogUtils.promptDouble("Edit Workshop", "Enter workshop price:", selected.getPrice());
        if (price == null) {
            return;
        }
        String location = UiDialogUtils.promptText("Edit Workshop", "Enter workshop location:", selected.getLocation());
        if (location == null) {
            return;
        }
        String description = UiDialogUtils.promptText("Edit Workshop", "Enter workshop description:", selected.getDescription());
        if (description == null) {
            return;
        }
        String level = UiDialogUtils.promptText("Edit Workshop", "Enter workshop level:", selected.getLevel());
        if (level == null) {
            return;
        }

        Workshop updated = new Workshop(selected.getTitle(), parseDateTime(dateValue), selected.getInstructor(), price);
        updated.setId(selected.getId());
        updated.setDurationMinutes(durationMinutes);
        updated.setMaxParticipants(maxParticipants);
        updated.setLocation(location);
        updated.setDescription(description);
        updated.setLevel(level);

        workshopService.updateWorkshop(updated);
        refreshTable();
    }

    @FXML
    private void handleDeleteWorkshop() {
        Workshop selected = workshopTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiDialogUtils.warn("Delete Workshop", "Please select a workshop to delete.");
            return;
        }
        if (!UiDialogUtils.confirm("Delete Workshop", "Delete selected workshop?")) {
            return;
        }
        workshopService.deleteWorkshop(selected.getId());
        refreshTable();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        workshopTable.setItems(FXCollections.observableArrayList(workshopService.searchWorkshops(query, null, null)));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        refreshTable();
    }

    private void refreshTable() {
        workshopTable.setItems(FXCollections.observableArrayList(workshopService.getAllWorkshops()));
    }

    private Artist resolveInstructor(String instructorName) {
        if (instructorName == null || instructorName.isBlank()) {
            UiDialogUtils.warn("Add Workshop", "Instructor name is required.");
            return null;
        }
        return artistService.getArtistByName(instructorName.trim()).orElseGet(() -> {
            UiDialogUtils.warn("Add Workshop", "Instructor not found: " + instructorName);
            return null;
        });
    }

    private LocalDateTime parseDateTime(String value) {
        try {
            return LocalDateTime.parse(value);
        } catch (Exception exception) {
            return LocalDateTime.now();
        }
    }
}
