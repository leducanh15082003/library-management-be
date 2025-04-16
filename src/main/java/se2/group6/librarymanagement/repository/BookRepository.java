package se2.group6.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se2.group6.librarymanagement.model.Book;
import se2.group6.librarymanagement.model.Subject;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    long countByGenre(String genre);

    long countBooksBySubject_Id(Long subjectId);

    List<Book> findBySubject_Id(Long subjectId);
}
