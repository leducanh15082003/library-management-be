package se2.group6.librarymanagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se2.group6.librarymanagement.model.Book;
import se2.group6.librarymanagement.model.BookCopy;
import se2.group6.librarymanagement.model.BorrowedRecord;
import se2.group6.librarymanagement.repository.BookCopyRepository;
import se2.group6.librarymanagement.repository.BookRepository;
import se2.group6.librarymanagement.service.BookCopyService;
import se2.group6.librarymanagement.service.BorrowedRecordService;

import java.util.List;

@Service
public class BookCopyServiceImpl implements BookCopyService {
    @Autowired
    private BookCopyRepository bookCopyRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BorrowedRecordService borrowedRecordService;

    @Override
    public BookCopy findById(Long id) {
        return bookCopyRepository.findById(id).orElse(null);
    }

    @Override
    public BookCopy save(BookCopy bookCopy) {
        return bookCopyRepository.save(bookCopy);
    }

    @Override
    public long countAllBookCopies() {
        return bookCopyRepository.findAll().size();
    }

    @Override
    public long countBorrowedBooks() {
        return bookCopyRepository.countBookCopiesByStatus("Borrowed");
    }

    @Override
    public long countByBookId(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(()->new RuntimeException("Book not found"));
        return bookCopyRepository.countByBook(book);
    }
}
