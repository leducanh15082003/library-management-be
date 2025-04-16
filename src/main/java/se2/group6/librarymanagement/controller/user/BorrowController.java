package se2.group6.librarymanagement.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import se2.group6.librarymanagement.config.security.CustomUserDetails;
import se2.group6.librarymanagement.dto.*;
import se2.group6.librarymanagement.model.Book;
import se2.group6.librarymanagement.model.BookCopy;
import se2.group6.librarymanagement.model.BorrowedRecord;
import se2.group6.librarymanagement.model.Subject;
import se2.group6.librarymanagement.service.*;
import se2.group6.librarymanagement.service.impl.BorrowTransactionServiceImpl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/service")
public class BorrowController {
    @Autowired
    private SubjectService subjectService;

    @Autowired
    private BookService bookService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private BorrowedRecordService borrowedRecordService;

    @Autowired
    private BorrowTransactionServiceImpl borrowTransactionService;

    @Autowired
    private BookCopyService bookCopyService;

    @GetMapping("/service-list")
    public String serviceList(Model model) {
        List<Map<String, String>> services = new ArrayList<>();
        services.add(Map.of("src", "https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742976425/muon-tai-lieu_wvqjsr.png", "text", "Mượn tài liệu", "link", "/service/borrow-page"));
        services.add(Map.of("src", "https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742976422/dat-phong-hoc_w7kmwq.png", "text", "Đặt phòng học", "link", "/service/book-room"));
        services.add(Map.of("src", "https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742976418/huy-dat-phong_uai4au.png", "text", "Hủy đặt phòng", "link", "/service/cancel-booking"));
        services.add(Map.of("src", "https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742976416/lich-su-muon_teh8l2.png", "text", "Lịch sử mượn, trả tài liệu", "link", "/service/borrow-history"));
        services.add(Map.of("src", "https://res.cloudinary.com/dlqpdl4mz/image/upload/v1742976427/lich-su-dat_tsx9od.png", "text", "Lịch sử đặt, hủy phòng", "link", "/service/booking-history"));
        model.addAttribute("services", services);
        return "/service/service-list";
    }

    @GetMapping("/borrow-page")
    public String borrowPage(
            @RequestParam(value = "category", required = false, defaultValue = "all") String category,
            @RequestParam(value = "title", required = false, defaultValue = "") String title,
            Model model) {

        List<SubjectWithBooksDTO> subjectDTOs;

        if (title != null && !title.trim().isEmpty()) {
            subjectDTOs = searchService.searchBooks(title, category);
        } else {
            List<Subject> allSubjects = subjectService.getAllSubjects();
            List<Subject> filteredSubjects = new ArrayList<>(allSubjects);

            if (!"all".equals(category)) {
                try {
                    Long catId = Long.parseLong(category);
                    filteredSubjects = filteredSubjects.stream()
                            .filter(subject -> subject.getId().equals(catId))
                            .toList();
                } catch (NumberFormatException ignored) {
                }
            }

            subjectDTOs = filteredSubjects.stream().map(subject -> {
                List<Book> booksList = new ArrayList<>(subject.getBooks());
                booksList.sort(Comparator.comparing(Book::getId));
                List<BookResponseDTO> books = booksList.stream()
                        .map(book -> new BookResponseDTO(
                                book.getId(),
                                book.getViewCount(),
                                book.getTitle(),
                                book.getAuthor().getId(),
                                book.getIsbn(),
                                book.getGenre(),
                                book.getPublisher(),
                                book.getPublishedYear(),
                                book.getCreatedAt(),
                                book.getUpdatedAt(),
                                book.getImageUrl()
                        ))
                        .collect(Collectors.toList());
                return new SubjectWithBooksDTO(
                        subject.getId(),
                        subject.getName(),
                        subject.getDescription(),
                        books
                );
            }).collect(Collectors.toList());
        }

        model.addAttribute("subjects", subjectDTOs);
        model.addAttribute("category", category);
        model.addAttribute("title", title);
        model.addAttribute("subjectsList", subjectService.getAllSubjects());
        model.addAttribute("loading", false);

        return "service/borrow-page";
    }


    @GetMapping("/borrow-status-page")
    public String showBorrowStatus(@RequestParam("search_id") Long id, Model model) {
        BookWithCopiesResponseDTO bookWithCopies = bookService.getBookWithCopies(id);
        model.addAttribute("book", bookWithCopies);
        model.addAttribute("subjectsList", subjectService.getAllSubjects());
        model.addAttribute("category", "all");
        model.addAttribute("title", "");
        return "service/borrow-status-page";
    }

    @GetMapping("/borrow-success-page")
    public String showBorrowSuccess(@RequestParam("search_id") Long bookId,
                                    @RequestParam("copy_id") Long copyId,
                                    Model model) {
        BookWithCopiesResponseDTO bookWithCopies = bookService.getBookWithCopies(bookId);
        if (bookWithCopies == null) {
            throw new RuntimeException("Book not found");
        }

        if (bookWithCopies.getCopies() != null) {
            List<BookCopyResponseDTO> filteredCopies = bookWithCopies.getCopies().stream()
                    .filter(copy -> copy.getId().equals(copyId))
                    .collect(Collectors.toList());
            bookWithCopies.setCopies(filteredCopies);
        }

        LocalDateTime borrowDate = LocalDateTime.now();
        LocalDateTime returnDate = borrowDate.plusDays(7);

        model.addAttribute("book", bookWithCopies);
        model.addAttribute("subjectsList", subjectService.getAllSubjects());
        model.addAttribute("category", "all");
        model.addAttribute("title", "");
        model.addAttribute("returnDate", returnDate);
        return "service/borrow-success-page";
    }

    @GetMapping("/borrow-history")
    public String getAllRecords(
            @RequestParam(value = "page", defaultValue = "1") int currentPage,
            @RequestParam(value = "size", defaultValue = "10") int rowsPerPage,
            Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            return "redirect:/auth/login";
        }
        Long userId = userDetails.getId();

        List<BorrowedRecord> allRecords = borrowedRecordService.findByUserId(userId);
        int totalRecords = allRecords.size();
        int totalPages = (int) Math.ceil((double) totalRecords / rowsPerPage);

        int startIndex = (currentPage - 1) * rowsPerPage;
        int endIndex = Math.min(startIndex + rowsPerPage, totalRecords);

        List<BorrowedRecord> paginatedRecords = new ArrayList<>();
        if (totalRecords > 0 && startIndex < totalRecords) {
            paginatedRecords = allRecords.subList(startIndex, endIndex);
        }

        List<BorrowedRecordResponseDTO> response = paginatedRecords.stream()
                .map(record -> {
                    LocalDateTime borrowDate = record.getBorrowAt();
                    LocalDateTime dueDate = borrowDate.plusDays(7);
                    LocalDateTime returnedDate = record.getReturnAt();
                    String barcode = record.getBookCopy().getBarcode();
                    String title = record.getBook().getTitle();
                    return new BorrowedRecordResponseDTO(barcode, title, borrowDate, dueDate, returnedDate);
                })
                .collect(Collectors.toList());

        model.addAttribute("records", response);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("rowsPerPage", rowsPerPage);
        model.addAttribute("totalPages", totalPages);

        return "service/borrow-history";
    }

    @PostMapping("/borrow")
    public String borrowCopy(@RequestParam("copyId") Long copyId, RedirectAttributes redirectAttributes) {
        try {
            borrowTransactionService.borrowProcess(copyId);
            redirectAttributes.addFlashAttribute("success", "Mượn sách thành công!");

            BookCopy copy = bookCopyService.findById(copyId);
            Long bookId = copy.getBook().getId();

            return "redirect:/service/borrow-success-page?search_id=" + bookId + "&copy_id=" + copyId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/service/borrow-page";
        }
    }
}
