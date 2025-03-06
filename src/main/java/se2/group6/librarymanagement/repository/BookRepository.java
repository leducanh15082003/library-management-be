package se2.group6.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se2.group6.librarymanagement.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
