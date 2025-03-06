package se2.group6.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se2.group6.librarymanagement.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
