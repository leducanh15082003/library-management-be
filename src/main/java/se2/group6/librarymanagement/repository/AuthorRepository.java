package se2.group6.librarymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se2.group6.librarymanagement.model.Author;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
   Optional<Author> findByName(String name);
}
