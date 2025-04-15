package se2.group6.librarymanagement.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import se2.group6.librarymanagement.model.RoomShift;
import se2.group6.librarymanagement.model.enums.RoomStatus;
import se2.group6.librarymanagement.repository.RoomShiftRepository;
import se2.group6.librarymanagement.service.RoomShiftService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoomShiftServiceImpl implements RoomShiftService {

    @Autowired
    private RoomShiftRepository roomShiftRepository;

    @Override
    public List<RoomShift> findByRoomId(Long roomId) {
        return roomShiftRepository.findByRoom_Id(roomId);
    }

    @Override
    public RoomShift findById(Long id) {
        return roomShiftRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void bookShift(Long shiftId) {
        RoomShift shift = roomShiftRepository.findById(shiftId).orElseThrow(()->new RuntimeException("Room Shift Not Found"));
        if (!"AVAILABLE".equalsIgnoreCase(shift.getStatus().name())) {
            throw new RuntimeException("Shift is booked");
        }

        shift.setStatus(RoomStatus.BOOKED);
        roomShiftRepository.save(shift);
    }

    @Override
    public long countRoomsBeingBooked() {
        return roomShiftRepository.countDistinctByStatus(RoomStatus.BOOKED);
    }

    @Scheduled(fixedDelay = 300000)
    @Transactional
    public void expiredBookedShift() {
        LocalDateTime now = LocalDateTime.now();
        List<RoomShift> bookedShifts = roomShiftRepository.findAllByStatus(RoomStatus.BOOKED);
        for (RoomShift shift : bookedShifts) {
            LocalDateTime shiftEnd = shift.getEndTime().atDate(LocalDate.now());
            if (now.isAfter(shiftEnd)) {
                shift.setStatus(RoomStatus.AVAILABLE);
                roomShiftRepository.save(shift);
            }
        }
    }


}
