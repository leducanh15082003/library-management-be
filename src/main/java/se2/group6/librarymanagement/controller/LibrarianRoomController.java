package se2.group6.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se2.group6.librarymanagement.model.Room;
import se2.group6.librarymanagement.service.CloudinaryService;
import se2.group6.librarymanagement.service.RoomService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/librarian/room")
public class LibrarianRoomController {

   @Autowired
   private RoomService roomService;

   @Autowired
   private CloudinaryService cloudinaryService;

   @GetMapping("/manage")
   public String manageRoom(
         @RequestParam(value = "roomName", required = false) String roomName,
         Model model) {
      List<Room> rooms = roomService.getAllRooms();

      if (roomName != null && !roomName.trim().isEmpty()) {
         String searchTerm = roomName.trim().toLowerCase();
         rooms = rooms.stream()
               .filter(room -> room.getRoomName().toLowerCase().contains(searchTerm))
               .collect(Collectors.toList());
      }

      model.addAttribute("rooms", rooms);
      model.addAttribute("searchQuery", roomName);

      return "librarian/manage-room";
   }

   @GetMapping("/add")
   public String showAddForm(Model model) {
      model.addAttribute("searchQuery", "");
      return "librarian/add-room";
   }

   @PostMapping("/add")
   public String addRoom(
         @RequestParam String roomName,
         @RequestParam int floor,
         @RequestParam int capacity,
         @RequestParam(required = false) MultipartFile image) {

      try {
         Room room = new Room();
         room.setRoomName(roomName);
         room.setFloor(floor);
         room.setCapacity(capacity);

         if (image != null && !image.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(image);
            room.setImageUrl(imageUrl);
         }

         roomService.saveRoom(room);
         return "redirect:/librarian/room/manage";
      } catch (Exception e) {
         e.printStackTrace();
         throw new RuntimeException("Error adding room: " + e.getMessage());
      }
   }

   @GetMapping("/edit/{id}")
   public String showEditForm(@PathVariable Long id, Model model) {
      Room room = roomService.getRoomById(id)
            .orElseThrow(() -> new RuntimeException("Room not found"));

      model.addAttribute("room", room);
      model.addAttribute("searchQuery", "");
      return "librarian/edit-room";
   }

   @PostMapping("/edit/{id}")
   public String updateRoom(
         @PathVariable Long id,
         @RequestParam String roomName,
         @RequestParam int floor,
         @RequestParam int capacity,
         @RequestParam(required = false) MultipartFile image,
         @RequestParam(required = false) String imageUrl) {

      try {
         Room room = roomService.getRoomById(id)
               .orElseThrow(() -> new RuntimeException("Room not found"));

         room.setRoomName(roomName);
         room.setFloor(floor);
         room.setCapacity(capacity);

         if (image != null && !image.isEmpty()) {
            String newImageUrl = cloudinaryService.uploadImage(image);
            room.setImageUrl(newImageUrl);
         } else if (imageUrl != null) {
            room.setImageUrl(imageUrl);
         }

         roomService.updateRoom(room);
         return "redirect:/librarian/room/manage";
      } catch (Exception e) {
         e.printStackTrace();
         throw new RuntimeException("Error updating room: " + e.getMessage());
      }
   }

   @PostMapping("/delete/{id}")
   public String deleteRoom(@PathVariable Long id) {
      roomService.deleteRoomById(id);
      return "redirect:/librarian/room/manage";
   }
}