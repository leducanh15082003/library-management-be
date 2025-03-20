package se2.group6.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se2.group6.librarymanagement.dto.BookResponseDTO;
import se2.group6.librarymanagement.dto.SubjectResponseDTO;
import se2.group6.librarymanagement.dto.SubjectWithBooksDTO;
import se2.group6.librarymanagement.model.Book;
import se2.group6.librarymanagement.model.Subject;
import se2.group6.librarymanagement.service.SubjectService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/subject")
public class SubjectController {
    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<List<SubjectResponseDTO>> getAllSubjects() {
        List<Subject> subjects = subjectService.getAllSubjects();
        List<SubjectResponseDTO> response = subjects.stream().map(subject -> {
            List<Long> bookIds = subject.getBooks().stream()
                    .map(Book::getId)
                    .collect(Collectors.toList());
            return new SubjectResponseDTO(
                    subject.getId(),
                    subject.getName(),
                    subject.getDescription(),
                    bookIds
            );
        }).toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/full")
    public ResponseEntity<List<SubjectWithBooksDTO>> getAllSubjectsWithBooks(@RequestParam(value = "sortType", required = false, defaultValue = "id") String sortType) {
        List<Subject> subjects = subjectService.getAllSubjects();
        List<SubjectWithBooksDTO> response = subjects.stream().map(subject -> {
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

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subject> getSubjectById(@PathVariable Long id) {
        Optional<Subject> optionalSubject = subjectService.getSubjectById(id);
        return optionalSubject
                .map(subject -> new ResponseEntity<>(subject, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Subject> createNewSubject(@RequestBody Subject subject) {
        Subject savedSubject = subjectService.saveSubject(subject);
        return new ResponseEntity<>(savedSubject, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(@PathVariable Long id, @RequestBody Subject subjectDetail) {
        Optional<Subject> optionalSubject = subjectService.getSubjectById(id);
        if (optionalSubject.isPresent()) {
            Subject subject = optionalSubject.get();
            subject.setName(subjectDetail.getName());
            subject.setDescription(subjectDetail.getDescription());
            Subject updatedSubject = subjectService.updateSubject(subject);
            return new ResponseEntity<>(updatedSubject, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Subject> deleteSubject(@PathVariable Long id) {
        Optional<Subject> optionalSubject = subjectService.getSubjectById(id);
        if (optionalSubject.isPresent()) {
            subjectService.deleteSubjectById(id);
            return new ResponseEntity<>(optionalSubject.get(), HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
