package com.project.artconnect.ui;

import java.time.LocalDateTime;

import com.project.artconnect.model.Workshop;
import com.project.artconnect.service.WorkshopService;
import com.project.artconnect.util.ServiceProvider;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

public class WorkshopController {
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
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Workshop");
        dialog.setHeaderText("Enter workshop title:");
        String title = dialog.showAndWait().orElse("");
        dialog.setHeaderText("Enter workshop price:");
        double price = Double.parseDouble(dialog.showAndWait().orElse("0"));
        dialog.setHeaderText("Enter workshop level (beginner/intermediate/advanced):");
        String level = dialog.showAndWait().orElse("beginner");
        Workshop newWorkshop = new Workshop(title, LocalDateTime.now(), null, price);
        newWorkshop.setLevel(level);
        workshopService.getAllWorkshops(); // Placeholder - service method needed
        refreshTable();
    }

    private void refreshTable() {
        workshopTable.setItems(FXCollections.observableArrayList(workshopService.getAllWorkshops()));
    }
}
