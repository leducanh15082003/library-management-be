package se2.group6.librarymanagement.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se2.group6.librarymanagement.model.RoomBooking;
import se2.group6.librarymanagement.model.RoomShift;
import se2.group6.librarymanagement.model.enums.RoomStatus;
import se2.group6.librarymanagement.repository.RoomBookingRepository;
import se2.group6.librarymanagement.repository.RoomShiftRepository;
import se2.group6.librarymanagement.service.RoomBookingService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoomBookingServiceImpl implements RoomBookingService {
    @Autowired
    private RoomBookingRepository roomBookingRepository;

    @Autowired
    private RoomShiftRepository roomShiftRepository;

    @Override
    public void save(RoomBooking roomBooking) {
        roomBookingRepository.save(roomBooking);
    }

    @Override
    public List<RoomBooking> findByUserId(Long userId) {
        return roomBookingRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public void cancelBooking(Long roomBookingId) {
        RoomBooking booking = roomBookingRepository.findById(roomBookingId).orElseThrow(()->new RuntimeException("Booking not found"));
        LocalDateTime now = LocalDateTime.now();
        if (!now.isBefore(booking.getStartTime())) {
            throw new RuntimeException("Không thể hủy khi đã đến giờ mượn phòng.");
        }
        RoomShift shift = booking.getRoomShift();
        if(shift != null) {
            shift.setStatus(RoomStatus.AVAILABLE);
            roomShiftRepository.save(shift);
        }
        booking.setStatus("CANCELLED");
        roomBookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Long roomBookingId) {
        roomBookingRepository.deleteById(roomBookingId);
    }
}
