package se2.group6.librarymanagement.service;

import se2.group6.librarymanagement.model.RoomShift;

import java.util.List;

public interface RoomShiftService {
    List<RoomShift> findByRoomId(Long roomId);
    RoomShift findById(Long id);
    void bookShift(Long shiftId);
    long countRoomsBeingBooked();
}
