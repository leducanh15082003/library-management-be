package se2.group6.librarymanagement.dto;

import java.util.List;

public class SubjectWithBooksDTO {
    private Long id;
    private String name;
    private String description;
    private List<BookResponseDTO> books;

    public SubjectWithBooksDTO() {
    }

    public SubjectWithBooksDTO(Long id, String name, String description, List<BookResponseDTO> books) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.books = books;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BookResponseDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookResponseDTO> books) {
        this.books = books;
    }
}
