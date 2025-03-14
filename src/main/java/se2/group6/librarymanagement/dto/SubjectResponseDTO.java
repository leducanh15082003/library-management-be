package se2.group6.librarymanagement.dto;

import java.util.List;

public class SubjectResponseDTO {
    private Long id;
    private String name;
    private String description;
    private List<Long> bookIds;

    public SubjectResponseDTO() {
    }

    public SubjectResponseDTO(Long id, String name, String description, List<Long> bookIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.bookIds = bookIds;
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

    public List<Long> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Long> bookIds) {
        this.bookIds = bookIds;
    }
}
