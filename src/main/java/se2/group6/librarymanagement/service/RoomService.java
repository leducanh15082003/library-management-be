package se2.group6.librarymanagement.service;

import se2.group6.librarymanagement.model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    List<Room> getAllRooms();
    Optional<Room> getRoomById(Long id);
    Room saveRoom(Room room);
    Room updateRoom(Room room);
    void deleteRoomById(Long id);
    long countAllRooms();
}
