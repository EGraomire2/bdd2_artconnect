package com.project.artconnect.ui;

import com.project.artconnect.config.DatabaseConfig;
import com.project.artconnect.util.ConnectionManager;
import com.project.artconnect.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
        String password = passwordField.getText() == null ? "" : passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }

        if (username.equals(DatabaseConfig.ROOT_USER) && password.equals(DatabaseConfig.ROOT_PASSWORD)) {
            ConnectionManager.setCredentials(DatabaseConfig.ROOT_USER, DatabaseConfig.ROOT_PASSWORD);
            UserSession.loginAdmin();
        } else if (username.equals(DatabaseConfig.READ_USER) && password.equals(DatabaseConfig.READ_PASSWORD)) {
            ConnectionManager.setCredentials(DatabaseConfig.READ_USER, DatabaseConfig.READ_PASSWORD);
            UserSession.loginReadOnly();
        } else {
            showError("Invalid username or password.");
            return;
        }

        if (mainController != null) {
            mainController.refreshAccess();
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        showError("Please contact the system administrator to reset credentials.");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
