package se2.group6.librarymanagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se2.group6.librarymanagement.dto.BookCopyResponseDTO;
import se2.group6.librarymanagement.dto.BookWithCopiesResponseDTO;
import se2.group6.librarymanagement.model.Book;
import se2.group6.librarymanagement.model.BookCopy;
import se2.group6.librarymanagement.repository.BookCopyRepository;
import se2.group6.librarymanagement.repository.BookRepository;
import se2.group6.librarymanagement.service.BookService;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookCopyRepository bookCopyRepository) {
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public BookWithCopiesResponseDTO getBookWithCopies(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        List<BookCopy> copies = bookCopyRepository.findByBookId(id);
        List<BookCopyResponseDTO> copiesDTO = copies.stream().map(copy ->
                new BookCopyResponseDTO(
                        copy.getId(),
                        copy.getBarcode(),
                        copy.getStatus(),
                        copy.getCreatedAt(),
                        copy.getUpdatedAt()
                )
        ).toList();
        return new BookWithCopiesResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getImageUrl(),
                book.getSubject().getName(),
                copiesDTO
        );
    }
}
