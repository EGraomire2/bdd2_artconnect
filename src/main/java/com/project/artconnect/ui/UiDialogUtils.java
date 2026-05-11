package com.project.artconnect.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

public final class UiDialogUtils {
    private UiDialogUtils() {
    }

    public static String promptText(String title, String header, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue == null ? "" : defaultValue);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        return dialog.showAndWait().orElse(null);
    }

    public static Integer promptInt(String title, String header, Integer defaultValue) {
        String fallback = defaultValue == null ? "0" : defaultValue.toString();
        String value = promptText(title, header, fallback);
        if (value == null) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }

    public static Double promptDouble(String title, String header, Double defaultValue) {
        String fallback = defaultValue == null ? "0" : defaultValue.toString();
        String value = promptText(title, header, fallback);
        if (value == null) {
            return null;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }

    public static boolean confirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().map(button -> button.getButtonData().isDefaultButton()).orElse(false);
    }

    public static void warn(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
