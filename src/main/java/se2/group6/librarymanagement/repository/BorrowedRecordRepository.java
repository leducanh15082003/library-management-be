package se2.group6.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se2.group6.librarymanagement.model.BorrowedRecord;

import java.util.List;

public interface BorrowedRecordRepository extends JpaRepository<BorrowedRecord, Long> {
    List<BorrowedRecord> findByUserId(Long userId);
}
