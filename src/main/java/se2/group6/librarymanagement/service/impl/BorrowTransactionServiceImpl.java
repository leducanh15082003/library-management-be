package se2.group6.librarymanagement.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import se2.group6.librarymanagement.config.security.CustomUserDetails;
import se2.group6.librarymanagement.model.BookCopy;
import se2.group6.librarymanagement.model.BorrowedRecord;
import se2.group6.librarymanagement.model.User;
import se2.group6.librarymanagement.service.BookCopyService;
import se2.group6.librarymanagement.service.BorrowedRecordService;
import se2.group6.librarymanagement.service.UserService;

@Service
public class BorrowTransactionServiceImpl {
    @Autowired
    private BorrowedRecordService borrowedRecordService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookCopyService bookCopyService;

    @Transactional
    public void borrowProcess(Long copyId) {
        BookCopy copy = bookCopyService.findById(copyId);
        if (copy == null) {
            throw new RuntimeException("Book copy not found");
        }
        if ("Borrowed".equalsIgnoreCase(copy.getStatus())) {
            throw new RuntimeException("Book copy already borrowed");
        }

        copy.setStatus("Borrowed");
        bookCopyService.save(copy);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new RuntimeException("User not authenticated");
        }
        Long userId = userDetails.getId();

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BorrowedRecord record = new BorrowedRecord();
        record.setUser(user);
        record.setBook(copy.getBook());
        record.setBookCopy(copy);
        borrowedRecordService.saveBorrowedRecord(record);
    }
}
