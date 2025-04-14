package se2.group6.librarymanagement.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se2.group6.librarymanagement.model.Room;
import se2.group6.librarymanagement.model.RoomBooking;
import se2.group6.librarymanagement.model.RoomShift;
import se2.group6.librarymanagement.repository.RoomBookingRepository;
import se2.group6.librarymanagement.repository.RoomRepository;
import se2.group6.librarymanagement.repository.RoomShiftRepository;
import se2.group6.librarymanagement.service.RoomService;

import java.util.List;
import java.util.Optional;

@Service
public class RoomServiceImpl implements RoomService {
   private final RoomRepository roomRepository;
   private final RoomBookingRepository roomBookingRepository;
   private final RoomShiftRepository roomShiftRepository;

   @Autowired
   public RoomServiceImpl(RoomRepository roomRepository, RoomBookingRepository roomBookingRepository,
         RoomShiftRepository roomShiftRepository) {
      this.roomRepository = roomRepository;
      this.roomBookingRepository = roomBookingRepository;
      this.roomShiftRepository = roomShiftRepository;
   }

   @Override
   public List<Room> getAllRooms() {
      return roomRepository.findAll();
   }

   @Override
   public Optional<Room> getRoomById(Long id) {
      return roomRepository.findById(id);
   }

   @Override
   public Room saveRoom(Room room) {
      return roomRepository.save(room);
   }

   @Override
   public Room updateRoom(Room room) {
      return roomRepository.save(room);
   }

   @Override
   @Transactional
   public void deleteRoomById(Long id) {
      Room room = roomRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Room not found"));

      // Delete all room bookings associated with this room
      List<RoomBooking> bookings = roomBookingRepository.findByRoomId(id);
      roomBookingRepository.deleteAll(bookings);

      // Delete all room shifts associated with this room
      List<RoomShift> shifts = roomShiftRepository.findByRoom_Id(id);
      roomShiftRepository.deleteAll(shifts);

      // Delete the room
      roomRepository.deleteById(id);
   }
   @Override
   public long countAllRooms() {
      return roomRepository.count(); // JPA method đếm tổng số phòng
   }


}
