package com.project.artconnect.service;

import java.util.List;
import java.util.Optional;

import com.project.artconnect.model.Booking;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Workshop;

public interface WorkshopService {
    List<Workshop> getAllWorkshops();

    Optional<Workshop> getWorkshopByTitle(String title);

    void bookWorkshop(Workshop workshop, CommunityMember member);

    List<Booking> getBookingsByMember(CommunityMember member);

    List<Workshop> searchWorkshops(String query, String instructor, String level);

    void createWorkshop(Workshop workshop);

    void updateWorkshop(Workshop workshop);

    void deleteWorkshop(int workshopId);
}
