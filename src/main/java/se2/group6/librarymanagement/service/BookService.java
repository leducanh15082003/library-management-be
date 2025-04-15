package se2.group6.librarymanagement.service;

import se2.group6.librarymanagement.dto.BookWithCopiesResponseDTO;
import se2.group6.librarymanagement.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getAllBooks();
    Optional<Book> getBookById(Long id);
    void saveBook(Book book);
    Book updateBook(Book book);
    void deleteBookById(Long id);
    List<Book> searchBooksByTitle(String title);
    BookWithCopiesResponseDTO getBookWithCopies(Long id);
    long countAllBooks();
    long countByGenre(String genre);
    long countBySubject(Long id);
}
