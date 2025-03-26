package se2.group6.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se2.group6.librarymanagement.dto.BookResponseDTO;
import se2.group6.librarymanagement.dto.SubjectWithBooksDTO;
import se2.group6.librarymanagement.model.Book;
import se2.group6.librarymanagement.model.Subject;
import se2.group6.librarymanagement.service.SubjectService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/")
    public String about(Model model) {
        return "about";
    }

    @GetMapping("/home")
    public String home(
            @RequestParam(value = "sortType", required = false, defaultValue = "id") String sortType,
            @RequestParam(value = "subjectIds", required = false) String subjectIds,
            Model model) {

        List<Subject> subjects = subjectService.getAllSubjects();

        if (subjectIds != null && !subjectIds.isEmpty()) {
            List<Long> idList = Arrays.stream(subjectIds.split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .toList();
            subjects = subjects.stream()
                    .filter(subject -> idList.contains(subject.getId()))
                    .toList();
        }

        List<SubjectWithBooksDTO> subjectDTOs = subjects.stream().map(subject -> {
            List<Book> booksList = new ArrayList<>(subject.getBooks());

            if ("most-view".equalsIgnoreCase(sortType)) {
                booksList.sort(Comparator.comparingInt(Book::getViewCount).reversed());
            } else if ("new".equalsIgnoreCase(sortType)) {
                booksList.sort(Comparator.comparing(Book::getCreatedAt).reversed());
            } else { // default: sort theo id
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

        model.addAttribute("subjects", subjectDTOs);
        model.addAttribute("sortType", sortType);
        return "home";
    }
}
