package com.project.artconnect.ui;

import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.service.CommunityService;
import com.project.artconnect.util.ServiceProvider;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;

public class CommunityController {
    @FXML
    private TableView<CommunityMember> memberTable;
    @FXML
    private TableColumn<CommunityMember, String> nameColumn;
    @FXML
    private TableColumn<CommunityMember, String> emailColumn;
    @FXML
    private TableColumn<CommunityMember, String> cityColumn;

    private final CommunityService communityService = ServiceProvider.getCommunityService();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));

        refreshTable();
    }

    @FXML
    private void handleAddMember() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add New Community Member");
        dialog.setHeaderText("Enter member name:");
        String name = dialog.showAndWait().orElse("");
        dialog.setHeaderText("Enter member email:");
        String email = dialog.showAndWait().orElse("");
        dialog.setHeaderText("Enter member phone:");
        String phone = dialog.showAndWait().orElse("");
        dialog.setHeaderText("Enter member city:");
        String city = dialog.showAndWait().orElse("");
        CommunityMember newMember = new CommunityMember(name, email);
        newMember.setPhone(phone);
        newMember.setCity(city);
        communityService.getAllMembers(); // Call service to add - add method needed
        refreshTable();
    }

    private void refreshTable() {
        memberTable.setItems(FXCollections.observableArrayList(communityService.getAllMembers()));
    }
}
