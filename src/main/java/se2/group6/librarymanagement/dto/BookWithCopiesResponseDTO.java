package se2.group6.librarymanagement.dto;

import java.util.List;

public class BookWithCopiesResponseDTO {
    private Long id;
    private String title;
    private String imageUrl;
    private String subject;
    private List<BookCopyResponseDTO> copies;

    public BookWithCopiesResponseDTO() {}

    public BookWithCopiesResponseDTO(Long id, String title, String imageUrl, String subject, List<BookCopyResponseDTO> copies) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.subject = subject;
        this.copies = copies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<BookCopyResponseDTO> getCopies() {
        return copies;
    }

    public void setCopies(List<BookCopyResponseDTO> copies) {
        this.copies = copies;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
