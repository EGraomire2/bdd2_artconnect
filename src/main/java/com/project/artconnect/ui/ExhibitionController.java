package com.project.artconnect.ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.persistence.JdbcExhibitionDao;
import com.project.artconnect.service.GalleryService;
import com.project.artconnect.util.ServiceProvider;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ExhibitionController {
    @FXML
    private TextField searchField;
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
    private final JdbcExhibitionDao exhibitionDao = new JdbcExhibitionDao();

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
        String title = UiDialogUtils.promptText("Add Exhibition", "Enter exhibition title:", "");
        if (title == null) {
            return;
        }
        LocalDate startDate = parseDate(UiDialogUtils.promptText("Add Exhibition", "Enter start date (YYYY-MM-DD):",
                LocalDate.now().toString()), LocalDate.now());
        if (startDate == null) {
            return;
        }
        LocalDate endDate = parseDate(UiDialogUtils.promptText("Add Exhibition", "Enter end date (YYYY-MM-DD):",
                LocalDate.now().plusMonths(1).toString()), LocalDate.now().plusMonths(1));
        if (endDate == null) {
            return;
        }
        String description = UiDialogUtils.promptText("Add Exhibition", "Enter exhibition description:", "");
        if (description == null) {
            return;
        }
        String curatorName = UiDialogUtils.promptText("Add Exhibition", "Enter curator name:", "");
        if (curatorName == null) {
            return;
        }
        String theme = UiDialogUtils.promptText("Add Exhibition", "Enter exhibition theme:", "");
        if (theme == null) {
            return;
        }
        String galleryName = UiDialogUtils.promptText("Add Exhibition", "Enter gallery name:", "");
        if (galleryName == null) {
            return;
        }

        Gallery gallery = resolveGallery(galleryName);
        if (gallery == null) {
            return;
        }

        Exhibition newExhibition = new Exhibition(title, startDate, endDate, gallery);
        newExhibition.setDescription(description);
        newExhibition.setCuratorName(curatorName);
        newExhibition.setTheme(theme);
        exhibitionDao.save(newExhibition);
        refreshData();
    }

    @FXML
    private void handleEditExhibition() {
        Exhibition selected = exhibitionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiDialogUtils.warn("Edit Exhibition", "Please select an exhibition to edit.");
            return;
        }

        LocalDate startDate = parseDate(UiDialogUtils.promptText("Edit Exhibition", "Enter start date (YYYY-MM-DD):",
                selected.getStartDate() != null ? selected.getStartDate().toString() : LocalDate.now().toString()),
                selected.getStartDate());
        if (startDate == null) {
            return;
        }
        LocalDate endDate = parseDate(UiDialogUtils.promptText("Edit Exhibition", "Enter end date (YYYY-MM-DD):",
                selected.getEndDate() != null ? selected.getEndDate().toString() : LocalDate.now().plusMonths(1).toString()),
                selected.getEndDate());
        if (endDate == null) {
            return;
        }
        String description = UiDialogUtils.promptText("Edit Exhibition", "Enter exhibition description:", selected.getDescription());
        if (description == null) {
            return;
        }
        String curatorName = UiDialogUtils.promptText("Edit Exhibition", "Enter curator name:", selected.getCuratorName());
        if (curatorName == null) {
            return;
        }
        String theme = UiDialogUtils.promptText("Edit Exhibition", "Enter exhibition theme:", selected.getTheme());
        if (theme == null) {
            return;
        }

        Exhibition updated = new Exhibition(selected.getTitle(), startDate, endDate, selected.getGallery());
        updated.setId(selected.getId());
        updated.setDescription(description);
        updated.setCuratorName(curatorName);
        updated.setTheme(theme);

        exhibitionDao.update(updated);
        refreshData();
    }

    @FXML
    private void handleDeleteExhibition() {
        Exhibition selected = exhibitionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiDialogUtils.warn("Delete Exhibition", "Please select an exhibition to delete.");
            return;
        }
        if (!UiDialogUtils.confirm("Delete Exhibition", "Delete selected exhibition?")) {
            return;
        }
        exhibitionDao.delete(selected.getId());
        refreshData();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        exhibitionTable.setItems(FXCollections.observableArrayList(
                loadExhibitions().stream()
                        .filter(e -> query == null || query.isBlank()
                                || (e.getTitle() != null && e.getTitle().toLowerCase().contains(query.toLowerCase()))
                                || (e.getTheme() != null && e.getTheme().toLowerCase().contains(query.toLowerCase()))
                                || (e.getCuratorName() != null && e.getCuratorName().toLowerCase().contains(query.toLowerCase()))
                                || (e.getGallery() != null && e.getGallery().getName() != null
                                        && e.getGallery().getName().toLowerCase().contains(query.toLowerCase())))
                        .collect(Collectors.toList())));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        refreshData();
    }

    private void refreshData() {
        exhibitionTable.setItems(FXCollections.observableArrayList(loadExhibitions()));
    }

    private List<Exhibition> loadExhibitions() {
        List<Exhibition> all = new ArrayList<>();
        for (Gallery gallery : galleryService.getAllGalleries()) {
            all.addAll(galleryService.getExhibitionsByGallery(gallery));
        }
        return all;
    }

    private Gallery resolveGallery(String galleryName) {
        if (galleryName == null || galleryName.isBlank()) {
            UiDialogUtils.warn("Add Exhibition", "Gallery name is required.");
            return null;
        }
        return galleryService.getGalleryByName(galleryName.trim()).orElseGet(() -> {
            UiDialogUtils.warn("Add Exhibition", "Gallery not found: " + galleryName);
            return null;
        });
    }

    private LocalDate parseDate(String value, LocalDate fallback) {
        if (value == null) {
            return null;
        }
        try {
            return LocalDate.parse(value);
        } catch (Exception exception) {
            return fallback;
        }
    }
}
