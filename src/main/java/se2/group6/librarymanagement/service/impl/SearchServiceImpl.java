package se2.group6.librarymanagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se2.group6.librarymanagement.dto.BookResponseDTO;
import se2.group6.librarymanagement.dto.SubjectWithBooksDTO;
import se2.group6.librarymanagement.model.Book;
import se2.group6.librarymanagement.service.BookService;
import se2.group6.librarymanagement.service.SearchService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private BookService bookService;

    @Override
    public List<SubjectWithBooksDTO> searchBooks(String title, String category) {
        if (title == null || title.trim().isEmpty()) {
            return Collections.emptyList();
        }

        List<Book> searchedBooks = bookService.searchBooksByTitle(title);

        if (!"all".equalsIgnoreCase(category)) {
            try {
                List<Long> catIds = Arrays.stream(category.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Long::parseLong)
                        .toList();
                searchedBooks = searchedBooks.stream()
                        .filter(book -> catIds.contains(book.getSubject().getId()))
                        .toList();
            } catch (NumberFormatException e) {
            }
        }

        Map<Long, List<Book>> groupedBooks = searchedBooks.stream()
                .collect(Collectors.groupingBy(book -> book.getSubject().getId()));

        List<SubjectWithBooksDTO> subjectDTOs = new ArrayList<>();
        for (Map.Entry<Long, List<Book>> entry : groupedBooks.entrySet()) {
            List<Book> booksList = new ArrayList<>(entry.getValue());
            booksList.sort(Comparator.comparing(Book::getId));

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

        return subjectDTOs;
    }
}
