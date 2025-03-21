package se2.group6.librarymanagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se2.group6.librarymanagement.model.BookCopy;
import se2.group6.librarymanagement.repository.BookCopyRepository;
import se2.group6.librarymanagement.service.BookCopyService;

@Service
public class BookCopyImpl implements BookCopyService {
    @Autowired
    private BookCopyRepository bookCopyRepository;

    @Override
    public BookCopy findById(Long id) {
        return bookCopyRepository.findById(id).orElse(null);
    }

    @Override
    public BookCopy save(BookCopy bookCopy) {
        return bookCopyRepository.save(bookCopy);
    }
}
