package se2.group6.librarymanagement.dto;

import java.time.LocalDateTime;

public class BookCopyResponseDTO {
    private Long id;
    private String barcode;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BookCopyResponseDTO() {}

    public BookCopyResponseDTO(Long id, String barcode, String status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.barcode = barcode;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBarcode() {
        return barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
