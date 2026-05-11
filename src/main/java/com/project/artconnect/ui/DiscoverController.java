package com.project.artconnect.ui;

import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.service.GalleryService;
import com.project.artconnect.service.WorkshopService;
import com.project.artconnect.util.ServiceProvider;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class DiscoverController {
    @FXML
    private FlowPane discoverPane;

    private final GalleryService galleryService = ServiceProvider.getGalleryService();
    private final WorkshopService workshopService = ServiceProvider.getWorkshopService();

    @FXML
    public void initialize() {
        List<Exhibition> featuredExhibitions = new ArrayList<>();
        for (Gallery gallery : galleryService.getAllGalleries()) {
            featuredExhibitions.addAll(gallery.getExhibitions());
            if (featuredExhibitions.size() >= 3) {
                break;
            }
        }

        featuredExhibitions.stream().limit(3).forEach(this::addExhibitionCard);
        workshopService.getAllWorkshops().stream().limit(3).forEach(this::addWorkshopCard);
    }

    private void addExhibitionCard(Exhibition exhibition) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: #e3f2fd; -fx-border-color: #2196f3; -fx-border-radius: 5; -fx-background-radius: 5;");
        card.setPrefWidth(250);
        card.getChildren().addAll(
                new Label("FEATURED EXHIBITION"),
                new Label(exhibition.getTitle()) {
                    {
                        setStyle("-fx-font-weight: bold;");
                    }
                },
                new Label("Theme: " + exhibition.getTheme()),
                new Label("Gallery: " + (exhibition.getGallery() != null ? exhibition.getGallery().getName() : "Unknown")));
        discoverPane.getChildren().add(card);
    }

    private void addWorkshopCard(Workshop workshop) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: #f1f8e9; -fx-border-color: #4caf50; -fx-border-radius: 5; -fx-background-radius: 5;");
        card.setPrefWidth(250);
        card.getChildren().addAll(
                new Label("UPCOMING WORKSHOP"),
                new Label(workshop.getTitle()) {
                    {
                        setStyle("-fx-font-weight: bold;");
                    }
                },
                new Label("Instructor: " + (workshop.getInstructor() != null ? workshop.getInstructor().getName() : "Unknown")),
                new Label("Price: $" + workshop.getPrice()));
        discoverPane.getChildren().add(card);
    }
}
