package se2.group6.librarymanagement.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se2.group6.librarymanagement.model.BookCopy;
import se2.group6.librarymanagement.model.BorrowedRecord;
import se2.group6.librarymanagement.repository.BookCopyRepository;
import se2.group6.librarymanagement.repository.BorrowedRecordRepository;
import se2.group6.librarymanagement.service.BorrowedRecordService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowedRecordServiceImpl implements BorrowedRecordService {

    @Autowired
    private BorrowedRecordRepository borrowedRecordRepository;

    @Autowired
    private BookCopyRepository bookCopyRepository;

    @Override
    public void saveBorrowedRecord(BorrowedRecord borrowedRecord) {
        borrowedRecordRepository.save(borrowedRecord);
    }

    @Override
    public List<BorrowedRecord> findByUserId(Long id) {
        return borrowedRecordRepository.findByUserId(id);
    }

    @Override
    public List<BorrowedRecord> findByReturnAtIsNull() {
        return borrowedRecordRepository.findByReturnAtIsNull();
    }

    @Override
    public List<BorrowedRecord> findAll() {
        return borrowedRecordRepository.findAll();
    }

    @Transactional
    @Override
    public void markAsReturned(Long id) {
        BorrowedRecord borrowedRecord = borrowedRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bản ghi không tồn tại"));

        borrowedRecord.setReturnAt(LocalDateTime.now());
        BookCopy bookCopy = borrowedRecord.getBookCopy();
        bookCopy.setStatus("Available");
        bookCopyRepository.save(bookCopy);
        borrowedRecordRepository.save(borrowedRecord);
    }

    @Override
    public List<BorrowedRecord> findByStudentId(String studentId) {
        return borrowedRecordRepository.findByUser_StudentId(studentId);
    }

    @Override
    public List<BorrowedRecord> findByStudentIdContaining(String studentId) {
        return borrowedRecordRepository.findByUser_StudentIdContaining(studentId);
    }

    @Override
    public long countCurrentlyBorrowedBooks() {
        return borrowedRecordRepository.countByReturnAtIsNull();
    }
}
