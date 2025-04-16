package se2.group6.librarymanagement.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se2.group6.librarymanagement.model.BorrowedRecord;
import se2.group6.librarymanagement.service.BorrowedRecordService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class ReturnedBookManagementController {

    @Autowired
    private BorrowedRecordService borrowedRecordService;

    @ResponseBody
    @GetMapping("/return-test")
    public String testReturnAccess() {
        return "This is a simple text response";
    }

    @GetMapping("/return-page")
    public String showReturnPage(@RequestParam(name = "studentId", required = false) String studentId, Model model) {
        List<BorrowedRecord> borrowedRecordList;

        if (studentId != null && !studentId.isEmpty()) {
            borrowedRecordList = borrowedRecordService.findByStudentId(studentId);
        } else {
            borrowedRecordList = borrowedRecordService.findAll();
        }

        model.addAttribute("borrowedRecordList", borrowedRecordList);
        model.addAttribute("searchValue", studentId);
        return "admin/return-page";
    }

    @PostMapping("/return-book/{id}")
    public String returnBook(@PathVariable Long id) {
        borrowedRecordService.markAsReturned(id);
        return "redirect:/admin/return-page";
    }
}
