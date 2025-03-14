package se2.group6.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se2.group6.librarymanagement.dto.AuthorResponseDTO;
import se2.group6.librarymanagement.model.Author;
import se2.group6.librarymanagement.model.Book;
import se2.group6.librarymanagement.service.AuthorService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/v1/author")
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponseDTO>> getAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        List<AuthorResponseDTO> response = authors.stream().map(author -> {
            List<Long> bookIds = author.getBooks().stream()
                    .map(Book::getId)
                    .collect(Collectors.toList());
            return new AuthorResponseDTO(
                    author.getId(),
                    author.getName(),
                    author.getBio(),
                    bookIds
            );
        }).toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        Optional<Author> optionalAuthor = authorService.getAuthorById(id);
        return optionalAuthor
                .map(author -> new ResponseEntity<>(author, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Author> createNewAuthor(@RequestBody Author author) {
        Author savedAuthor = authorService.saveAuthor(author);
        return new ResponseEntity<>(savedAuthor, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author authorDetail) {
        Optional<Author> optionalAuthor = authorService.getAuthorById(id);
        if (optionalAuthor.isPresent()) {
            Author author = optionalAuthor.get();
            author.setName(authorDetail.getName());
            author.setBio(authorDetail.getBio());
            author.setBooks(null);
            Author updatedAuthor = authorService.updateAuthor(author);
            return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Author> deleteAuthor(@PathVariable Long id) {
        Optional<Author> optionalAuthor = authorService.getAuthorById(id);
        if (optionalAuthor.isPresent()) {
            authorService.deleteAuthorById(id);
            return new ResponseEntity<>(optionalAuthor.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
