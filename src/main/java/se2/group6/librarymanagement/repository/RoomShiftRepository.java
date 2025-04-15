package se2.group6.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se2.group6.librarymanagement.model.RoomShift;
import se2.group6.librarymanagement.model.enums.RoomStatus;

import java.util.List;

public interface RoomShiftRepository extends JpaRepository<RoomShift, Long> {
    List<RoomShift> findByRoom_Id(Long roomId);

    List<RoomShift> findAllByStatus(RoomStatus status);

    @Query("SELECT COUNT(DISTINCT rs.room.id) FROM RoomShift rs WHERE rs.status = :status")
    long countDistinctByStatus(@Param("status") RoomStatus status);
}
