package se2.group6.librarymanagement.dto;

import java.time.LocalDateTime;

public class BorrowedRecordResponseDTO {
    private String barcode;
    private String title;
    private LocalDateTime borrowedDate;
    private LocalDateTime returnedDate;

    public BorrowedRecordResponseDTO() {
    }

    public BorrowedRecordResponseDTO(String barcode, String title, LocalDateTime borrowedDate, LocalDateTime returnedDate) {
        this.barcode = barcode;
        this.title = title;
        this.borrowedDate = borrowedDate;
        this.returnedDate = returnedDate;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDateTime borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDateTime getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDateTime returnedDate) {
        this.returnedDate = returnedDate;
    }
}
