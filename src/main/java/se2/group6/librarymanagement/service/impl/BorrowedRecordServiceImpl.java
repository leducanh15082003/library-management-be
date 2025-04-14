package se2.group6.librarymanagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se2.group6.librarymanagement.model.BorrowedRecord;
import se2.group6.librarymanagement.repository.BorrowedRecordRepository;
import se2.group6.librarymanagement.service.BorrowedRecordService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowedRecordServiceImpl implements BorrowedRecordService {

    @Autowired
    private BorrowedRecordRepository borrowedRecordRepository;

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
        return borrowedRecordRepository.findAll(); // Trả về tất cả các bản ghi mượn
    }

    @Override
    public void markAsReturned(Long id) {
        // Lấy bản ghi mượn từ repository theo id
        BorrowedRecord borrowedRecord = borrowedRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bản ghi không tồn tại"));

        // Cập nhật ngày trả sách
        borrowedRecord.setReturnAt(LocalDateTime.now());

        // Lưu bản ghi đã được cập nhật vào cơ sở dữ liệu
        borrowedRecordRepository.save(borrowedRecord);
    }

    @Override
    public List<BorrowedRecord> findByStudentId(String studentId) {
        return borrowedRecordRepository.findByUser_StudentId(studentId); // Sửa để gọi theo thuộc tính user.studentId
    }
    @Override
    public long countCurrentlyBorrowedBooks() {
        return borrowedRecordRepository.countByReturnAtIsNull();
    }
}
