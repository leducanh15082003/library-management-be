package se2.group6.librarymanagement.service;

import se2.group6.librarymanagement.model.BorrowedRecord;

import java.util.List;

public interface BorrowedRecordService {

    void saveBorrowedRecord(BorrowedRecord borrowedRecord);

    List<BorrowedRecord> findByUserId(Long id);

    List<BorrowedRecord> findByReturnAtIsNull();

    List<BorrowedRecord> findAll();

    void markAsReturned(Long id);

    List<BorrowedRecord> findByStudentId(String studentId); // Thêm phương thức tìm kiếm theo studentId

}
