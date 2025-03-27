package se2.group6.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se2.group6.librarymanagement.model.BookCopy;
import se2.group6.librarymanagement.service.impl.BookCopyServiceImpl;
import se2.group6.librarymanagement.service.impl.BorrowTransactionServiceImpl;

@Controller
@RequestMapping("/service")
public class BorrowController {

    @Autowired
    private BorrowTransactionServiceImpl borrowTransactionService;

    @Autowired
    private BookCopyServiceImpl bookCopyService;

    @PostMapping("/borrow")
    public String borrowCopy(@RequestParam("copyId") Long copyId, RedirectAttributes redirectAttributes) {
        try {
            borrowTransactionService.borrowProcess(copyId);
            redirectAttributes.addFlashAttribute("success", "Mượn sách thành công!");

            BookCopy copy = bookCopyService.findById(copyId);
            Long bookId = copy.getBook().getId();

            return "redirect:/service/borrow-success-page?search_id=" + bookId + "&copy_id=" + copyId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/service/borrow-page";
        }
    }
}

