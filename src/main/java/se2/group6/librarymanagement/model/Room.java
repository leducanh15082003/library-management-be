package se2.group6.librarymanagement.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")
public class Room {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "room_name")
   private String roomName;

   @Column(name = "floor")
   private int floor;

   @Column(name = "capacity")
   private int capacity;

   @Column(name = "image_url")
   private String imageUrl;

   @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<RoomBooking> roomBookings = new ArrayList<>();

   public List<RoomBooking> getRoomBookings() {
      return roomBookings;
   }

   public void setRoomBookings(List<RoomBooking> roomBookings) {
      this.roomBookings = roomBookings;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getRoomName() {
      return roomName;
   }

   public void setRoomName(String roomName) {
      this.roomName = roomName;
   }

   public int getCapacity() {
      return capacity;
   }

   public void setCapacity(int capacity) {
      this.capacity = capacity;
   }

   public int getFloor() {
      return floor;
   }

   public void setFloor(int floor) {
      this.floor = floor;
   }

   public String getImageUrl() {
      return imageUrl;
   }

   public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
   }
}
