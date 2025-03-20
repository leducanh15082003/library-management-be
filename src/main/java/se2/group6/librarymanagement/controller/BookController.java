package se2.group6.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se2.group6.librarymanagement.dto.BookResponseDTO;
import se2.group6.librarymanagement.model.Author;
import se2.group6.librarymanagement.model.Book;
import se2.group6.librarymanagement.model.Subject;
import se2.group6.librarymanagement.service.AuthorService;
import se2.group6.librarymanagement.service.BookService;
import se2.group6.librarymanagement.service.SubjectService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/book")
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final SubjectService subjectService;

    @Autowired
    public BookController(BookService bookService, AuthorService authorService, SubjectService subjectService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        List<BookResponseDTO> response = books.stream()
                .map(book -> new BookResponseDTO(
                        book.getId(),
                        book.getViewCount(),
                        book.getTitle(),
                        book.getAuthor().getId(),
                        book.getIsbn(),
                        book.getGenre(),
                        book.getPublisher(),
                        book.getPublishedYear(),
                        book.getCreatedAt(),
                        book.getUpdatedAt(),
                        book.getImageUrl()
                ))
                .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> optionalBook = bookService.getBookById(id);
        return optionalBook
                .map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Book> createNewBook(@RequestBody Book book) {
        Book savedBook = bookService.saveBook(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetail) {
        Optional<Book> optionalBook = bookService.getBookById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setTitle(bookDetail.getTitle());

            Author author = authorService.getAuthorById(bookDetail.getAuthor().getId())
                            .orElseThrow(() -> new RuntimeException("Author not found"));
            book.setAuthor(author);

            book.setIsbn(bookDetail.getIsbn());
            book.setGenre(bookDetail.getGenre());
            book.setPublisher(bookDetail.getPublisher());
            book.setPublishedYear(bookDetail.getPublishedYear());

            Subject subject = subjectService.getSubjectById(bookDetail.getSubject().getId())
                            .orElseThrow(() -> new RuntimeException("Subject not found"));
            book.setSubject(subject);

            book.setUpdatedAt(bookDetail.getUpdatedAt());
            book.setBorrowedRecords(null);
            Book updatedBook = bookService.updateBook(book);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable Long id) {
        Optional<Book> optionalBook = bookService.getBookById(id);
        if (optionalBook.isPresent()) {
            bookService.deleteBookById(id);
            return new ResponseEntity<>(optionalBook.get(), HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
