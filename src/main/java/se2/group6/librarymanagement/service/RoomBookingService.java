package se2.group6.librarymanagement.service;

import se2.group6.librarymanagement.model.RoomBooking;

import java.util.List;

public interface RoomBookingService {
    void save(RoomBooking roomBooking);
    List<RoomBooking> findByUserId(Long userId);
    void cancelBooking(Long roomBookingId);
    void deleteBooking(Long roomBookingId);
}
