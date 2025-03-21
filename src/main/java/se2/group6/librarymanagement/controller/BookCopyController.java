package se2.group6.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se2.group6.librarymanagement.model.BookCopy;
import se2.group6.librarymanagement.service.BookCopyService;

@RestController
@RequestMapping("/api/v1/book-copy")
public class BookCopyController {
    @Autowired
    private BookCopyService bookCopyService;

    @PutMapping("/{copyId}/borrow")
    public ResponseEntity<Void> borrowCopy(@PathVariable Long copyId) {
        BookCopy copy = bookCopyService.findById(copyId);
        if (copy == null) {
            return ResponseEntity.notFound().build();
        }

        if ("Borrowed".equalsIgnoreCase(copy.getStatus())) {
            return ResponseEntity.badRequest().build();
        }

        copy.setStatus("Borrowed");
        bookCopyService.save(copy);

        return ResponseEntity.ok().build();
    }
}
