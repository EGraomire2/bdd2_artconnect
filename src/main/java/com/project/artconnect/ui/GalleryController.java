package com.project.artconnect.ui;

import com.project.artconnect.model.Gallery;
import com.project.artconnect.service.GalleryService;
import com.project.artconnect.util.ServiceProvider;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class GalleryController {
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Gallery> galleryTable;
    @FXML
    private TableColumn<Gallery, String> nameColumn;
    @FXML
    private TableColumn<Gallery, String> addressColumn;
    @FXML
    private TableColumn<Gallery, String> ownerNameColumn;
    @FXML
    private TableColumn<Gallery, String> openingHoursColumn;
    @FXML
    private TableColumn<Gallery, String> contactPhoneColumn;
    @FXML
    private TableColumn<Gallery, Double> ratingColumn;
    @FXML
    private TableColumn<Gallery, String> websiteColumn;

    private final GalleryService galleryService = ServiceProvider.getGalleryService();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        ownerNameColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        openingHoursColumn.setCellValueFactory(new PropertyValueFactory<>("openingHours"));
        contactPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("contactPhone"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        websiteColumn.setCellValueFactory(new PropertyValueFactory<>("website"));

        refreshTable();
    }

    @FXML
    private void handleAddGallery() {
        String name = UiDialogUtils.promptText("Add Gallery", "Enter gallery name:", "");
        if (name == null) {
            return;
        }
        String address = UiDialogUtils.promptText("Add Gallery", "Enter gallery address:", "");
        if (address == null) {
            return;
        }
        Double rating = UiDialogUtils.promptDouble("Add Gallery", "Enter gallery rating (0-5):", 0.0);
        if (rating == null) {
            return;
        }
        String ownerName = UiDialogUtils.promptText("Add Gallery", "Enter owner name:", "");
        if (ownerName == null) {
            return;
        }
        String openingHours = UiDialogUtils.promptText("Add Gallery", "Enter opening hours:", "");
        if (openingHours == null) {
            return;
        }
        String contactPhone = UiDialogUtils.promptText("Add Gallery", "Enter contact phone:", "");
        if (contactPhone == null) {
            return;
        }
        String website = UiDialogUtils.promptText("Add Gallery", "Enter website:", "");
        if (website == null) {
            return;
        }

        Gallery newGallery = new Gallery(name, address, rating);
        newGallery.setOwnerName(ownerName);
        newGallery.setOpeningHours(openingHours);
        newGallery.setContactPhone(contactPhone);
        newGallery.setWebsite(website);
        galleryService.createGallery(newGallery);
        refreshTable();
    }

    @FXML
    private void handleEditGallery() {
        Gallery selected = galleryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiDialogUtils.warn("Edit Gallery", "Please select a gallery to edit.");
            return;
        }

        String address = UiDialogUtils.promptText("Edit Gallery", "Enter gallery address:", selected.getAddress());
        if (address == null) {
            return;
        }
        String ownerName = UiDialogUtils.promptText("Edit Gallery", "Enter owner name:", selected.getOwnerName());
        if (ownerName == null) {
            return;
        }
        String openingHours = UiDialogUtils.promptText("Edit Gallery", "Enter opening hours:", selected.getOpeningHours());
        if (openingHours == null) {
            return;
        }
        String contactPhone = UiDialogUtils.promptText("Edit Gallery", "Enter contact phone:", selected.getContactPhone());
        if (contactPhone == null) {
            return;
        }
        Double rating = UiDialogUtils.promptDouble("Edit Gallery", "Enter gallery rating (0-5):", selected.getRating());
        if (rating == null) {
            return;
        }
        String website = UiDialogUtils.promptText("Edit Gallery", "Enter website:", selected.getWebsite());
        if (website == null) {
            return;
        }

        Gallery updated = new Gallery(selected.getName(), address, rating);
        updated.setId(selected.getId());
        updated.setOwnerName(ownerName);
        updated.setOpeningHours(openingHours);
        updated.setContactPhone(contactPhone);
        updated.setWebsite(website);
        updated.setExhibitions(selected.getExhibitions());

        galleryService.updateGallery(updated);
        refreshTable();
    }

    @FXML
    private void handleDeleteGallery() {
        Gallery selected = galleryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiDialogUtils.warn("Delete Gallery", "Please select a gallery to delete.");
            return;
        }
        if (!UiDialogUtils.confirm("Delete Gallery", "Delete selected gallery?")) {
            return;
        }
        galleryService.deleteGallery(selected.getId());
        refreshTable();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        galleryTable.setItems(FXCollections.observableArrayList(galleryService.searchGalleries(query, null, null)));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        refreshTable();
    }

    private void refreshTable() {
        galleryTable.setItems(FXCollections.observableArrayList(galleryService.getAllGalleries()));
    }
}
