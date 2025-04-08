package se2.group6.librarymanagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se2.group6.librarymanagement.model.BorrowedRecord;
import se2.group6.librarymanagement.repository.BorrowedRecordRepository;
import se2.group6.librarymanagement.service.BorrowedRecordService;

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
}
