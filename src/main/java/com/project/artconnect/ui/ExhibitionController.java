package com.project.artconnect.ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.service.GalleryService;
import com.project.artconnect.util.ServiceProvider;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

public class ExhibitionController {
    @FXML
    private TableView<Exhibition> exhibitionTable;
    @FXML
    private TableColumn<Exhibition, String> titleColumn;
    @FXML
    private TableColumn<Exhibition, LocalDate> dateColumn;
    @FXML
    private TableColumn<Exhibition, String> themeColumn;
    @FXML
    private TableColumn<Exhibition, String> galleryColumn;

    private final GalleryService galleryService = ServiceProvider.getGalleryService();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        themeColumn.setCellValueFactory(new PropertyValueFactory<>("theme"));

        galleryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getGallery() != null ? cellData.getValue().getGallery().getName() : "Unknown"));

        refreshData();
    }

    @FXML
    private void handleAddExhibition() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Exhibition");
        dialog.setHeaderText("Enter exhibition title:");
        String title = dialog.showAndWait().orElse("");
        dialog.setHeaderText("Enter exhibition theme:");
        String theme = dialog.showAndWait().orElse("");
        dialog.setHeaderText("Enter curator name:");
        String curatorName = dialog.showAndWait().orElse("");
        Exhibition newExhibition = new Exhibition(title, LocalDate.now(), LocalDate.now().plusMonths(1), null);
        newExhibition.setTheme(theme);
        newExhibition.setCuratorName(curatorName);
        refreshData();
    }

    private void refreshData() {
        List<Exhibition> all = new ArrayList<>();
        for (Gallery g : galleryService.getAllGalleries()) {
            all.addAll(galleryService.getExhibitionsByGallery(g));
        }
        exhibitionTable.setItems(FXCollections.observableArrayList(all));
    }
}
