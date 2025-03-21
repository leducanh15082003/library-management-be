package se2.group6.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se2.group6.librarymanagement.config.security.CustomUserDetails;
import se2.group6.librarymanagement.dto.BorrowedRecordResponseDTO;
import se2.group6.librarymanagement.model.BorrowedRecord;
import se2.group6.librarymanagement.service.BorrowedRecordService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/borrowed-record")
public class BorrowedRecordController {
    @Autowired
    private BorrowedRecordService borrowedRecordService;

    @GetMapping
    public ResponseEntity<List<BorrowedRecordResponseDTO>> getAllRecords() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = userDetails.getId();

        List<BorrowedRecord> records = borrowedRecordService.findByUserId(userId);
        List<BorrowedRecordResponseDTO> response = records.stream().map(record -> {
            LocalDateTime borrowDate = record.getBorrowAt();
            LocalDateTime returnDate = borrowDate.plusDays(7);
            String barcode = record.getBookCopy().getBarcode();
            String title = record.getBook().getTitle();
            return new BorrowedRecordResponseDTO(barcode, title, borrowDate, returnDate);
        }).toList();
        return ResponseEntity.ok(response);
    }
}
