package se2.group6.librarymanagement.dto;

import java.util.List;

public class AuthorResponseDTO {
    private Long id;
    private String name;
    private String bio;
    private List<Long> bookIds;

    public AuthorResponseDTO() {
    }

    public AuthorResponseDTO(Long id, String name, String bio, List<Long> bookIds) {
        this.id = id;
        this.name = name;
        this.bio = bio;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Long> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Long> bookIds) {
        this.bookIds = bookIds;
    }
}
