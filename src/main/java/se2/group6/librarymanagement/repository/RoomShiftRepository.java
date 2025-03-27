package se2.group6.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se2.group6.librarymanagement.model.RoomShift;

import java.util.List;

public interface RoomShiftRepository extends JpaRepository<RoomShift, Long> {
    List<RoomShift> findByRoom_Id(Long roomId);
}
