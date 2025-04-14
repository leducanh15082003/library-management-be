package se2.group6.librarymanagement.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se2.group6.librarymanagement.config.security.CustomUserDetails;
import se2.group6.librarymanagement.model.Room;
import se2.group6.librarymanagement.model.RoomBooking;
import se2.group6.librarymanagement.model.RoomShift;
import se2.group6.librarymanagement.model.User;
import se2.group6.librarymanagement.service.RoomBookingService;
import se2.group6.librarymanagement.service.RoomService;
import se2.group6.librarymanagement.service.RoomShiftService;
import se2.group6.librarymanagement.service.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/service")
public class BookRoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomShiftService roomShiftService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomBookingService roomBookingService;

    @GetMapping("/book-room")
    public String bookRoom(Model model) {
        List<Room> rooms = roomService.getAllRooms();
        model.addAttribute("rooms", rooms);
        return "service/book-room";
    }

    @GetMapping("/book-shift/{roomId}")
    public String bookShift(@PathVariable Long roomId, Model model) {
        List<RoomShift> shifts = roomShiftService.findByRoomId(roomId);
        Room room = roomService.getRoomById(roomId).orElseThrow(()->new RuntimeException("Room not found"));
        LocalTime currentTime = LocalTime.now();
        model.addAttribute("roomName", room.getRoomName());
        model.addAttribute("shifts", shifts);
        model.addAttribute("roomId", roomId);
        model.addAttribute("currentTime", currentTime);
        return "service/book-shift";
    }

    @PostMapping("/book")
    public String bookRoom(@RequestParam("shiftId") Long shiftId, RedirectAttributes redirectAttributes) {
        try {
            roomShiftService.bookShift(shiftId);
            RoomShift shift = roomShiftService.findById(shiftId);
            if(shift == null) {
                throw new RuntimeException("Shift not found");
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
                throw new RuntimeException("User not authenticated");
            }
            User currentUser = userService.getUserById(userDetails.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            RoomBooking booking = new RoomBooking();
            booking.setUser(currentUser);
            booking.setRoom(shift.getRoom());
            booking.setRoomShift(shift);
            booking.setStartTime(shift.getStartTime().atDate(LocalDate.now()));
            booking.setEndTime(shift.getEndTime().atDate(LocalDate.now()));
            booking.setBookedTime(LocalDateTime.now());

            roomBookingService.save(booking);
            redirectAttributes.addFlashAttribute("success", "Đặt phòng thành công");
            Long roomId = shift.getRoom().getId();
            return "redirect:/service/book-success-page?roomId=" + roomId + "&shiftId=" + shiftId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            RoomShift shift = roomShiftService.findById(shiftId);
            return "redirect:/service/book-shift/" + (shift != null ? shift.getRoom().getId() : "");
        }
    }

    @GetMapping("/book-success-page")
    public String showBookSuccessPage(@RequestParam("roomId") Long roomId,
                                      @RequestParam("shiftId") Long shiftId,
                                      Model model) {
        RoomShift shift = roomShiftService.findById(shiftId);
        Room room = roomService.getRoomById(roomId).orElseThrow(()->new RuntimeException("Room not found"));
        if (shift == null) {
            throw new RuntimeException("Shift not found");
        }
        model.addAttribute("roomName", room.getRoomName());
        model.addAttribute("roomId", roomId);
        model.addAttribute("room", shift.getRoom());
        model.addAttribute("shift", shift);
        return "service/book-success-page";
    }

    @GetMapping("/cancel-booking")
    public String cancelBooking(@RequestParam(value = "page", defaultValue = "1") int currentPage,
                                @RequestParam(value = "size", defaultValue = "10") int rowsPerPage,
                                Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            return "redirect:/auth/login";
        }
        Long userId = userDetails.getId();

        List<RoomBooking> allBookings = roomBookingService.findByUserId(userId).stream()
                .filter(booking -> !"CANCELLED".equalsIgnoreCase(booking.getStatus()) || !"DONE".equalsIgnoreCase(booking.getStatus()))
                .toList();

        LocalDateTime now = LocalDateTime.now();
        List<RoomBooking> expiredBookings = allBookings.stream()
                .filter(booking -> now.isAfter(booking.getEndTime()))
                .toList();
        for (RoomBooking booking : expiredBookings) {
            booking.setStatus("DONE");
            roomBookingService.save(booking);
        }

        allBookings = roomBookingService.findByUserId(userId).stream()
                .filter(booking -> !"CANCELLED".equalsIgnoreCase(booking.getStatus()) || !"DONE".equalsIgnoreCase(booking.getStatus()))
                .toList();
        int totalRecords = allBookings.size();
        int totalPages = (int) Math.ceil((double) totalRecords / rowsPerPage);

        if (totalRecords == 0) {
            currentPage = 1;
        } else if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        int startIndex = (currentPage - 1) * rowsPerPage;
        int endIndex = Math.min(startIndex + rowsPerPage, totalRecords);

        List<RoomBooking> paginatedBookings = new ArrayList<>();
        if (totalRecords > 0 && startIndex < totalRecords) {
            paginatedBookings = allBookings.subList(startIndex, endIndex);
        }

        model.addAttribute("bookings", paginatedBookings);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("rowsPerPage", rowsPerPage);
        model.addAttribute("totalPages", totalPages);
        return "service/cancel-booking";
    }

    @PostMapping("/cancel-booking")
    public String cancelBooking(@RequestParam("bookingId") Long bookingId,
                                RedirectAttributes redirectAttributes) {
        try {
            roomBookingService.cancelBooking(bookingId);
            redirectAttributes.addFlashAttribute("success", "Hủy đặt phòng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/service/cancel-booking";
    }

    @GetMapping("/booking-history")
    public String bookingHistory(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            return "redirect:/auth/login";
        }
        Long userId = userDetails.getId();

        List<RoomBooking> records = roomBookingService.findByUserId(userId);

        model.addAttribute("records", records);
        return "service/booking-history";
    }
}

