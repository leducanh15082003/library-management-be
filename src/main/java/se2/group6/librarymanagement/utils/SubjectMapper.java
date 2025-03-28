package se2.group6.librarymanagement.utils;

import org.springframework.stereotype.Component;
import se2.group6.librarymanagement.dto.BookResponseDTO;
import se2.group6.librarymanagement.dto.SubjectWithBooksDTO;
import se2.group6.librarymanagement.model.Book;
import se2.group6.librarymanagement.model.Subject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubjectMapper {
    public List<SubjectWithBooksDTO> mapSubjectsWithBooks(List<Subject> subjects, String sortType) {
        return subjects.stream().map(subject -> {
            List<Book> booksList = new ArrayList<>(subject.getBooks());

            // Sắp xếp theo sortType
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

}
