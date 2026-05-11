package com.project.artconnect.ui;

import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.service.CommunityService;
import com.project.artconnect.util.ServiceProvider;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CommunityController {
    @FXML
    private TextField searchField;
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
        String name = UiDialogUtils.promptText("Add Community Member", "Enter member name:", "");
        if (name == null) {
            return;
        }
        String email = UiDialogUtils.promptText("Add Community Member", "Enter member email:", "");
        if (email == null) {
            return;
        }
        String phone = UiDialogUtils.promptText("Add Community Member", "Enter member phone:", "");
        if (phone == null) {
            return;
        }
        String city = UiDialogUtils.promptText("Add Community Member", "Enter member city:", "");
        if (city == null) {
            return;
        }

        CommunityMember newMember = new CommunityMember(name, email);
        newMember.setPhone(phone);
        newMember.setCity(city);
        communityService.createMember(newMember);
        refreshTable();
    }

    @FXML
    private void handleEditMember() {
        CommunityMember selected = memberTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiDialogUtils.warn("Edit Community Member", "Please select a member to edit.");
            return;
        }

        String email = UiDialogUtils.promptText("Edit Community Member", "Enter member email:", selected.getEmail());
        if (email == null) {
            return;
        }
        Integer birthYear = UiDialogUtils.promptInt("Edit Community Member", "Enter member birth year:",
            selected.getBirthYear() != null ? selected.getBirthYear() : Integer.valueOf(0));
        if (birthYear == null) {
            return;
        }
        String phone = UiDialogUtils.promptText("Edit Community Member", "Enter member phone:", selected.getPhone());
        if (phone == null) {
            return;
        }
        String city = UiDialogUtils.promptText("Edit Community Member", "Enter member city:", selected.getCity());
        if (city == null) {
            return;
        }
        String membershipType = UiDialogUtils.promptText("Edit Community Member", "Enter membership type:",
                selected.getMembershipType());
        if (membershipType == null) {
            return;
        }

        CommunityMember updated = new CommunityMember(selected.getName(), email);
        updated.setId(selected.getId());
        updated.setBirthYear(birthYear);
        updated.setPhone(phone);
        updated.setCity(city);
        updated.setMembershipType(membershipType);
        updated.setFavoriteDisciplines(selected.getFavoriteDisciplines());
        updated.setBookings(selected.getBookings());
        updated.setReviews(selected.getReviews());

        communityService.updateMember(updated);
        refreshTable();
    }

    @FXML
    private void handleDeleteMember() {
        CommunityMember selected = memberTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiDialogUtils.warn("Delete Community Member", "Please select a member to delete.");
            return;
        }
        if (!UiDialogUtils.confirm("Delete Community Member", "Delete selected member?")) {
            return;
        }
        communityService.deleteMember(selected.getId());
        refreshTable();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        memberTable.setItems(FXCollections.observableArrayList(communityService.searchMembers(query, null, null)));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        refreshTable();
    }

    private void refreshTable() {
        memberTable.setItems(FXCollections.observableArrayList(communityService.getAllMembers()));
    }
}
