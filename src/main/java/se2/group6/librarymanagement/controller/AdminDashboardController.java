package se2.group6.librarymanagement.controller;

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

    @GetMapping("/admin/dashboard")
    public String showDashboard(Model model) {
        // Tổng số tài liệu (sách, báo, tạp chí, nghiên cứu, ...)
        model.addAttribute("totalBooks", bookService.countAllBooks());

        // Tổng số phòng học
        model.addAttribute("totalRooms", roomService.countAllRooms());

        // Tổng số người dùng
        model.addAttribute("totalUsers", userService.countAllUsers());

        // Tổng số tài liệu đang được mượn
        model.addAttribute("borrowedBooks", borrowedRecordService.countCurrentlyBorrowedBooks());

        // Tổng số phòng đang được sử dụng
        model.addAttribute("borrowedRooms", roomBookingService.countCurrentlyBorrowedRooms());

        // Tài liệu theo thể loại
        model.addAttribute("totalReports", bookService.countByGenre("Tờ báo"));
        model.addAttribute("totalMagazines", bookService.countByGenre("Tạp chí"));
        model.addAttribute("totalResearch", bookService.countByGenre("Nghiên cứu"));

        return "admin/dashboard";
    }
}
