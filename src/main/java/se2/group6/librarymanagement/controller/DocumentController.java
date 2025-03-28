package se2.group6.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se2.group6.librarymanagement.dto.BookResponseDTO;
import se2.group6.librarymanagement.dto.SubjectWithBooksDTO;
import se2.group6.librarymanagement.model.Book;
import se2.group6.librarymanagement.model.Subject;
import se2.group6.librarymanagement.service.BookService;
import se2.group6.librarymanagement.service.SearchService;
import se2.group6.librarymanagement.service.SubjectService;
import se2.group6.librarymanagement.utils.SubjectMapper;

import java.time.LocalDateTime;
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

        List<Map<String, String>> documents = subjects.stream().map(subject -> {
            String link;
            if (subject.getId().equals(1L)) {
                link = "/document/books";
            } else if (subject.getId().equals(2L)) {
                link = "/document/new-and-magazine";
            } else if (subject.getId().equals(3L)) {
                link = "/document/new-and-magazine";
            } else {
                link = "/document/" + subject.getId();
            }
            return Map.of(
                    "src", subject.getImageUrl(),
                    "text", subject.getName(),
                    "link", link
            );
        }).collect(Collectors.toList());

        model.addAttribute("documents", documents);
        return "document/document-list";
    }

    @GetMapping("/search")
    public String searchBooks(
            @RequestParam(value = "title", required = false, defaultValue = "") String title,
            @RequestParam(value = "category", required = false) String category,
            Model model) {
        List<SubjectWithBooksDTO> subjectDTOs = searchService.searchBooks(title, category);
        model.addAttribute("subjects", subjectDTOs);
        model.addAttribute("title", title);
        model.addAttribute("category", category);
        if ("1".equals(category)) {
            return "document/books";
        } else if ("2, 3".equals(category)) {
            return "document/new-and-magazine";
        } else {
            return "document/scientific-research";
        }
    }

    @GetMapping("/books")
    public String books(Model model) {
        List<Subject> subjects = subjectService.getAllSubjects().stream()
                .filter(subject -> subject.getId().equals(1L))
                .toList();
        List<SubjectWithBooksDTO> subjectDTOs = subjectMapper.mapSubjectsWithBooks(subjects, "id");
        model.addAttribute("subjects", subjectDTOs);
        return "document/books";
    }


    @GetMapping("/new-and-magazine")
    public String newAndMagazine(Model model) {
        List<Subject> subjects = subjectService.getAllSubjects().stream()
                .filter(subject -> subject.getId().equals(2L) || subject.getId().equals(3L))
                .toList();
        List<SubjectWithBooksDTO> subjectDTOs = subjectMapper.mapSubjectsWithBooks(subjects, "id");
        model.addAttribute("subjects", subjectDTOs);
        return "document/new-and-magazine";
    }


    @GetMapping("scientific-research")
    public String scientificResearch(Model model) {
        return "document/scientific-research";
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
