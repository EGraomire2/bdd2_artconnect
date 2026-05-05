package com.project.artconnect.ui;

import com.project.artconnect.model.Gallery;
import com.project.artconnect.service.GalleryService;
import com.project.artconnect.util.ServiceProvider;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

public class GalleryController {
    @FXML
    private ListView<Gallery> galleryList;

    private final GalleryService galleryService = ServiceProvider.getGalleryService();

    @FXML
    public void initialize() {
        galleryList.setItems(FXCollections.observableArrayList(galleryService.getAllGalleries()));

        // Custom cell factory to show more info
        galleryList.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Gallery item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " - " + item.getAddress() + " (" + item.getRating() + "/5.0)");
                }
            }
        });
    }

    @FXML
    private void handleAddGallery() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Gallery");
        dialog.setHeaderText("Enter gallery name:");
        String name = dialog.showAndWait().orElse("");
        dialog.setHeaderText("Enter gallery address:");
        String address = dialog.showAndWait().orElse("");
        dialog.setHeaderText("Enter gallery rating (0-5):");
        double rating = Double.parseDouble(dialog.showAndWait().orElse("0"));
        Gallery newGallery = new Gallery(name, address, rating);
        galleryService.getAllGalleries(); // Call service to add - add method needed
        refreshList();
    }

    private void refreshList() {
        galleryList.setItems(FXCollections.observableArrayList(galleryService.getAllGalleries()));
        galleryList.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Gallery item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " - " + item.getAddress() + " (" + item.getRating() + "/5.0)");
                }
            }
        });
    }
}
