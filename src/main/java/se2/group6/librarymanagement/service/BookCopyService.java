package se2.group6.librarymanagement.service;

import se2.group6.librarymanagement.model.BookCopy;

public interface BookCopyService {
    BookCopy findById(Long id);
    BookCopy save(BookCopy bookCopy);
    long countAllBookCopies();
    long countBorrowedBooks();
    long countByBookId(Long id);

}
