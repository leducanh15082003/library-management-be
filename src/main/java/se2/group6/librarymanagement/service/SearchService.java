package se2.group6.librarymanagement.service;

import se2.group6.librarymanagement.dto.SubjectWithBooksDTO;

import java.util.List;

public interface SearchService {
    List<SubjectWithBooksDTO> searchBooks(String title, String category);
}
