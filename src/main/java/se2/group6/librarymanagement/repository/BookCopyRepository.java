package se2.group6.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se2.group6.librarymanagement.model.Book;
import se2.group6.librarymanagement.model.BookCopy;

import java.util.List;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    List<BookCopy> findByBookId(Long bookId);
    long countBookCopiesByStatus(String status);
    long countByBook(Book book);

    void deleteByBook_Id(Long bookId);
}