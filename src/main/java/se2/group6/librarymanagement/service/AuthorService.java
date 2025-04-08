package se2.group6.librarymanagement.service;

import se2.group6.librarymanagement.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
   List<Author> getAllAuthors();

   Optional<Author> getAuthorById(Long id);

   Optional<Author> findByName(String name);

   Author saveAuthor(Author author);

   Author updateAuthor(Author author);

   void deleteAuthorById(Long id);
}
