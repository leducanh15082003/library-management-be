package se2.group6.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se2.group6.librarymanagement.service.impl.BorrowTransactionServiceImpl;

@RestController
@RequestMapping("/api/v1/borrow")
public class BorrowController {

    @Autowired
    private BorrowTransactionServiceImpl borrowTransactionService;

    @PostMapping("")
    public ResponseEntity<Void> borrowCopy(@RequestParam Long copyId) {
        try {
            borrowTransactionService.borrowProcess(copyId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

