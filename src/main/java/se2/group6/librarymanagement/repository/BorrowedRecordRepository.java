package se2.group6.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se2.group6.librarymanagement.model.BookCopy;
import se2.group6.librarymanagement.model.BorrowedRecord;

import java.util.List;

public interface BorrowedRecordRepository extends JpaRepository<BorrowedRecord, Long> {
    List<BorrowedRecord> findByUserId(Long userId);
    List<BorrowedRecord> findByReturnAtIsNull();
    long countByReturnAtIsNull();
    List<BorrowedRecord> findByUser_StudentId(String studentId);
    List<BorrowedRecord> findByUser_StudentIdContaining(String studentId);

    void deleteByBookCopy_Id(Long bookCopyId);
}
