package se2.group6.librarymanagement.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import se2.group6.librarymanagement.service.*;

@Controller
public class AdminDashboardController {

    @Autowired
    private BookService bookService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Autowired
    private BorrowedRecordService borrowedRecordService;

    @Autowired
    private RoomBookingService roomBookingService;

    @Autowired
    private RoomShiftService roomShiftService;

    @Autowired
    private BookCopyService bookCopyService;

    @GetMapping("/admin/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("totalDocuments", bookCopyService.countAllBookCopies());

        model.addAttribute("totalRooms", roomService.countAllRooms());

        model.addAttribute("totalUsers", userService.countAllUsers());

        model.addAttribute("borrowedBooks", bookCopyService.countBorrowedBooks());

        model.addAttribute("borrowedRooms", roomShiftService.countRoomsBeingBooked());

        model.addAttribute("totalBooks", bookService.countBySubject(1L));
        model.addAttribute("totalMagazines", bookService.countBySubject(2L));
        model.addAttribute("totalReports", bookService.countBySubject(3L));
        model.addAttribute("totalResearch", bookService.countBySubject(1L));

        return "admin/dashboard";
    }
}
