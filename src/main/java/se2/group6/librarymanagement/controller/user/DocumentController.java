package se2.group6.librarymanagement.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import se2.group6.librarymanagement.dto.BookResponseDTO;
import se2.group6.librarymanagement.dto.SubjectWithBooksDTO;
import se2.group6.librarymanagement.model.Book;
import se2.group6.librarymanagement.model.Subject;
import se2.group6.librarymanagement.service.BookService;
import se2.group6.librarymanagement.service.SearchService;
import se2.group6.librarymanagement.service.SubjectService;
import se2.group6.librarymanagement.utils.SubjectMapper;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SearchService searchService;

    @Autowired
    private BookService bookService;

    @Autowired
    private SubjectMapper subjectMapper;

    @GetMapping("/document-list")
    public String documentList(Model model) {
        List<Subject> subjects = subjectService.getAllSubjects();
        System.out.println(subjects);
        List<Map<String, String>> documents = subjects.stream()
                .map(subject -> {
                    String name = subject.getName() != null ? subject.getName() : "Không xác định";
                    String slug = name.toLowerCase();

                    String imageUrl = subject.getImageUrl() != null
                            ? subject.getImageUrl()
                            : "/images/default-subject.png";

                    String link = "/document/" + slug;

                    return Map.of(
                            "src", imageUrl,
                            "text", name,
                            "link", link
                    );
                })
                .collect(Collectors.toList());

        model.addAttribute("documents", documents);
        return "document/document-list";
    }

    @GetMapping("/search")
    public String searchBooks(
            @RequestParam(value = "title", required = false, defaultValue = "") String title,
            @RequestParam(value = "category", required = false, defaultValue = "all") String category,
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
        model.addAttribute("subjectTitle", "Tài liệu");

        return "document/subject";
    }

    @GetMapping("/{name}")
    public String documentBySlug(@PathVariable String name, Model model, HttpServletRequest request) {
        Subject subject = subjectService.getSubjectByName(name);
        if (subject == null) {
            return "error/404";
        }

        List<SubjectWithBooksDTO> subjects = subjectMapper.mapSubjectsWithBooks(List.of(subject), "id");
        model.addAttribute("subjects", subjects);
        model.addAttribute("title", subject.getName());
        model.addAttribute("requestURI", request.getRequestURI());

        return "document/subject";
    }

    @GetMapping("/detail")
    public String documentDetail(@RequestParam("search_id") Long search_id, Model model) {
        Book book = bookService.getBookById(search_id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        BookResponseDTO dto = new BookResponseDTO(
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
        );

        model.addAttribute("book", dto);
        return "document/detail";
    }
}
