package com.project.artconnect.ui;

import java.time.LocalDateTime;

import com.project.artconnect.model.Booking;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.service.BookingService;
import com.project.artconnect.service.CommunityService;
import com.project.artconnect.service.WorkshopService;
import com.project.artconnect.util.ServiceProvider;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class BookingController {
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Booking> bookingTable;
    @FXML
    private TableColumn<Booking, Integer> idColumn;
    @FXML
    private TableColumn<Booking, String> workshopColumn;
    @FXML
    private TableColumn<Booking, String> memberColumn;
    @FXML
    private TableColumn<Booking, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<Booking, String> paymentStatusColumn;

    private final BookingService bookingService = ServiceProvider.getBookingService();
    private final WorkshopService workshopService = ServiceProvider.getWorkshopService();
    private final CommunityService communityService = ServiceProvider.getCommunityService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        workshopColumn.setCellValueFactory(cellData -> new SimpleStringProperty(resolveWorkshopLabel(cellData.getValue())));
        memberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(resolveMemberLabel(cellData.getValue())));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));
        paymentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

        refreshTable();
    }

    @FXML
    private void handleAddBooking() {
        Booking booking = promptBooking(null);
        if (booking == null) {
            return;
        }
        if (bookingService.bookingExists(booking.getWorkshopId(), booking.getMemberId())) {
            UiDialogUtils.warn("Add Booking", "This member already has a booking for this workshop.");
            return;
        }
        bookingService.createBooking(booking);
        refreshTable();
    }

    @FXML
    private void handleEditBooking() {
        Booking selected = bookingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiDialogUtils.warn("Edit Booking", "Please select a booking to edit.");
            return;
        }

        Booking updated = promptBooking(selected);
        if (updated == null) {
            return;
        }
        updated.setId(selected.getId());
        bookingService.updateBooking(updated);
        refreshTable();
    }

    @FXML
    private void handleDeleteBooking() {
        Booking selected = bookingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UiDialogUtils.warn("Delete Booking", "Please select a booking to delete.");
            return;
        }
        if (!UiDialogUtils.confirm("Delete Booking", "Delete selected booking?")) {
            return;
        }
        bookingService.deleteBooking(selected.getId());
        refreshTable();
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        bookingTable.setItems(FXCollections.observableArrayList(bookingService.searchBookings(query, null)));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        refreshTable();
    }

    private void refreshTable() {
        bookingTable.setItems(FXCollections.observableArrayList(bookingService.getAllBookings()));
    }

    private Booking promptBooking(Booking seed) {
        String currentWorkshop = seed != null ? resolveWorkshopLabel(seed) : "";
        String currentMember = seed != null ? resolveMemberLabel(seed) : "";
        String currentDate = seed != null && seed.getBookingDate() != null ? seed.getBookingDate().toString() : LocalDateTime.now().toString();
        String currentStatus = seed != null && seed.getPaymentStatus() != null ? seed.getPaymentStatus() : "PENDING";

        String workshopTitle = UiDialogUtils.promptText(seed == null ? "Add Booking" : "Edit Booking",
                "Enter workshop title:", currentWorkshop);
        if (workshopTitle == null) {
            return null;
        }
        Workshop workshop = resolveWorkshop(workshopTitle);
        if (workshop == null) {
            return null;
        }

        String memberName = UiDialogUtils.promptText(seed == null ? "Add Booking" : "Edit Booking",
                "Enter member name:", currentMember);
        if (memberName == null) {
            return null;
        }
        CommunityMember member = resolveMember(memberName);
        if (member == null) {
            return null;
        }

        String dateValue = UiDialogUtils.promptText(seed == null ? "Add Booking" : "Edit Booking",
                "Enter booking date (ISO format):", currentDate);
        if (dateValue == null) {
            return null;
        }

        String paymentStatus = UiDialogUtils.promptText(seed == null ? "Add Booking" : "Edit Booking",
                "Enter payment status (PENDING, PAID, CANCELLED):", currentStatus);
        if (paymentStatus == null) {
            return null;
        }

        Booking booking = new Booking();
        booking.setWorkshop(workshop);
        booking.setWorkshopId(workshop.getId());
        booking.setMember(member);
        booking.setMemberId(member.getId());
        booking.setBookingDate(parseDateTime(dateValue, seed != null ? seed.getBookingDate() : LocalDateTime.now()));
        booking.setPaymentStatus(parsePaymentStatus(paymentStatus, currentStatus));
        return booking;
    }

    private Workshop resolveWorkshop(String workshopTitle) {
        if (workshopTitle == null || workshopTitle.isBlank()) {
            UiDialogUtils.warn("Booking", "Workshop title is required.");
            return null;
        }
        return workshopService.getWorkshopByTitle(workshopTitle.trim()).orElseGet(() -> {
            UiDialogUtils.warn("Booking", "Workshop not found: " + workshopTitle);
            return null;
        });
    }

    private CommunityMember resolveMember(String memberName) {
        if (memberName == null || memberName.isBlank()) {
            UiDialogUtils.warn("Booking", "Member name is required.");
            return null;
        }
        return communityService.getMemberByName(memberName.trim()).orElseGet(() -> {
            UiDialogUtils.warn("Booking", "Member not found: " + memberName);
            return null;
        });
    }

    private String resolveWorkshopLabel(Booking booking) {
        if (booking == null) {
            return "";
        }
        if (booking.getWorkshop() != null && booking.getWorkshop().getTitle() != null) {
            return booking.getWorkshop().getTitle();
        }
        return booking.getWorkshopId() != null ? "Workshop #" + booking.getWorkshopId() : "Unknown";
    }

    private String resolveMemberLabel(Booking booking) {
        if (booking == null) {
            return "";
        }
        if (booking.getMember() != null && booking.getMember().getName() != null) {
            return booking.getMember().getName();
        }
        return booking.getMemberId() != null ? "Member #" + booking.getMemberId() : "Unknown";
    }

    private LocalDateTime parseDateTime(String value, LocalDateTime fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        try {
            return LocalDateTime.parse(value.trim());
        } catch (Exception exception) {
            return fallback;
        }
    }

    private String parsePaymentStatus(String value, String fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        String normalized = value.trim().toUpperCase();
        if (normalized.equals("PENDING") || normalized.equals("PAID") || normalized.equals("CANCELLED")) {
            return normalized;
        }
        return fallback;
    }
}