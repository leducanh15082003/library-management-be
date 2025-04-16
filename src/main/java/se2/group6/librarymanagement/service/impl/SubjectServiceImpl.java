package se2.group6.librarymanagement.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import se2.group6.librarymanagement.model.Book;
import se2.group6.librarymanagement.model.BookCopy;
import se2.group6.librarymanagement.model.Subject;
import se2.group6.librarymanagement.repository.BookCopyRepository;
import se2.group6.librarymanagement.repository.BookRepository;
import se2.group6.librarymanagement.repository.BorrowedRecordRepository;
import se2.group6.librarymanagement.repository.SubjectRepository;
import se2.group6.librarymanagement.service.CloudinaryService;
import se2.group6.librarymanagement.service.SubjectService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final BookRepository bookRepository;
    private final SubjectRepository subjectRepository;
    private final CloudinaryService cloudinaryService;
    private final BookCopyRepository bookCopyRepository;
    private final BorrowedRecordRepository borrowedRecordRepository;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository, CloudinaryService cloudinaryService, BookRepository bookRepository, BookCopyRepository bookCopyRepository, BorrowedRecordRepository borrowedRecordRepository) {
        this.subjectRepository = subjectRepository;
        this.cloudinaryService = cloudinaryService;
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.borrowedRecordRepository = borrowedRecordRepository;
    }

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public Optional<Subject> getSubjectById(Long id) {
        return subjectRepository.findById(id);
    }

    @Override
    public void saveSubject(Subject subject, MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(imageFile);
                subject.setImageUrl(imageUrl);
            }
        } catch (IOException e) {
            throw new RuntimeException("Không thể upload ảnh: " + e.getMessage());
        }

        subjectRepository.save(subject);
    }

    @Override
    public void updateSubject(Subject subject) {
        subjectRepository.save(subject);
    }

    @Transactional
    @Override
    public void deleteSubjectById(Long id) {
        List<Book> books = bookRepository.findBySubject_Id(id);

        for (Book book : books) {
            List<BookCopy> copies = bookCopyRepository.findByBookId(book.getId());
            for (BookCopy copy : copies) {
                borrowedRecordRepository.deleteByBookCopy_Id(copy.getId());
            }
            bookCopyRepository.deleteAll(copies);
            bookRepository.delete(book);
        }

        subjectRepository.deleteById(id);
    }

    @Override
    public Subject getSubjectByName(String name) {
        return subjectRepository.findByName(name);
    }

    @Override
    public List<Subject> searchByName(String name) {
        return subjectRepository.findByNameContainingIgnoreCase(name);
    }
}
