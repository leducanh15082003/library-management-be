package se2.group6.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se2.group6.librarymanagement.model.BorrowedRecord;

import java.util.List;

public interface BorrowedRecordRepository extends JpaRepository<BorrowedRecord, Long> {

    // Tìm bản ghi mượn theo userId
    List<BorrowedRecord> findByUserId(Long userId);

    // Tìm bản ghi mượn chưa trả sách
    List<BorrowedRecord> findByReturnAtIsNull();
    long countByReturnAtIsNull();

    // Tìm bản ghi mượn theo studentId trong bảng User
    List<BorrowedRecord> findByUser_StudentId(String studentId); // Thêm _ để tham chiếu tới thuộc tính 'studentId' của User

}
