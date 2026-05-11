package com.project.artconnect.ui;

import com.project.artconnect.util.ConnectionManager;
import com.project.artconnect.util.UserSession;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML
    private TabPane mainTabPane;

    @FXML
    private Tab discoverTab;

    @FXML
    private Tab artistsTab;

    @FXML
    private Tab artworksTab;

    @FXML
    private Tab galleriesTab;

    @FXML
    private Tab exhibitionsTab;

    @FXML
    private Tab workshopsTab;

    @FXML
    private Tab communityTab;

    @FXML
    private MenuItem loginMenuItem;

    @FXML
    private MenuItem logoutMenuItem;

    @FXML
    private Button loginButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Label userStatusLabel;

    private final List<Tab> protectedTabs = new ArrayList<>();

    @FXML
    public void initialize() {
        protectedTabs.add(artistsTab);
        protectedTabs.add(artworksTab);
        protectedTabs.add(galleriesTab);
        protectedTabs.add(exhibitionsTab);
        protectedTabs.add(workshopsTab);
        protectedTabs.add(communityTab);

        ConnectionManager.resetToDefaultConnection();
        UserSession.loginReadOnly();
        applyAccessState();
    }

    @FXML
    private void handleOpenLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/artconnect/ui/login.fxml"));
            Parent root = loader.load();
            LoginController controller = loader.getController();
            controller.setMainController(this);

            Stage loginStage = new Stage();
            loginStage.initOwner(mainTabPane.getScene().getWindow());
            loginStage.initModality(Modality.APPLICATION_MODAL);
            loginStage.setResizable(false);
            loginStage.setTitle("ArtConnect Login");
            loginStage.setScene(new Scene(root));
            loginStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        UserSession.loginReadOnly();
        ConnectionManager.resetToDefaultConnection();
        applyAccessState();
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }

    public void refreshAccess() {
        applyAccessState();
    }

    private void applyAccessState() {
        if (UserSession.isAdmin()) {
            showProtectedTabs();
            loginMenuItem.setDisable(true);
            loginButton.setDisable(true);
            logoutMenuItem.setDisable(false);
            logoutButton.setDisable(false);
        } else {
            hideProtectedTabs();
            loginMenuItem.setDisable(false);
            loginButton.setDisable(false);
            logoutMenuItem.setDisable(true);
            logoutButton.setDisable(true);
        }

        userStatusLabel.setText("Current user: " + UserSession.getDisplayName());
    }

    private void hideProtectedTabs() {
        mainTabPane.getTabs().removeAll(protectedTabs);
    }

    private void showProtectedTabs() {
        if (!mainTabPane.getTabs().contains(artistsTab)) {
            mainTabPane.getTabs().addAll(protectedTabs);
        }
    }
}
