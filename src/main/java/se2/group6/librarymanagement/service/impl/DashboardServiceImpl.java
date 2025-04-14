package se2.group6.librarymanagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se2.group6.librarymanagement.repository.BookRepository;
import se2.group6.librarymanagement.repository.RoomBookingRepository;
import se2.group6.librarymanagement.repository.UserRepository;
import se2.group6.librarymanagement.repository.BorrowedRecordRepository;
import se2.group6.librarymanagement.service.DashboardService;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RoomBookingRepository roomBookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BorrowedRecordRepository borrowedRecordRepository;

    @Override
    public Map<String, Long> getDashboardStatistics() {
        Map<String, Long> stats = new HashMap<>();

        long totalDocuments = bookRepository.count();
        long totalRooms = roomBookingRepository.count();  // Bạn có thể tính số phòng từ RoomBooking nếu cần
        long totalUsers = userRepository.count();
        long totalBorrowedDocuments = borrowedRecordRepository.countByReturnAtIsNull();

        // Đếm số phòng đang mượn từ RoomBooking với status là "BORROWED"
        long totalBorrowedRooms = roomBookingRepository.countByStatus("BORROWED");

        long totalBooks = bookRepository.countByGenre("Sách");
        long totalMagazines = bookRepository.countByGenre("Tạp chí");
        long totalResearch = bookRepository.countByGenre("Nghiên cứu");

        stats.put("totalDocuments", totalDocuments);
        stats.put("totalRooms", totalRooms); // Số phòng có thể được lấy từ roomBookingRepository hoặc dựa trên các thông tin khác
        stats.put("totalUsers", totalUsers);
        stats.put("totalBorrowedDocuments", totalBorrowedDocuments);
        stats.put("totalBorrowedRooms", totalBorrowedRooms); // Tính số phòng đang mượn
        stats.put("totalBooks", totalBooks);
        stats.put("totalMagazines", totalMagazines);
        stats.put("totalResearch", totalResearch);

        return stats;
    }
}

