package se2.group6.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se2.group6.librarymanagement.dto.BookResponseDTO;
import se2.group6.librarymanagement.dto.BookWithCopiesResponseDTO;
import se2.group6.librarymanagement.dto.SubjectWithBooksDTO;
import se2.group6.librarymanagement.model.Book;
import se2.group6.librarymanagement.model.Subject;
import se2.group6.librarymanagement.service.BookService;
import se2.group6.librarymanagement.service.SubjectService;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/service")
public class ServiceController {
    @Autowired
    private SubjectService subjectService;

    @Autowired
    private BookService bookService;

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
            @RequestParam(value = "sortType", required = false, defaultValue = "id") String sortType,
            @RequestParam(value = "category", required = false, defaultValue = "all") String category,
            @RequestParam(value = "title", required = false, defaultValue = "") String title,
            Model model) {

        List<SubjectWithBooksDTO> subjectDTOs;

        if (title != null && !title.trim().isEmpty()) {
            List<Book> searchedBooks = bookService.searchBooksByTitle(title);
            if (!"all".equals(category)) {
                try {
                    Long catId = Long.parseLong(category);
                    searchedBooks = searchedBooks.stream()
                            .filter(book -> book.getSubject().getId().equals(catId))
                            .toList();
                } catch (NumberFormatException ignored) {
                }
            }
            Map<Long, List<Book>> groupedBooks = searchedBooks.stream()
                    .collect(Collectors.groupingBy(book -> book.getSubject().getId()));

            subjectDTOs = new ArrayList<>();
            for (Map.Entry<Long, List<Book>> entry : groupedBooks.entrySet()) {
                List<Book> booksList = new ArrayList<>(entry.getValue());
                if ("most-view".equalsIgnoreCase(sortType)) {
                    booksList.sort(Comparator.comparingInt(Book::getViewCount).reversed());
                } else if ("new".equalsIgnoreCase(sortType)) {
                    booksList.sort(Comparator.comparing(Book::getCreatedAt).reversed());
                } else { // mặc định: sort theo id
                    booksList.sort(Comparator.comparing(Book::getId));
                }
                List<BookResponseDTO> booksDTO = booksList.stream()
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

                Book firstBook = booksList.getFirst();
                SubjectWithBooksDTO subjectDto = new SubjectWithBooksDTO(
                        firstBook.getSubject().getId(),
                        firstBook.getSubject().getName(),
                        firstBook.getSubject().getDescription(),
                        booksDTO
                );
                subjectDTOs.add(subjectDto);
            }
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
                if ("most-view".equalsIgnoreCase(sortType)) {
                    booksList.sort(Comparator.comparingInt(Book::getViewCount).reversed());
                } else if ("new".equalsIgnoreCase(sortType)) {
                    booksList.sort(Comparator.comparing(Book::getCreatedAt).reversed());
                } else {
                    booksList.sort(Comparator.comparing(Book::getId));
                }
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
        model.addAttribute("sortType", sortType);
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
        return "service/borrow-status-page";
    }

    @GetMapping("/book-room")
    public String bookRoom(Model model) {
        return "service/book-room";
    }

    @GetMapping("/cancel-booking")
    public String cancelBooking(Model model) {
        return "service/cancel-booking";
    }

    @GetMapping("/borrow-history")
    public String borrowHistory(Model model) {
        return "service/borrow-history";
    }

    @GetMapping("/booking-history")
    public String bookingHistory(Model model) {
        return "service/booking-history";
    }
}
